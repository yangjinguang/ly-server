package com.liyu.server.service.impl;

import com.google.common.primitives.UnsignedLong;
import com.liyu.server.model.AccountCreateData;
import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.records.AccountRecord;
import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.liyu.server.tables.Account.ACCOUNT;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private DSLContext context;

    @Override
    public List<Account> list() {
        return context.selectFrom(ACCOUNT).fetch().into(Account.class);
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
    public Account create(AccountCreateData createData) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        String salt = RandomStringUtils.random(5, true, true);
        String pass = createData.getPassword() + salt;
        String md5Pass = DigestUtils.md5DigestAsHex(pass.getBytes());
        return context.insertInto(ACCOUNT).columns(
                ACCOUNT.USERNAME,
                ACCOUNT.PASSWORD,
                ACCOUNT.SALT,
                ACCOUNT.PHONE,
                ACCOUNT.EMAIL,
                ACCOUNT.TENANT_ID,
                ACCOUNT.ORGANIZATION_ID,
                ACCOUNT.ROLE_ID,
                ACCOUNT.CREATED_AT,
                ACCOUNT.UPDATED_AT
        ).values(
                createData.getUsername(),
                md5Pass,
                salt,
                createData.getPhone(),
                createData.getEmail(),
                ULong.valueOf(createData.getTenantId()),
                ULong.valueOf(createData.getOrganizationId()),
                ULong.valueOf(0),
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
        if (updateData.getOrganizationId() != null) {
            accountRecord.setOrganizationId(updateData.getOrganizationId());
        }
        if (updateData.getTenantId() != null) {
            accountRecord.setTenantId(updateData.getTenantId());
        }
        if (updateData.getRoleId() != null) {
            accountRecord.setRoleId(updateData.getRoleId());
        }
        accountRecord.update();
        return accountRecord.into(Account.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ACCOUNT).where(ACCOUNT.ID.eq(id)).execute();
    }
}
