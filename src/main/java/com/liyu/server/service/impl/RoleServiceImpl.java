package com.liyu.server.service.impl;

import com.liyu.server.service.RoleService;
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

import static com.liyu.server.tables.Account.ACCOUNT;
import static com.liyu.server.tables.Role.ROLE;
import static com.liyu.server.tables.RoleAccount.ROLE_ACCOUNT;

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
        return context.selectCount().from(ROLE_ACCOUNT)
                .where(ROLE_ACCOUNT.ROLE_ID.eq(roleId))
                .fetchOne()
                .into(int.class);
    }

    @Override
    public List<Account> members(String roleId, Integer offset, Integer size) {
        return context.select(ACCOUNT.fields())
                .from(ROLE_ACCOUNT)
                .leftJoin(ACCOUNT)
                .on(ACCOUNT.ACCOUNT_ID.eq(ROLE_ACCOUNT.ACCOUNT_ID))
                .where(ROLE_ACCOUNT.ROLE_ID.eq(roleId))
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Account.class);
    }

    @Override
    public void bindMembers(String roleId, List<String> accountIds) {
        for (String accountId : accountIds) {
            Integer count = context.selectCount().from(ROLE_ACCOUNT)
                    .where(ROLE_ACCOUNT.ROLE_ID.eq(roleId), ROLE_ACCOUNT.ACCOUNT_ID.eq(accountId))
                    .fetchOne()
                    .into(int.class);
            if (count > 0) {
                continue;
            }
            context.insertInto(ROLE_ACCOUNT).columns(
                    ROLE_ACCOUNT.ROLE_ID,
                    ROLE_ACCOUNT.ACCOUNT_ID
            ).values(
                    roleId,
                    accountId
            ).execute();
        }
    }

    @Override
    public void unBindMembers(String roleId, List<String> accountIds) {
        context.deleteFrom(ROLE_ACCOUNT)
                .where(ROLE_ACCOUNT.ROLE_ID.eq(roleId), ROLE_ACCOUNT.ACCOUNT_ID.in(accountIds))
                .execute();
    }
}
