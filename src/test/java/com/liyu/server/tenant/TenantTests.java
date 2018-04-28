package com.liyu.server.tenant;

import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TenantTests {
    @Resource
    private TenantService tenantService;

    @Test
    public void create() {
        Tenant newTenant = new Tenant();
        newTenant.setName("test-1");
        newTenant.setDescription("test-test");
        tenantService.create(newTenant);
    }

    @Test
    public void bindAccount() {
        tenantService.bindAccount("93d526b86529454599bdda86beb2dbb4", "0e8ca8ce14844c4893649afd2daff5ad");
    }

    @Test
    public void getByAccountUsername() {
        List<Tenant> tenants = tenantService.byAccountUsername("root");
        log.info(tenants.toString());
    }

}
