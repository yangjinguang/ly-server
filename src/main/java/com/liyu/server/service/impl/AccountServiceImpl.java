package com.liyu.server.service.impl;

import com.liyu.server.enums.AccountStatusEnum;
import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.records.AccountRecord;
import com.liyu.server.utils.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.liyu.server.tables.Account.ACCOUNT;
import static com.liyu.server.tables.Organization.ORGANIZATION;
import static com.liyu.server.tables.OrganizationAccount.ORGANIZATION_ACCOUNT;
import static com.liyu.server.tables.TenantAccount.TENANT_ACCOUNT;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private DSLContext context;

    @Override
    public List<Account> list() {
        return context.selectFrom(ACCOUNT).fetch().into(Account.class);
    }

    @Override
    public List<Account> listByTenantId(String tenantId) {
        return context.select(ACCOUNT.fields())
                .from(TENANT_ACCOUNT)
                .leftJoin(ACCOUNT)
                .on(ACCOUNT.ACCOUNT_ID.eq(TENANT_ACCOUNT.ACCOUNT_ID))
                .where(TENANT_ACCOUNT.TENANT_ID.eq(tenantId))
                .fetch()
                .into(Account.class);
    }

    @Override
    public Account getByUsernameAndPassword(String username, String password) {
        AccountRecord accountRecord = context.selectFrom(ACCOUNT).where(ACCOUNT.USERNAME.eq(username)).fetchOne();
        if (accountRecord == null) {
            return null;
        } else {
            String saltPass = password + accountRecord.getSalt();
            String md5Pass = DigestUtils.md5DigestAsHex(saltPass.getBytes());
            if (accountRecord.getPassword().equals(md5Pass)) {
                return accountRecord.into(Account.class);
            } else {
                return null;
            }
        }

    }

    @Override
    public Account getById(ULong id) {
        return context.selectFrom(ACCOUNT).where(ACCOUNT.ID.equal(id)).fetchOne().into(Account.class);
    }

    @Override
    public Account getByUsername(String username) {
        return context.selectFrom(ACCOUNT).where(ACCOUNT.USERNAME.equal(username)).fetchOne().into(Account.class);
    }

    @Override
    public Account create(Account newAccount) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        String salt = RandomStringUtils.random(5, true, true);
        String pass = newAccount.getPassword() + salt;
        String md5Pass = DigestUtils.md5DigestAsHex(pass.getBytes());
        return context.insertInto(ACCOUNT).columns(
                ACCOUNT.ACCOUNT_ID,
                ACCOUNT.USERNAME,
                ACCOUNT.PASSWORD,
                ACCOUNT.SALT,
                ACCOUNT.NAME,
                ACCOUNT.PHONE,
                ACCOUNT.EMAIL,
                ACCOUNT.WX_OPEN_ID,
                ACCOUNT.AVATAR,
                ACCOUNT.STATUS,
                ACCOUNT.CREATED_AT,
                ACCOUNT.UPDATED_AT
        ).values(
                CommonUtils.UUIDGenerator(),
                newAccount.getUsername(),
                md5Pass,
                salt,
                newAccount.getName(),
                newAccount.getPhone(),
                newAccount.getEmail(),
                newAccount.getWxOpenId(),
                newAccount.getAvatar(),
                AccountStatusEnum.NORMAL,
                currentTime,
                currentTime
        ).returning().fetchOne().into(Account.class);
    }

    @Override
    public Account update(ULong id, Account updateData) {
        AccountRecord accountRecord = context.selectFrom(ACCOUNT).where(ACCOUNT.ID.eq(id)).fetchOne();
        if (!updateData.getUsername().isEmpty()) {
            accountRecord.setUsername(updateData.getUsername());
        }
        if (!updateData.getEmail().isEmpty()) {
            accountRecord.setEmail(updateData.getEmail());
        }
        accountRecord.update();
        return accountRecord.into(Account.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ACCOUNT).where(ACCOUNT.ID.eq(id)).execute();
    }

    @Override
    public void bindOrganization(String accountId, String organizationId) {
        Integer count = context.selectCount().from(ORGANIZATION_ACCOUNT).where(ORGANIZATION_ACCOUNT.ACCOUNT_ID.eq(accountId), ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(organizationId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(ORGANIZATION_ACCOUNT).columns(
                    ORGANIZATION_ACCOUNT.ACCOUNT_ID,
                    ORGANIZATION_ACCOUNT.ORGANIZATION_ID
            ).values(
                    accountId,
                    organizationId
            ).execute();
        }

    }

    @Override
    public void bindOrganizations(String accountId, List<String> organizationIds) {
        for (String organizationId : organizationIds) {
            this.bindOrganization(accountId, organizationId);
        }
    }

    @Override
    public List<Organization> organizations(String accountId) {
        return context.select(ORGANIZATION.fields())
                .from(ORGANIZATION_ACCOUNT)
                .leftJoin(ORGANIZATION)
                .on(ORGANIZATION_ACCOUNT.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .where(ORGANIZATION_ACCOUNT.ACCOUNT_ID.eq(accountId))
                .fetch().into(Organization.class);
    }
}
