package com.liyu.server.service.impl;

import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Tenant;
import com.liyu.server.tables.records.TenantRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.liyu.server.tables.Tenant.TENANT;

@Slf4j
@Service
public class TenantServiceImpl implements TenantService {
    @Resource
    private DSLContext context;

    @Override
    public List<Tenant> list() {
        return context.selectFrom(TENANT).fetch().into(Tenant.class);
    }

    @Override
    public Tenant create(Tenant newTenant) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return context.insertInto(TENANT).columns(
                TENANT.NAME,
                TENANT.ADDRESS,
                TENANT.DESCRIPTION,
                TENANT.CREATED_AT,
                TENANT.UPDATED_AT
        ).values(
                newTenant.getName(),
                newTenant.getAddress(),
                newTenant.getDescription(),
                currentTime,
                currentTime
        ).returning().fetchOne().into(Tenant.class);
    }

    @Override
    public Tenant update(ULong id, Tenant newTenant) {
        TenantRecord tenantRecord = context.selectFrom(TENANT).where(TENANT.ID.eq(id)).fetchOne();
        if (!newTenant.getName().isEmpty()) {
            tenantRecord.setName(newTenant.getName());
        }
        if (!newTenant.getAddress().isEmpty()) {
            tenantRecord.setAddress(newTenant.getAddress());
        }
        if (!newTenant.getDescription().isEmpty()) {
            tenantRecord.setDescription(newTenant.getDescription());
        }
        tenantRecord.update();
        return tenantRecord.into(Tenant.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(TENANT).where(TENANT.ID.eq(id)).execute();
    }
}
