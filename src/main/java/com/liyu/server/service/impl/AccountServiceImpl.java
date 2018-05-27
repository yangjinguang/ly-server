package com.liyu.server.service.impl;

import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.records.AccountRecord;
import com.liyu.server.utils.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.liyu.server.tables.Account.ACCOUNT;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private DSLContext context;

    @Override
    public Integer count() {
        return context.selectCount().from(ACCOUNT).fetchOne().into(int.class);
    }

    @Override
    public List<Account> list(Integer offset, Integer size) {
        return context.selectFrom(ACCOUNT)
                .offset(offset)
                .limit(size)
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
    public Account getByAccountId(String accountId) {
        return context.selectFrom(ACCOUNT).where(ACCOUNT.ACCOUNT_ID.equal(accountId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到账户"))
                .into(Account.class);
    }

    @Override
    public Account getByUsername(String username) {
        AccountRecord accountRecord = context.selectFrom(ACCOUNT).where(ACCOUNT.USERNAME.equal(username)).fetchOne();
        if (accountRecord == null) {
            return null;
        }
        return accountRecord.into(Account.class);
    }

    @Override
    public Account create(Account newAccount) {
        String salt = RandomStringUtils.random(5, true, true);
        String pass = newAccount.getPassword() + salt;
        String md5Pass = DigestUtils.md5DigestAsHex(pass.getBytes());
        return context.insertInto(ACCOUNT).columns(
                ACCOUNT.ACCOUNT_ID,
                ACCOUNT.USERNAME,
                ACCOUNT.PASSWORD,
                ACCOUNT.SALT,
                ACCOUNT.PHONE,
                ACCOUNT.EMAIL,
                ACCOUNT.WX_OPEN_ID,
                ACCOUNT.AVATAR
        ).values(
                CommonUtils.UUIDGenerator(),
                newAccount.getUsername(),
                md5Pass,
                salt,
                newAccount.getPhone(),
                newAccount.getEmail(),
                newAccount.getWxOpenId(),
                newAccount.getAvatar()
        ).returning().fetchOne().into(Account.class);
    }

    @Override
    public Account update(ULong id, Account newAccount) {
        AccountRecord accountRecord = context.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("account not found"));
        String username = newAccount.getUsername();
        if (username != null && !username.isEmpty()) {
            accountRecord.setUsername(username);
        }
        String email = newAccount.getEmail();
        if (email != null && !email.isEmpty()) {
            accountRecord.setEmail(email);
        }
        String phone = newAccount.getPhone();
        if (phone != null && !phone.isEmpty()) {
            accountRecord.setPhone(phone);
        }
        String wxOpenId = newAccount.getWxOpenId();
        if (wxOpenId != null && !wxOpenId.isEmpty()) {
            accountRecord.setWxOpenId(wxOpenId);
        }
        String avatar = newAccount.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            accountRecord.setAvatar(avatar);
        }
        accountRecord.update();
        return accountRecord.into(Account.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ACCOUNT).where(ACCOUNT.ID.eq(id)).execute();
    }
}
