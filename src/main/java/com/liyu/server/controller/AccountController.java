package com.liyu.server.controller;

import com.liyu.server.model.ContactDetail;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.service.TenantService;
import com.liyu.server.tables.pojos.Account;
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

@Api(value = "账户", description = "账户操作", tags = {"账户接口"})
@RestController
@Slf4j
@RequestMapping(value = "/api/accounts")
public class AccountController {
    @Resource
    private AccountService accountService;

    @Resource
    private TenantService tenantService;

    @Resource
    private OrganizationService organizationService;

    @ApiOperation(value = "获取账户列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        Integer total = accountService.count();
        List<Account> accounts = accountService.list((page - 1) * size, size);
        for (Account account : accounts) {
            account.setPassword(null);
            account.setSalt(null);
        }
        return APIResponse.withPagination(accounts, total, page, size);
    }

    @ApiOperation(value = "创建新账户", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "newAccount", value = "账户信息", required = true, dataType = "Account")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(
            @RequestBody Account newAccount) {
        Account account = accountService.create(newAccount);
        account.setSalt(null);
        account.setPassword(null);
        return APIResponse.success(account);
    }

    @ApiOperation(value = "更新账户", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "newAccount", value = "账户信息", required = true, dataType = "AccountDetail", paramType = "body")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse create(@PathVariable Long id,
                              @RequestBody Account newAccount) {
        Account account = accountService.update(ULong.valueOf(id), newAccount);
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

//    @ApiOperation(value = "获取账户详情", notes = "")
//    @ResponseBody
//    @ApiImplicitParam(name = "id", value = "账户ID", required = true, dataType = "Long", paramType = "path")
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public APIResponse detail(@PathVariable Long id) {
//        Account account = accountService.getById(ULong.valueOf(id));
//        account.setPassword(null);
//        account.setSalt(null);
//        AccountDetail accountDetail = new AccountDetail(account);
//        List<Organization> organizations = accountService.organizations(account.getAccountId());
//        ArrayList<String> organizationIds = new ArrayList<>();
//        ArrayList<OrganizationDetail> organizationDetails = new ArrayList<>();
//        for (Organization organization : organizations) {
//            organizationIds.add(organization.getOrganizationId());
//            OrganizationDetail organizationDetail = new OrganizationDetail(organization);
//            List<String> organizationRoute = organizationService.getOrganizationRoute(organization, organizations);
//            organizationDetail.setRoute(organizationRoute);
//            organizationDetails.add(organizationDetail);
//        }
//        accountDetail.setOrganizationIds(organizationIds);
//        accountDetail.setOrganizations(organizationDetails);
//        return APIResponse.success(accountDetail);
//    }
//
//    @ApiOperation(value = "更改状态", notes = "")
//    @ResponseBody
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
//            @ApiImplicitParam(name = "accountChangeStatusBody", value = "账户信息", required = true, dataType = "AccountChangeStatusBody", paramType = "body")
//    })
//    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
//    public APIResponse changeStatus(@PathVariable Long id,
//                                    @RequestBody ContactChangeStatusBody accountChangeStatusBody) {
//        Account account = accountService.changeStatus(ULong.valueOf(id), accountChangeStatusBody.getStatus());
//        return APIResponse.success(account);
//    }
}
