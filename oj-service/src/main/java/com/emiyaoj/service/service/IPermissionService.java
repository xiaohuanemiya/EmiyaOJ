package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.PermissionQueryDTO;
import com.emiyaoj.service.domain.dto.PermissionSaveDTO;
import com.emiyaoj.service.domain.pojo.Permission;
import com.emiyaoj.service.domain.vo.PermissionVO;

import java.util.List;

/**
 * <p>
 * 权限服务接口
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 查询权限列表
     */
    List<PermissionVO> selectPermissionList(PermissionQueryDTO queryDTO);

    /**
     * 查询权限树
     */
    List<PermissionVO> selectPermissionTree(PermissionQueryDTO queryDTO);

    /**
     * 根据ID查询权限信息
     */
    PermissionVO selectPermissionById(Long id);

    /**
     * 新增权限
     */
    boolean savePermission(PermissionSaveDTO saveDTO);

    /**
     * 修改权限
     */
    boolean updatePermission(PermissionSaveDTO saveDTO);

    /**
     * 删除权限
     */
    boolean deletePermission(Long id);

    /**
     * 批量删除权限
     */
    boolean deletePermissions(List<Long> ids);

    /**
     * 修改权限状态
     */
    boolean updatePermissionStatus(Long id, Integer status);

    /**
     * 检查权限编码是否已存在
     */
    boolean existsPermissionCode(String permissionCode, Long excludeId);

    /**
     * 根据角色ID查询权限列表
     */
    List<PermissionVO> selectPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限列表（包括角色权限和直接权限）
     */
    List<PermissionVO> selectPermissionsByUserId(Long userId);

    /**
     * 构建权限树
     */
    List<PermissionVO> buildPermissionTree(List<PermissionVO> permissions);

    /**
     * 获取菜单权限树（用于前端路由）
     */
    List<PermissionVO> getMenuTree(Long userId);

    /**
     * 获取按钮权限列表
     */
    List<String> getButtonPermissions(Long userId);
}
