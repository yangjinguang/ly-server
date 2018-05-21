package com.liyu.server.service.impl;

import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Tenant;
import com.liyu.server.tables.records.AccountRecord;
import com.liyu.server.tables.records.TenantAccountRecord;
import com.liyu.server.tables.records.TenantRecord;
import com.liyu.server.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.liyu.server.tables.Account.ACCOUNT;
import static com.liyu.server.tables.Tenant.TENANT;
import static com.liyu.server.tables.TenantAccount.TENANT_ACCOUNT;

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
        TenantRecord tenantRecord = context.insertInto(TENANT).columns(
                TENANT.TENANT_ID,
                TENANT.NAME,
                TENANT.ADDRESS,
                TENANT.DESCRIPTION,
                TENANT.AVATAR
        ).values(
                CommonUtils.UUIDGenerator(),
                newTenant.getName(),
                newTenant.getAddress(),
                newTenant.getDescription(),
                newTenant.getAvatar()
        ).returning().fetchOne();
        return tenantRecord.into(Tenant.class);
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
        if (!newTenant.getAvatar().isEmpty()) {
            tenantRecord.setAvatar(newTenant.getAvatar());
        }
        tenantRecord.update();
        return tenantRecord.into(Tenant.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(TENANT).where(TENANT.ID.eq(id)).execute();
    }

    @Override
    public void bindAccount(String tenantId, String accountId) {
        Integer count = context.selectCount().from(TENANT_ACCOUNT).where(TENANT_ACCOUNT.TENANT_ID.eq(tenantId), TENANT_ACCOUNT.ACCOUNT_ID.eq(accountId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(TENANT_ACCOUNT).columns(
                    TENANT_ACCOUNT.ACCOUNT_ID,
                    TENANT_ACCOUNT.TENANT_ID
            ).values(
                    accountId,
                    tenantId
            ).execute();
        }

    }

    @Override
    public List<Tenant> byAccountId(String accountId) {
        return context.selectFrom(TENANT_ACCOUNT).where(TENANT_ACCOUNT.ACCOUNT_ID.eq(accountId)).fetch().into(Tenant.class);
    }

    @Override
    public List<Tenant> byAccountUsername(String username) {
        AccountRecord accountRecord = context.selectFrom(ACCOUNT).where(ACCOUNT.USERNAME.eq(username)).fetchOne();
        return context.select(TENANT.fields()).from(TENANT_ACCOUNT).leftJoin(TENANT).on(TENANT.TENANT_ID.eq(TENANT_ACCOUNT.TENANT_ID)).where(TENANT_ACCOUNT.ACCOUNT_ID.eq(accountRecord.getAccountId())).fetch().into(Tenant.class);
    }
}
