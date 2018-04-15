package com.liyu.server.controller;

import com.liyu.server.model.AccountCreateData;
import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.*;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "账户", description = "账户操作", tags = {"账户接口"})
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "获取账户列表", notes = "")
    @ResponseBody
    @ApiParam(name = "page", value = "页码", required = false)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(@RequestParam(value = "page", required = false) Integer page) {
        List<Account> accounts = accountService.list();
        for (Account account : accounts) {
            account.setPassword(null);
            account.setSalt(null);
        }
        return APIResponse.success(accounts);
    }

    @ApiOperation(value = "创建新账户", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "createData", value = "账户信息", required = true, dataType = "AccountCreateData")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody AccountCreateData createData) {
        Account account = accountService.create(createData);
        account.setSalt(null);
        account.setPassword(null);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "更新账户", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账户ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "updateData", value = "账户信息", required = true, dataType = "Account", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Account updateData) {
        Account account = accountService.update(ULong.valueOf(id), updateData);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "删除账户", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "账户ID", required = true, paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable Long id) {
        accountService.delete(ULong.valueOf(id));
        return APIResponse.success("success");
    }
}
