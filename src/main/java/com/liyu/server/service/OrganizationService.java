package com.liyu.server.service;

import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.model.OrganizationTree;
import com.liyu.server.tables.pojos.Contact;
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
     * 根据租户ID获取组织列表
     *
     * @param tenantId 租户ID
     * @return List<Organization>
     */
    List<OrganizationDetail> listByTenantId(String tenantId);

    /**
     * 获取组织根节点
     *
     * @param tenantId 租户ID
     * @return Organization
     */
    OrganizationTree getRoot(String tenantId);

    /**
     * 获取组织路径
     *
     * @param organization  组织信息
     * @param organizations 组织列表
     * @return List<String>
     */
    List<String> getOrganizationRoute(Organization organization, List<Organization> organizations);

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
     * @param organizationId 组织ID
     */
    void delete(String organizationId);

    /**
     * 绑定帐号到组织
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void bindContact(String organizationId, String accountId);

    /**
     * 取消帐号绑定
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void unbindContact(String organizationId, String accountId);

    /**
     * 根据组织ID获取账户个数
     *
     * @param organizationId
     * @return
     */
    Integer contactsCount(String organizationId);

    /**
     * 根据组织ID获取账户列表
     *
     * @param organizationId
     * @param offset
     * @param size
     * @return
     */
    List<Contact> contacts(String organizationId, Integer offset, Integer size);


    /**
     * 获取所有所有子节点ID
     *
     * @param organizationId 组织ID
     * @return List<String>
     */
    List<String> getAllChildrenOrganizationIds(String organizationId);

    /**
     * 获取当前及其所有子节点的账户个数
     *
     * @param ids
     * @return
     */
    Integer contactDeepCount(List<String> ids);

    /**
     * 获取当前及其所有子节点的账户列表
     *
     * @param ids
     * @param offset
     * @param size
     * @return
     */
    List<Contact> contactsDeep(List<String> ids, Integer offset, Integer size);

    /**
     * 排序
     *
     * @param ids
     */
    void changeOrder(List<ULong> ids);

    List<Organization> listByContactId(String contactId);
}
