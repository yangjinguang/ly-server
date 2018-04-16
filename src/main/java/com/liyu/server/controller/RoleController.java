package com.liyu.server.controller;

import com.liyu.server.service.RoleService;
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
    public APIResponse list(@RequestParam(value = "page", required = false) Integer page) {
        List<Role> roles = roleService.list();
        return APIResponse.success(roles);
    }

    @ApiOperation(value = "创建角色", notes = "")
    @ApiImplicitParam(name = "body", value = "角色详情", required = true, dataType = "Role", paramType = "body")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody Role newRole) {
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
}
