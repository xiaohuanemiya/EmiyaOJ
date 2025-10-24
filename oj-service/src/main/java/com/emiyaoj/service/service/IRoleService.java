package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.RoleQueryDTO;
import com.emiyaoj.service.domain.dto.RoleSaveDTO;
import com.emiyaoj.service.domain.pojo.Role;
import com.emiyaoj.service.domain.vo.RoleVO;

import java.util.List;

/**
 * <p>
 * 角色服务接口
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
public interface IRoleService extends IService<Role> {

    /**
     * 分页查询角色列表
     */
    Page<RoleVO> selectRolePage(RoleQueryDTO queryDTO);

    /**
     * 根据ID查询角色信息
     */
    RoleVO selectRoleById(Long id);

    /**
     * 查询所有角色列表
     */
    List<RoleVO> selectAllRoles();

    /**
     * 新增角色
     */
    boolean saveRole(RoleSaveDTO saveDTO);

    /**
     * 修改角色
     */
    boolean updateRole(RoleSaveDTO saveDTO);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);

    /**
     * 批量删除角色
     */
    boolean deleteRoles(List<Long> ids);

    /**
     * 修改角色状态
     */
    boolean updateRoleStatus(Long id, Integer status);

    /**
     * 为角色分配权限
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色的权限列表
     */
    List<Long> getRolePermissionIds(Long roleId);

    /**
     * 检查角色编码是否已存在
     */
    boolean existsRoleCode(String roleCode, Long excludeId);

    /**
     * 根据用户ID查询角色列表
     */
    List<RoleVO> selectRolesByUserId(Long userId);
}
