package com.liyu.server.service;

import com.liyu.server.enums.AccountStatusEnum;
import com.liyu.server.model.AccountDetail;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
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
     * 根据租户ID获取账户列表
     *
     * @param tenantId
     * @param offset
     * @param size
     * @return
     */
    List<Account> listByTenantId(String tenantId, Integer offset, Integer size);

    /**
     * 根据租户ID获取账户个数
     *
     * @param tenantId
     * @return
     */
    Integer countByTenantId(String tenantId);

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
     * 根据username获取用户
     *
     * @param username username
     * @return Account
     */
    Account getByUsername(String username);

    /**
     * 根据id获取账户
     *
     * @param id id
     * @return Account
     */
    Account getById(ULong id);

    /**
     * 创建新账户
     *
     * @param newAccount 账户详情
     * @return Account
     */
    Account create(AccountDetail newAccount);

    /**
     * 更新账户信息
     *
     * @param id         账户ID
     * @param newAccount 账户数据
     * @return Account
     */
    Account update(ULong id, AccountDetail newAccount);

    /**
     * 删除帐号
     *
     * @param id 账户ID
     */
    void delete(ULong id);

    /**
     * 绑定账户到组织
     *
     * @param accountId      账户ID
     * @param organizationId 组织ID
     */
    void bindOrganization(String accountId, String organizationId);

    /**
     * 批量绑定账户到组织
     *
     * @param accountId       账户ID
     * @param organizationIds 组织ID列表
     */
    void bindOrganizations(String accountId, List<String> organizationIds);

    /**
     * 删除账户所有绑定部门
     *
     * @param accountId 账户ID
     */
    void unbindOrganizationsAll(String accountId);

    /**
     * 获取用户关联的组织列表
     *
     * @param accountId 账户ID
     * @return List<Organization>
     */
    List<Organization> organizations(String accountId);

    /**
     * 更改账户状态
     *
     * @param id     ID
     * @param status 状态
     * @return Account
     */
    Account changeStatus(ULong id, AccountStatusEnum status);
}
