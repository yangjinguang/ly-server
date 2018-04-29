package com.liyu.server.service;

import com.liyu.server.model.OrganizationTree;
import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.util.List;

public interface OrganizationService {

    /**
     * 根据ID获取组织详情
     *
     * @param id ID
     * @return Organization
     */
    Organization byId(ULong id);

    /**
     * 获取组织列表
     *
     * @return List<Organization>
     */
    List<Organization> list();

    /**
     * 根据租户ID获取组织列表
     *
     * @param tenantId 租户ID
     * @return List<Organization>
     */
    List<Organization> listByTenantId(String tenantId);

    /**
     * 获取组织根节点
     *
     * @param tenantId 租户ID
     * @return Organization
     */
    OrganizationTree getRoot(String tenantId);

    /**
     * 根据parent Id获取组织列表
     *
     * @param parentId parent Id
     * @return List<Organization>
     */
    List<OrganizationTree> listByParentId(String parentId);

    /**
     * 根据parent Id获取组织个数
     *
     * @param parentId parent Id
     * @return int
     */
    int countByParentId(String parentId);

    /**
     * 获取组织树
     *
     * @param tenantId 租户ID
     * @return
     */
    OrganizationTree tree(String tenantId);

    /**
     * 创建新组织
     *
     * @param newOrganization 组织信息
     * @return Organization
     */
    Organization create(Organization newOrganization);

    /**
     * 更新组织
     *
     * @param id              组织ID
     * @param newOrganization 组织信息
     * @return Organization
     */
    Organization update(ULong id, Organization newOrganization);

    /**
     * 删除组织
     *
     * @param id 组织ID
     */
    void delete(ULong id);

    /**
     * 绑定帐号到组织
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void bindAccount(String organizationId, String accountId);

    /**
     * 取消帐号绑定
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void unbindAccount(String organizationId, String accountId);

    /**
     * 根据组织ID获取账户列表
     *
     * @param organizationId 组织ID
     * @return List<Account>
     */
    List<Account> accounts(String organizationId);

    /**
     * 获取所有所有子节点ID
     *
     * @param organizationId 组织ID
     * @return List<String>
     */
    List<String> getAllChildrenOrganizationIds(String organizationId);

    /**
     * 获取当前及其所有子节点的账户列表
     *
     * @param organizationId 组织ID
     * @return List<Account>
     */
    List<Account> accountsDeep(String organizationId);
}
