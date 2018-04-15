package com.liyu.server.service;

import com.liyu.server.model.AccountCreateData;
import com.liyu.server.tables.pojos.Account;
import org.jooq.types.ULong;

import java.util.List;

public interface AccountService {

    /**
     * 获取所有帐号
     *
     * @return List<Account>
     */
    List<Account> list();

    /**
     * 根据username password获取用户
     * 用来验证用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Account
     */
    Account getByUsernameAndPassword(String username, String password);

    /**
     * 创建新账户
     *
     * @param createData 接收前端数据
     * @return Account
     */
    Account create(AccountCreateData createData);

    /**
     * 更新账户信息
     *
     * @param id         账户ID
     * @param updateData 账户数据
     * @return Account
     */
    Account update(ULong id, Account updateData);

    /**
     * 删除帐号
     *
     * @param id 账户ID
     */
    void delete(ULong id);
}
