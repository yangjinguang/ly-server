package com.liyu.server.controller;

import com.liyu.server.service.AccountService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.pojos.Tenant;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@Api(value = "租户", description = "租户操作", tags = {"租户接口"})
@RestController
@RequestMapping(value = "/api/tenants")
public class TenantController {
    @Resource
    private TenantService tenantService;
    @Resource
    private AccountService accountService;
    @Resource
    private OrganizationService organizationService;

    @ApiOperation(value = "获取租户列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "size", required = false) Integer size) {
        List<Tenant> tenants = tenantService.list();
        return APIResponse.success(tenants);
    }

    @ApiOperation(value = "创建租户", notes = "")
    @ApiImplicitParam(name = "newTenant", value = "租户详情", required = true, dataType = "Tenant", paramType = "body")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody Tenant newTenant) {
        Tenant tenant = tenantService.create(newTenant);
        Organization newOrganization = new Organization();
        newOrganization.setTenantId(tenant.getTenantId());
        newOrganization.setName("全公司");
        newOrganization.setDescription("全公司");
        newOrganization.setIsRoot(true);
        newOrganization.setIsClass(false);
        organizationService.create(newOrganization);
        return APIResponse.success(tenant);
    }

    @ApiOperation(value = "更新租户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "租户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "newTenant", value = "租户详情", required = true, dataType = "Tenant", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Tenant newTenant) {
        Tenant tenant = tenantService.update(ULong.valueOf(id), newTenant);
        return APIResponse.success(tenant);
    }

    @ApiOperation(value = "删除租户", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "租户", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable Long id) {
        tenantService.delete(ULong.valueOf(id));
        return APIResponse.success("success");
    }

    @ApiOperation(value = "获取当前用户的租户列表", notes = "")
    @ResponseBody
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public APIResponse tenants(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        List<Tenant> tenants = tenantService.byAccountUsername(principal.getName());
        return APIResponse.success(tenants);
    }
}
