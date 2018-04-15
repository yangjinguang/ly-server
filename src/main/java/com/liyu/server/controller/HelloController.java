package com.liyu.server.controller;

import com.liyu.server.service.AccountService;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@Api(value = "测试", description = "用于测测试", tags = {"测试接口"})
@RequestMapping(value = "/api")
public class HelloController {
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "哈哈", notes = "")
    @ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public APIResponse hello() {
        List<Account> accounts = accountService.list();
        for (Account account : accounts) {
            account.setPassword(null);
            account.setSalt(null);
        }
        return APIResponse.success(accounts);
    }

    @ApiOperation(value = "哦哦", notes = "world")
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public APIResponse world() {
        return APIResponse.success("world");
    }
}
