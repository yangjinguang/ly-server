package com.liyu.server.service;

import com.liyu.server.tables.pojos.Role;
import org.jooq.types.ULong;

import java.util.List;

public interface RoleService {
    /**
     * 角色列表
     *
     * @return List<Role>
     */
    List<Role> list();

    /**
     * 创建角色
     *
     * @param newRole 角色详情
     * @return Role
     */
    Role create(Role newRole);

    /**
     * 更新角色
     *
     * @param id      角色ID
     * @param newRole 角色详情
     * @return Role
     */
    Role update(ULong id, Role newRole);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void delete(ULong id);
}
