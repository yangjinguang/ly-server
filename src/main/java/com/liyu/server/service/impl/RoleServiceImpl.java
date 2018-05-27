package com.liyu.server.service.impl;

import com.liyu.server.service.RoleService;
import com.liyu.server.tables.Contact;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Role;
import com.liyu.server.tables.records.RoleRecord;
import com.liyu.server.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.liyu.server.tables.Contact.CONTACT;
import static com.liyu.server.tables.Role.ROLE;
import static com.liyu.server.tables.RoleContact.ROLE_CONTACT;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private DSLContext context;

    @Override
    public Integer count() {
        return context.selectCount().from(ROLE).fetchOne().into(int.class);
    }

    @Override
    public List<Role> list(Integer offset, Integer size) {
        return context.selectFrom(ROLE)
                .offset(offset)
                .limit(size)
                .fetch().into(Role.class);
    }

    @Override
    public Role create(Role newRole) {
        return context.insertInto(ROLE).columns(
                ROLE.ROLE_ID,
                ROLE.NAME,
                ROLE.TENANT_ID
        ).values(
                CommonUtils.UUIDGenerator(),
                newRole.getName(),
                newRole.getTenantId()
        ).returning().fetchOne().into(Role.class);
    }

    @Override
    public Role update(ULong id, Role newRole) {
        RoleRecord roleRecord = context.selectFrom(ROLE)
                .where(ROLE.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此角色"));
        if (!newRole.getName().isEmpty()) {
            roleRecord.setName(newRole.getName());
        }
        if (newRole.getDescription() != null) {
            roleRecord.setDescription(newRole.getDescription());
        }
        roleRecord.update();
        return roleRecord.into(Role.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ROLE).where(ROLE.ID.eq(id)).execute();
    }

    @Override
    public Role enabledOrDisabled(ULong id, Boolean enabled) {
        RoleRecord roleRecord = context.selectFrom(ROLE)
                .where(ROLE.ID.eq(id))
                .fetchOptional().orElseThrow(() -> new NoDataFoundException("未找到此角色"));
        if (enabled != null) {
            roleRecord.set(ROLE.ENABLED, enabled);
        }
        roleRecord.update();
        return roleRecord.into(Role.class);
    }

    @Override
    public Role detail(ULong id) {
        return context.selectFrom(ROLE)
                .where(ROLE.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到角色"))
                .into(Role.class);
    }

    @Override
    public Integer membersCount(String roleId) {
        return context.selectCount().from(ROLE_CONTACT)
                .where(ROLE_CONTACT.ROLE_ID.eq(roleId))
                .fetchOne()
                .into(int.class);
    }

    @Override
    public List<Contact> members(String roleId, Integer offset, Integer size) {
        return context.select(CONTACT.fields())
                .from(ROLE_CONTACT)
                .leftJoin(CONTACT)
                .on(CONTACT.CONTACT_ID.eq(ROLE_CONTACT.CONTACT_ID))
                .where(ROLE_CONTACT.ROLE_ID.eq(roleId))
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Contact.class);
    }

    @Override
    public void bindMembers(String roleId, List<String> contactIds) {
        for (String contactId : contactIds) {
            Integer count = context.selectCount().from(ROLE_CONTACT)
                    .where(ROLE_CONTACT.ROLE_ID.eq(roleId), ROLE_CONTACT.CONTACT_ID.eq(contactId))
                    .fetchOne()
                    .into(int.class);
            if (count > 0) {
                continue;
            }
            context.insertInto(ROLE_CONTACT).columns(
                    ROLE_CONTACT.ROLE_ID,
                    ROLE_CONTACT.CONTACT_ID
            ).values(
                    roleId,
                    contactId
            ).execute();
        }
    }

    @Override
    public void unBindMembers(String roleId, List<String> contactIds) {
        context.deleteFrom(ROLE_CONTACT)
                .where(ROLE_CONTACT.ROLE_ID.eq(roleId), ROLE_CONTACT.CONTACT_ID.in(contactIds))
                .execute();
    }
}
