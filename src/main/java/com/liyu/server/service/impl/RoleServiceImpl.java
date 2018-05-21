package com.liyu.server.service.impl;

import com.liyu.server.service.RoleService;
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

import static com.liyu.server.tables.Role.ROLE;

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
}
