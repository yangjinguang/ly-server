package com.liyu.server.controller;

import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.model.OrganizationOrderBody;
import com.liyu.server.model.OrganizationTree;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Contact;
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
import java.util.ArrayList;
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
        List<OrganizationDetail> organizationDetails = organizationService.listByTenantId(tenantId);
        return APIResponse.success(organizationDetails);
    }

    @ApiOperation(value = "获取组织详情", notes = "")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse list(@PathVariable(value = "id", required = true) Long id) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "获取组织根节点", notes = "")
    @RequestMapping(value = "/root", method = RequestMethod.GET)
    public APIResponse getRoot(@RequestHeader(value = "X-TENANT-ID") String tenantId) {
        OrganizationTree root = organizationService.getRoot(tenantId);
        return APIResponse.success(root);
    }

    @ApiOperation(value = "获取组织子节点", notes = "")
    @ApiImplicitParam(name = "parentId", value = "parentId", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{parentId}/children", method = RequestMethod.GET)
    public APIResponse children(@PathVariable(value = "parentId", required = true) String parentId) {
        List<OrganizationTree> organizationTrees = organizationService.listByParentId(parentId);
        return APIResponse.success(organizationTrees);
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
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "newOrganization", value = "组织详情", required = true, dataType = "Organization", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Organization newOrganization) {
        Organization organization = organizationService.update(ULong.valueOf(id), newOrganization);
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "删除组织", notes = "")
    @ApiImplicitParam(name = "organizationId", value = "组织 ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable String organizationId) {
        organizationService.delete(organizationId);
        return APIResponse.success("success");
    }

    @ApiOperation(value = "绑定联系人", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "accountIds", value = "用户ID列表", required = true, dataType = "List<String>", paramType = "body"),
    })
    @RequestMapping(value = "/{id}/bindAccounts", method = RequestMethod.PUT)
    public APIResponse bindAccounts(@PathVariable Long id,
                                    @RequestBody List<String> accountIds) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        for (String accountId : accountIds) {
            log.info("bind accountId: " + accountId);
            organizationService.bindContact(organization.getOrganizationId(), accountId);
        }
        return APIResponse.success("success");
    }

    @ApiOperation(value = "取消绑定联系人", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "contactIds", value = "联系人ID列表", required = true, dataType = "List<String>", paramType = "body"),
    })
    @RequestMapping(value = "/{id}/unbindContacts", method = RequestMethod.PUT)
    public APIResponse unbindAccounts(@PathVariable Long id,
                                      @RequestBody List<String> contactIds) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        for (String contactId : contactIds) {
            log.info("unbind contactId: " + contactId);
            organizationService.unbindContact(organization.getOrganizationId(), contactId);
        }
        return APIResponse.success("success");
    }

    @ApiOperation(value = "组织绑定的联系人列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "deep", value = "是否获取子节点", required = false, dataType = "Boolean", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/{id}/contacts", method = RequestMethod.GET)
    public APIResponse list(@PathVariable(value = "id", required = true) Long id,
                            @RequestParam(value = "deep", required = false) Boolean deep,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        List<Contact> contacts;
        Integer total;
        if (deep) {
            List<String> ids = organizationService.getAllChildrenOrganizationIds(organization.getOrganizationId());
            total = organizationService.contactDeepCount(ids);
            contacts = organizationService.contactsDeep(ids, (page - 1) * size, size);
        } else {
            total = organizationService.contactsCount(organization.getOrganizationId());
            contacts = organizationService.contacts(organization.getOrganizationId(), (page - 1) * size, size);
        }
        return APIResponse.withPagination(contacts, total, page, size);
    }

    @ApiOperation(value = "组织排序", notes = "")
    @RequestMapping(value = "/order", method = RequestMethod.PUT)
    public APIResponse order(@RequestBody OrganizationOrderBody orderBody) {
        ArrayList<ULong> uLongs = new ArrayList<>();
        for (Long id : orderBody.getIds()) {
            uLongs.add(ULong.valueOf(id));
        }
        organizationService.changeOrder(uLongs);
        return APIResponse.success("success");
    }
}
