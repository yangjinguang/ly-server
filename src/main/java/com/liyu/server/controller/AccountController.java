package com.liyu.server.controller;

import com.liyu.server.model.AccountCreateBody;
import com.liyu.server.model.AccountDetail;
import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Api(value = "账户", description = "账户操作", tags = {"账户接口"})
@RestController
@Slf4j
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
    @ApiImplicitParam(name = "createData", value = "账户信息", required = true, dataType = "Account")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestBody AccountCreateBody newAccount) {
        Account account = accountService.create(newAccount.getAccount());
        List<String> organizationIds = newAccount.getOrganizationIds();
        if (organizationIds.size() > 0) {
            accountService.bindOrganizations(account.getAccountId(), newAccount.getOrganizationIds());
        }
        account.setSalt(null);
        account.setPassword(null);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "更新账户", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "updateData", value = "账户信息", required = true, dataType = "Account", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Account updateData) {
        Account account = accountService.update(ULong.valueOf(id), updateData);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "删除账户", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "账户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable Long id) {
        accountService.delete(ULong.valueOf(id));
        return APIResponse.success("success");
    }

    @ApiOperation(value = "获取当前用户信息", notes = "")
    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public APIResponse profile(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        log.info(principal.getName());
        Account account = accountService.getByUsername(principal.getName());
        account.setPassword(null);
        account.setSalt(null);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "获取账户详情", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "账户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse detail(@PathVariable Long id) {
        Account account = accountService.getById(ULong.valueOf(id));
        account.setPassword(null);
        account.setSalt(null);
        AccountDetail accountDetail = new AccountDetail(account);
        List<Organization> organizations = accountService.organizations(account.getAccountId());
        accountDetail.setOrganizations(organizations);
        return APIResponse.success(accountDetail);
    }
}
