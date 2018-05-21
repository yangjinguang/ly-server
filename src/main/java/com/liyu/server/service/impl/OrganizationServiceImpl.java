package com.liyu.server.service.impl;

import com.liyu.server.enums.AccountStatusEnum;
import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.model.OrganizationTree;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.records.OrganizationRecord;
import com.liyu.server.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

import static com.liyu.server.tables.Account.ACCOUNT;
import static com.liyu.server.tables.Organization.ORGANIZATION;
import static com.liyu.server.tables.OrganizationAccount.ORGANIZATION_ACCOUNT;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Resource
    private DSLContext context;

    @Override
    public Organization byId(ULong id) {
        return context.selectFrom(ORGANIZATION).where(ORGANIZATION.ID.eq(id)).fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("organization not found"))
                .into(Organization.class);
    }

    @Override
    public OrganizationTree getRoot(String tenantId) {
        Organization organization = context.selectFrom(ORGANIZATION).where(ORGANIZATION.PARENT_ID.eq("ROOT"), ORGANIZATION.TENANT_ID.eq(tenantId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("organization not found"))
                .into(Organization.class);
        OrganizationTree organizationTreeRoot = new OrganizationTree(organization);
        List<Organization> organizations = context.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.PARENT_ID.eq(organization.getOrganizationId()))
                .orderBy(ORGANIZATION.ORDER)
                .fetch()
                .into(Organization.class);
        ArrayList<OrganizationTree> children = new ArrayList<>();
        for (Organization org : organizations) {
            OrganizationTree newTree = new OrganizationTree(org);
            newTree.setNumberOfChildren(this.countByParentId(org.getOrganizationId()));
            children.add(newTree);
        }
        organizationTreeRoot.setChildren(children);
        return organizationTreeRoot;
    }

    private void _fillRoute(Organization curOrganization, List<Organization> organizations, List<String> route) {
        for (Organization organization : organizations) {
            if (organization.getOrganizationId().equals(curOrganization.getParentId())) {
                route.add(organization.getName());
                if (organization.getParentId() != null && !organization.getParentId().equals("ROOT")) {
                    this._fillRoute(organization, organizations, route);
                }
                break;
            }
        }
    }

    @Override
    public List<String> getOrganizationRoute(Organization organization, List<Organization> organizations) {
        ArrayList<String> route = new ArrayList<>();
        route.add(organization.getName());
        this._fillRoute(organization, organizations, route);
        Collections.reverse(route);
        return route;
    }

    @Override
    public List<OrganizationDetail> listByTenantId(String tenantId) {
        List<Organization> organizations = context.selectFrom(ORGANIZATION).where(ORGANIZATION.TENANT_ID.eq(tenantId)).fetch().into(Organization.class);
        ArrayList<OrganizationDetail> organizationDetails = new ArrayList<>();
        for (Organization organization : organizations) {
            OrganizationDetail organizationDetail = new OrganizationDetail(organization);
            List<String> route = this.getOrganizationRoute(organization, organizations);
            organizationDetail.setRoute(route);
            organizationDetails.add(organizationDetail);
        }
        return organizationDetails;
    }

    private void orgTreeChild(OrganizationTree organizationTree, List<Organization> organizations) {
        if (organizationTree == null) {
            return;
        }
        ArrayList<OrganizationTree> children = new ArrayList<>();
        for (Organization organization : organizations) {
            if (organization.getParentId().equals(organizationTree.getOrganizationId())) {
                children.add(new OrganizationTree(organization));
            }
        }
        if (children.size() > 0) {
            organizationTree.setChildren(children);
            for (OrganizationTree tree : organizationTree.getChildren()) {
                this.orgTreeChild(tree, organizations);
            }
        }
    }

    @Override
    public List<OrganizationTree> listByParentId(String parentId) {
        List<Organization> organizations = context.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.PARENT_ID.eq(parentId))
                .orderBy(ORGANIZATION.ORDER)
                .fetch().into(Organization.class);
        ArrayList<OrganizationTree> organizationTrees = new ArrayList<>();
        for (Organization organization : organizations) {
            OrganizationTree newTree = new OrganizationTree(organization);
            newTree.setNumberOfChildren(this.countByParentId(organization.getOrganizationId()));
            organizationTrees.add(newTree);
        }
        return organizationTrees;
    }


    @Override
    public int countByParentId(String parentId) {
        return context.selectCount().from(ORGANIZATION).where(ORGANIZATION.PARENT_ID.eq(parentId)).fetchOne().into(int.class);
    }

    @Override
    public OrganizationTree tree(String tenantId) {

        OrganizationTree organizationTree = null;
        List<Organization> organizations = context.selectFrom(ORGANIZATION).where(ORGANIZATION.TENANT_ID.eq(tenantId)).fetch().into(Organization.class);
        for (Organization organization : organizations) {
            if (organization.getIsRoot() != null && organization.getIsRoot()) {
                log.info("organization name: " + organization.getName());
                organizationTree = new OrganizationTree(organization);
            }
        }
        this.orgTreeChild(organizationTree, organizations);
        return organizationTree;
    }

    @Override
    public Organization create(Organization newOrganization) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return context.insertInto(ORGANIZATION).columns(
                ORGANIZATION.ORGANIZATION_ID,
                ORGANIZATION.NAME,
                ORGANIZATION.AVATAR,
                ORGANIZATION.IS_ROOT,
                ORGANIZATION.IS_CLASS,
                ORGANIZATION.PARENT_ID,
                ORGANIZATION.TENANT_ID,
                ORGANIZATION.CREATED_AT,
                ORGANIZATION.UPDATED_AT
        ).values(
                CommonUtils.UUIDGenerator(),
                newOrganization.getName(),
                newOrganization.getAvatar(),
                newOrganization.getIsRoot(),
                newOrganization.getIsClass(),
                newOrganization.getParentId(),
                newOrganization.getTenantId(),
                currentTime,
                currentTime
        ).returning().fetchOne().into(Organization.class);
    }

    @Override
    public Organization update(ULong id, Organization newOrganization) {
        OrganizationRecord organizationRecord = context.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("organization not found"));
        String name = newOrganization.getName();
        if (name != null && !name.isEmpty()) {
            organizationRecord.setName(name);
        }
        String avatar = newOrganization.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            organizationRecord.setAvatar(avatar);
        }
        String description = newOrganization.getDescription();
        if (description != null && !description.isEmpty()) {
            organizationRecord.setDescription(description);
        }
        Boolean isRoot = newOrganization.getIsRoot();
        if (isRoot != null) {
            organizationRecord.setIsRoot(isRoot);
        }
        Boolean isClass = newOrganization.getIsClass();
        if (isClass != null) {
            organizationRecord.setIsClass(isClass);
        }
        String parentId = newOrganization.getParentId();
        if (parentId != null && !parentId.isEmpty()) {
            organizationRecord.setParentId(parentId);
        }
        String tenantId = newOrganization.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            organizationRecord.setTenantId(newOrganization.getTenantId());
        }
        organizationRecord.update();
        return organizationRecord.into(Organization.class);
    }

    @Override
    public void delete(String organizationId) {
        context.deleteFrom(ORGANIZATION).where(ORGANIZATION.ORGANIZATION_ID.eq(organizationId)).execute();
        context.deleteFrom(ORGANIZATION_ACCOUNT).where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId)).execute();
    }

    @Override
    public void bindAccount(String organizationId, String accountId) {
        Integer count = context.selectCount().from(ORGANIZATION_ACCOUNT).where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId), ORGANIZATION_ACCOUNT.ACCOUNT_ID.eq(accountId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(ORGANIZATION_ACCOUNT).columns(
                    ORGANIZATION_ACCOUNT.ORGANIZATION_ID,
                    ORGANIZATION_ACCOUNT.ACCOUNT_ID
            ).values(
                    organizationId,
                    accountId
            ).execute();
        }
    }

    @Override
    public void unbindAccount(String organizationId, String accountId) {
        context.deleteFrom(ORGANIZATION_ACCOUNT).where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId), ORGANIZATION_ACCOUNT.ACCOUNT_ID.eq(accountId)).execute();
    }

    @Override
    public Integer accountsCount(String organizationId) {
        return context.selectCount().from(ORGANIZATION_ACCOUNT)
                .where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId))
                .fetchOne()
                .into(int.class);
    }

    @Override
    public List<Account> accounts(String organizationId, Integer offset, Integer size) {
        return context.select(ACCOUNT.fields()).from(ORGANIZATION_ACCOUNT)
                .leftJoin(ACCOUNT)
                .on(ACCOUNT.ACCOUNT_ID.eq(ORGANIZATION_ACCOUNT.ACCOUNT_ID))
                .where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId), ACCOUNT.STATUS.notEqual(AccountStatusEnum.DELETED))
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Account.class);
    }

    private void getChildrenOrganizationIds(String organizationId, List<String> resIds) {
        List<String> organizationIds = context.select(ORGANIZATION.ORGANIZATION_ID).from(ORGANIZATION)
                .where(ORGANIZATION.PARENT_ID.eq(organizationId)).fetch().into(String.class);
        resIds.addAll(organizationIds);
        if (organizationIds.size() > 0) {
            for (String childId : organizationIds) {
                this.getChildrenOrganizationIds(childId, resIds);
            }
        }
    }

    @Override
    public List<String> getAllChildrenOrganizationIds(String organizationId) {
        ArrayList<String> resIds = new ArrayList<String>();
        this.getChildrenOrganizationIds(organizationId, resIds);
        resIds.add(organizationId);
        return new ArrayList<String>(new HashSet<>(resIds));
    }

    @Override
    public Integer accountDeepCount(List<String> ids) {
        return context.selectCount().from(ORGANIZATION_ACCOUNT)
                .leftJoin(ACCOUNT)
                .on(ACCOUNT.ACCOUNT_ID.eq(ORGANIZATION_ACCOUNT.ACCOUNT_ID))
                .where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.in(ids), ACCOUNT.STATUS.notEqual(AccountStatusEnum.DELETED))
                .fetchOne()
                .into(int.class);
    }

    @Override
    public List<Account> accountsDeep(List<String> ids, Integer offset, Integer size) {
//        List<String> ids = this.getAllChildrenOrganizationIds(organizationId);
//        ids.add(organizationId);
//        ids = new ArrayList<String>(new HashSet<>(ids));
        return context.select(ACCOUNT.fields())
                .from(ORGANIZATION_ACCOUNT)
                .leftJoin(ACCOUNT)
                .on(ACCOUNT.ACCOUNT_ID.eq(ORGANIZATION_ACCOUNT.ACCOUNT_ID))
                .where(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.in(ids), ACCOUNT.STATUS.notEqual(AccountStatusEnum.DELETED))
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Account.class);
    }

    @Override
    public void changeOrder(List<ULong> ids) {
        for (int i = 0; i < ids.size(); i++) {
            ULong id = ids.get(i);
            context.update(ORGANIZATION)
                    .set(ORGANIZATION.ORDER, i)
                    .where(ORGANIZATION.ID.eq(id))
                    .execute();
        }
    }
}
