package com.liyu.server.service;

import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.util.List;

public interface OrganizationService {
    /**
     * 获取组织列表
     *
     * @return List<Organization>
     */
    List<Organization> list();

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
}
