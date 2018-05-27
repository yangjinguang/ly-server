package com.liyu.server.account;

import com.liyu.server.model.AccountDetail;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.TenantService;
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
    @Resource
    private TenantService tenantService;

    @Test
    public void createRoot() {
        AccountDetail newAccount = new AccountDetail();
        newAccount.setUsername("root");
        newAccount.setPassword("123456");
        newAccount.setEmail("root@ly.com");
        Account account = accountService.create(newAccount);
    }

    @Test
    public void createTestAccounts() {
        for (int i = 0; i < 100; i++) {
            AccountDetail newAccount = new AccountDetail();
            String username = "test-user-" + i;
            newAccount.setUsername(username);
            newAccount.setPassword("123456");
            newAccount.setName("测试用户" + i);
            newAccount.setEmail(username + "@ly.com");
            Account account = accountService.create(newAccount);
            // 绑定帐号到租户
            tenantService.bindAccount("0c8b848bec27486b9ee898c41d4f9752", account.getAccountId());
        }

    }

}
