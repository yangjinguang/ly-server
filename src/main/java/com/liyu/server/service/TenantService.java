package com.liyu.server.service;

import com.liyu.server.tables.pojos.Tenant;
import org.jooq.types.ULong;

import java.util.List;

public interface TenantService {
    /**
     * 租户列表
     *
     * @return List<Tenant>
     */
    List<Tenant> list();

    /**
     * 创建新租户
     *
     * @param newTenant 租户信息
     * @return Tenant
     */
    Tenant create(Tenant newTenant);

    /**
     * 更新租户
     *
     * @param id        租户ID
     * @param newTenant 租户信息
     * @return Tenant
     */
    Tenant update(ULong id, Tenant newTenant);

    /**
     * 删除租户
     *
     * @param id 租户ID
     */
    void delete(ULong id);

    /**
     * 根据用户ID获取租户列表
     *
     * @param accountId 帐号ID
     * @return
     */
    List<Tenant> byAccountId(String accountId);

    /**
     * 绑定帐号
     *
     * @param tenantId  租户ID
     * @param accountId 帐号ID
     */
    void bindAccount(String tenantId, String accountId);

    List<Tenant> byAccountUsername(String username);
}
