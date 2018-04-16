package com.liyu.server.service.impl;

import com.liyu.server.service.RoleService;
import com.liyu.server.tables.pojos.Role;

import static com.liyu.server.tables.Role.ROLE;

import com.liyu.server.tables.records.RoleRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private DSLContext context;

    @Override
    public List<Role> list() {
        return context.selectFrom(ROLE).fetch().into(Role.class);
    }

    @Override
    public Role create(Role newRole) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return context.insertInto(ROLE).columns(
                ROLE.NAME,
                ROLE.TENANT_ID,
                ROLE.CREATED_AT,
                ROLE.UPDATED_AT
        ).values(
                newRole.getName(),
                newRole.getTenantId(),
                currentTime,
                currentTime
        ).returning().fetchOne().into(Role.class);
    }

    @Override
    public Role update(ULong id, Role newRole) {
        RoleRecord roleRecord = context.selectFrom(ROLE).where(ROLE.ID.eq(id)).fetchOne();
        if (!newRole.getName().isEmpty()) {
            roleRecord.setName(newRole.getName());
        }
        if (newRole.getTenantId() != null) {
            roleRecord.setTenantId(newRole.getTenantId());
        }
        roleRecord.update();
        return roleRecord.into(Role.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ROLE).where(ROLE.ID.eq(id)).execute();
    }
}
