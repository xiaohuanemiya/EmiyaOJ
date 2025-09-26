package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.utils.Permissions;
import com.emiyaoj.service.domain.dto.RoleQueryDTO;
import com.emiyaoj.service.domain.dto.RoleSaveDTO;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.domain.vo.RoleVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色服务实现类
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public Page<RoleVO> selectRolePage(RoleQueryDTO queryDTO) {
        Page<Role> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getRoleCode()), Role::getRoleCode, queryDTO.getRoleCode())
               .like(StringUtils.hasText(queryDTO.getRoleName()), Role::getRoleName, queryDTO.getRoleName())
               .eq(queryDTO.getStatus() != null, Role::getStatus, queryDTO.getStatus())
               .eq(Role::getDeleted, 0)
               .orderByDesc(Role::getCreateTime);

        Page<Role> rolePage = this.page(page, wrapper);

        List<RoleVO> roleVOList = rolePage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());

        Page<RoleVO> result = new Page<>();
        BeanUtils.copyProperties(rolePage, result);
        result.setRecords(roleVOList);

        return result;
    }

    @Override
    public RoleVO selectRoleById(Long id) {
        Role role = this.getById(id);
        if (role == null || role.getDeleted() == 1) {
            return null;
        }
        return convertToVO(role);
    }

    @Override
    public List<RoleVO> selectAllRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1)
               .eq(Role::getDeleted, 0)
               .orderBy(true, true, Role::getCreateTime);

        List<Role> roles = this.list(wrapper);
        return roles.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleSaveDTO saveDTO) {
        // 检查角色编码是否已存在
        if (existsRoleCode(saveDTO.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }

        Role role = new Role();
        BeanUtils.copyProperties(saveDTO, role);
        role.setStatus(saveDTO.getStatus() != null ? saveDTO.getStatus() : 1);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());

        boolean result = this.save(role);

        // 分配权限
        if (result && !CollectionUtils.isEmpty(saveDTO.getPermissionIds())) {
            assignPermissions(role.getId(), saveDTO.getPermissionIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleSaveDTO saveDTO) {
        Role existRole = this.getById(saveDTO.getId());
        if (existRole == null || existRole.getDeleted() == 1) {
            throw new RuntimeException("角色不存在");
        }

        // 检查角色编码是否已被其他角色使用
        if (existsRoleCode(saveDTO.getRoleCode(), saveDTO.getId())) {
            throw new RuntimeException("角色编码已存在");
        }

        Role role = new Role();
        BeanUtils.copyProperties(saveDTO, role);
        role.setUpdateTime(LocalDateTime.now());

        boolean result = this.updateById(role);

        // 重新分配权限
        if (result && saveDTO.getPermissionIds() != null) {
            assignPermissions(saveDTO.getId(), saveDTO.getPermissionIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        // 检查是否有用户绑定了这个角色
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(id);
        if (!CollectionUtils.isEmpty(userIds)) {
            throw new RuntimeException("该角色下还有用户，无法删除");
        }

        Role role = new Role();
        role.setId(id);
        role.setDeleted(1);
        role.setUpdateTime(LocalDateTime.now());

        // 同时删除角色权限关联
        rolePermissionMapper.deleteByRoleId(id);

        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(List<Long> ids) {
        for (Long id : ids) {
            deleteRole(id);
        }
        return true;
    }

    @Override
    public boolean updateRoleStatus(Long id, Integer status) {
        Role role = new Role();
        role.setId(id);
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除原有的权限关联
        rolePermissionMapper.deleteByRoleId(roleId);

        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<RolePermission> rolePermissions = permissionIds.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermission.setCreateTime(LocalDateTime.now());
                return rolePermission;
            }).collect(Collectors.toList());

            rolePermissionMapper.batchInsert(rolePermissions);
        }

        return true;
    }

    @Override
    public List<String> getRolePermissions(Long roleId) {
        List<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleId(roleId);

        if (CollectionUtils.isEmpty(permissionIds)) {
            return List.of();
        }
        
        List<Permission> permissions = permissionMapper.selectByIds(permissionIds);
        int code = 0;
        for (Permission permission : permissions) {
            code = code | permission.getPermissionCode();
        }
        
//        return permissions.stream()
//                .filter(p -> p.getStatus() == 1)
//                .map(Permission::getPermissionCode)
//                .collect(Collectors.toList());
        return Permissions.parsePermissions(code);
    }

    @Override
    public boolean existsRoleCode(String roleCode, Long excludeId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleCode)
               .eq(Role::getDeleted, 0);

        if (excludeId != null) {
            wrapper.ne(Role::getId, excludeId);
        }

        return this.count(wrapper) > 0;
    }

    @Override
    public List<RoleVO> selectRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);

        if (CollectionUtils.isEmpty(roleIds)) {
            return List.of();
        }

        List<Role> roles = this.listByIds(roleIds);
        return roles.stream()
                .filter(r -> r.getStatus() == 1 && r.getDeleted() == 0)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private RoleVO convertToVO(Role role) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);

        // 设置状态描述
        roleVO.setStatusDesc(role.getStatus() == 1 ? "启用" : "禁用");

        return roleVO;
    }
}
