package com.liyu.server.account;

import com.liyu.server.model.AccountDetail;
import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTests {
    @Resource
    private AccountService accountService;

    @Test
    public void createRoot() {
        AccountDetail newAccount = new AccountDetail();
        newAccount.setUsername("root");
        newAccount.setPassword("123456");
        newAccount.setEmail("root@ly.com");
        Account account = accountService.create(newAccount);
    }

}
