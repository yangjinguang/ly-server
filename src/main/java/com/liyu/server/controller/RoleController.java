package com.liyu.server.controller;

import com.liyu.server.model.RoleBindMembersBody;
import com.liyu.server.service.RoleService;
import com.liyu.server.tables.Contact;
import com.liyu.server.tables.pojos.Role;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "角色", description = "角色操作", tags = {"角色接口"})
@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "获取角色列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        Integer total = roleService.count();
        List<Role> roles = roleService.list((page - 1) * size, size);
        return APIResponse.withPagination(roles, total, page, size);
    }

    @ApiOperation(value = "创建角色", notes = "")
    @ApiImplicitParam(name = "body", value = "角色详情", required = true, dataType = "Role", paramType = "body")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody Role newRole,
                              @RequestHeader(value = "X-TENANT-ID") String tenantId) {
        newRole.setTenantId(tenantId);
        Role role = roleService.create(newRole);
        return APIResponse.success(role);
    }

    @ApiOperation(value = "更新角色", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "body", value = "角色详情", required = true, dataType = "Role", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Role newRole) {
        Role role = roleService.update(ULong.valueOf(id), newRole);
        return APIResponse.success(role);
    }

    @ApiOperation(value = "删除角色", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "角色", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable Long id) {
        roleService.delete(ULong.valueOf(id));
        return APIResponse.success("success");
    }

    @ApiOperation(value = "启用/禁用角色", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean", paramType = "path")
    })
    @RequestMapping(value = "/{id}/{enabled}", method = RequestMethod.PUT)
    public APIResponse enabledOrDisabled(@PathVariable(value = "id", required = true) Long id,
                                         @PathVariable(value = "enabled", required = true) Boolean enabled) {
        Role role = roleService.enabledOrDisabled(ULong.valueOf(id), enabled);
        return APIResponse.success(role);
    }

    @ApiOperation(value = "角色详细信息", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path"),
    })
    @RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
    public APIResponse detail(@PathVariable(value = "id", required = true) Long id) {
        Role role = roleService.detail(ULong.valueOf(id));
        return APIResponse.success(role);
    }

    @ApiOperation(value = "角色成员列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/{roleId}/members", method = RequestMethod.GET)
    public APIResponse members(@PathVariable(value = "roleId", required = true) String roleId,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        Integer total = roleService.membersCount(roleId);
        List<Contact> members = roleService.members(roleId, (page - 1) * size, size);
        return APIResponse.withPagination(members, total, page, size);
    }

    @ApiOperation(value = "角色绑定联系人", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "bindBody", value = "绑定数据", required = true, dataType = "RoleBindMembersBody", paramType = "body"),
    })
    @RequestMapping(value = "/{roleId}/bindMembers", method = RequestMethod.PUT)
    public APIResponse bindMembers(@PathVariable(value = "roleId", required = true) String roleId,
                                   @RequestBody RoleBindMembersBody bindBody) {
        roleService.bindMembers(roleId, bindBody.getContactIds());
        return APIResponse.success("success");
    }

    @ApiOperation(value = "角色解绑账户", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "bindBody", value = "绑定数据", required = true, dataType = "RoleBindMembersBody", paramType = "body"),
    })
    @RequestMapping(value = "/{roleId}/unBindMembers", method = RequestMethod.PUT)
    public APIResponse unBindMembers(@PathVariable(value = "roleId", required = true) String roleId,
                                     @RequestBody RoleBindMembersBody bindBody) {
        roleService.unBindMembers(roleId, bindBody.getContactIds());
        return APIResponse.success("success");
    }
}
