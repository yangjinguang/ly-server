package com.liyu.server;

import com.liyu.server.service.AccountService;
import com.liyu.server.service.ContactService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.pojos.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {
    @Resource
    private TenantService tenantService;
    @Resource
    private AccountService accountService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private ContactService contactService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void md5Test() {
        String str = "123456ccdd";
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        log.info("md5 abc:" + md5);
    }

    @Test
    public void testDataInt() {
        // 创建租户
        Tenant newTenant = new Tenant();
        newTenant.setName("test-tenant");
        newTenant.setDescription("for test");
        newTenant.setAddress("xx address");
        Tenant tenant = tenantService.create(newTenant);

        // 创建组织
        Organization newOrganization = new Organization();
        newOrganization.setTenantId(tenant.getTenantId());
        newOrganization.setName("全公司");
        newOrganization.setDescription("全公司");
        newOrganization.setIsRoot(true);
        newOrganization.setIsClass(false);
        newOrganization.setParentId("ROOT");
        Organization organization = organizationService.create(newOrganization);

        // 创建帐号
        Account newAccount = new Account();
        newAccount.setUsername("root");
        newAccount.setPassword("123456");
        newAccount.setPhone("10000000000");
        newAccount.setEmail("root@ly.com");
        newAccount.setWxOpenId("");
        Account account = accountService.create(newAccount);

        // 创建联系人
        Contact newContact = new Contact();
        newContact.setAccountId(account.getAccountId());
        newContact.setName("超级管理员");
        newContact.setEmail("root@ly.com");
        newContact.setPhone("10000000000");
        newContact.setTenantId(tenant.getTenantId());
        Contact contact = contactService.create(newContact, account.getAccountId(), tenant.getTenantId());

        // 绑定帐号到租户
//        tenantService.bindAccount(tenant.getTenantId(), account.getAccountId());

        // 绑定帐号到组织
        organizationService.bindContact(organization.getOrganizationId(), contact.getContactId());
    }

    @Test
    public void paginationTest() {
        Integer total = 110;
        Integer size = 10;
        Integer page = 1;
        int i = total % size;
        log.info("%: " + i);
        int totalPages = total / size;
        if (i > 0) {
            totalPages++;
        }
        log.info("totalPages:" + totalPages);
    }

}
