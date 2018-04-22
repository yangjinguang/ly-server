package com.liyu.server.controller;

import com.liyu.server.model.OrganizationTree;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "组织", description = "组织操作", tags = {"组织接口"})
@RestController
@RequestMapping(value = "/api/organizations")
public class OrganizationController {
    @Resource
    private OrganizationService organizationService;
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "获取组织列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(@RequestHeader(value = "X-TENANT-ID") String tenantId,
                            @RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "size", required = false) Integer size) {
        log.info("tenantId: " + tenantId);
        List<Organization> organizations = organizationService.listByTenantId(tenantId);
        return APIResponse.success(organizations);
    }

    @ApiOperation(value = "获取组织树", notes = "")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public APIResponse list(@RequestHeader(value = "X-TENANT-ID") String tenantId) {
        OrganizationTree organizationTree = organizationService.tree(tenantId);
        return APIResponse.success(organizationTree);
    }

    @ApiOperation(value = "创建组织", notes = "")
    @ApiImplicitParam(name = "newOrganization", value = "组织详情", required = true, dataType = "Organization", paramType = "body")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestHeader(value = "X-TENANT-ID") String tenantId,
                              @RequestBody Organization newOrganization) {
        newOrganization.setTenantId(tenantId);
        Organization organization = organizationService.create(newOrganization);
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "更新组织", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "组织ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "newOrganization", value = "组织详情", required = true, dataType = "Organization", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Organization newOrganization) {
        Organization organization = organizationService.update(ULong.valueOf(id), newOrganization);
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "删除组织", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "组织", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable Long id) {
        organizationService.delete(ULong.valueOf(id));
        return APIResponse.success("success");
    }
}
