package com.liyu.server.service.impl;

import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.liyu.server.tables.Account.ACCOUNT;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private DSLContext context;

    @Override
    public List<Account> List() {
        return context.selectFrom(ACCOUNT).fetch().into(Account.class);
    }
}
