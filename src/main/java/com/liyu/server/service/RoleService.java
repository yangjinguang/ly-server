package com.liyu.server.service;

import com.liyu.server.tables.pojos.Account;
import com.liyu.server.tables.pojos.Role;
import org.jooq.types.ULong;

import java.util.List;

public interface RoleService {
    /**
     * count
     *
     * @return Integer
     */
    Integer count();

    /**
     * 角色列表
     *
     * @return List<Role>
     */
    List<Role> list(Integer offset, Integer size);

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

    /**
     * 停用禁用角色
     *
     * @param id
     * @param enabled
     * @return
     */
    Role enabledOrDisabled(ULong id, Boolean enabled);

    /**
     * 角色详细信息
     *
     * @param id
     * @return
     */
    Role detail(ULong id);

    /**
     * 角色绑定的账户个数
     *
     * @param roleId
     * @return
     */
    Integer membersCount(String roleId);

    /**
     * 角色绑定的账户列表
     *
     * @param roleId
     * @param offset
     * @param size
     * @return
     */
    List<Account> members(String roleId, Integer offset, Integer size);

    /**
     * 绑定用户
     *
     * @param roleId
     * @param accountIds
     */
    void bindMembers(String roleId, List<String> accountIds);

    /**
     * 解绑用户
     *
     * @param roleId
     * @param accountIds
     */
    void unBindMembers(String roleId, List<String> accountIds);
}
