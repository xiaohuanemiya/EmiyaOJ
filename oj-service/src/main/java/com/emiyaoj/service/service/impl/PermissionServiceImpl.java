package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.constant.PermissionTypeEnum;
import com.emiyaoj.service.domain.dto.PermissionQueryDTO;
import com.emiyaoj.service.domain.dto.PermissionSaveDTO;
import com.emiyaoj.service.domain.pojo.Permission;
import com.emiyaoj.service.domain.vo.PermissionVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限服务实现类
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<PermissionVO> selectPermissionList(PermissionQueryDTO queryDTO) {
        LambdaQueryWrapper<Permission> wrapper = buildQueryWrapper(queryDTO);
        List<Permission> permissions = this.list(wrapper);
        return permissions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<PermissionVO> selectPermissionTree(PermissionQueryDTO queryDTO) {
        List<PermissionVO> permissionList = selectPermissionList(queryDTO);
        return buildPermissionTree(permissionList);
    }

    @Override
    public PermissionVO selectPermissionById(Long id) {
        Permission permission = this.getById(id);
        if (permission == null || permission.getDeleted() == 1) {
            return null;
        }
        return convertToVO(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermission(PermissionSaveDTO saveDTO) {
        // 检查权限编码是否已存在
        if (existsPermissionCode(saveDTO.getPermissionCode(), null)) {
            throw new RuntimeException("权限编码已存在");
        }

        Permission permission = new Permission();
        BeanUtils.copyProperties(saveDTO, permission);
        if (saveDTO.getPermissionType()== 1){
            permission.setPermissionType(PermissionTypeEnum.MENU);
        } else if (saveDTO.getPermissionType()== 2){
            permission.setPermissionType(PermissionTypeEnum.BUTTON);
        } else if (saveDTO.getPermissionType()== 3){
            permission.setPermissionType(PermissionTypeEnum.LINK);
        }
        permission.setParentId(saveDTO.getParentId() != null ? saveDTO.getParentId() : -1L);
        permission.setStatus(saveDTO.getStatus() != null ? saveDTO.getStatus() : 1);
        permission.setSortOrder(saveDTO.getSortOrder() != null ? saveDTO.getSortOrder() : 0);
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());

        return this.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermission(PermissionSaveDTO saveDTO) {
        Permission existPermission = this.getById(saveDTO.getId());
        if (existPermission == null || existPermission.getDeleted() == 1) {
            throw new RuntimeException("权限不存在");
        }

        // 检查权限编码是否已被其他权限使用
        if (existsPermissionCode(saveDTO.getPermissionCode(), saveDTO.getId())) {
            throw new RuntimeException("权限编码已存在");
        }

        Permission permission = new Permission();
        BeanUtils.copyProperties(saveDTO, permission);
        permission.setUpdateTime(LocalDateTime.now());

        return this.updateById(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long id) {
        // 检查是否有子权限
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getParentId, id)
               .eq(Permission::getDeleted, 0);
        long childCount = this.count(wrapper);
        if (childCount > 0) {
            throw new RuntimeException("该权限下还有子权限，无法删除");
        }

        // 检查是否有角色绑定了这个权限
        List<Long> roleIds = rolePermissionMapper.selectRoleIdsByPermissionId(id);
        if (!CollectionUtils.isEmpty(roleIds)) {
            throw new RuntimeException("该权限已被角色使用，无法删除");
        }

        Permission permission = new Permission();
        permission.setId(id);
        permission.setDeleted(1);
        permission.setUpdateTime(LocalDateTime.now());

        return this.updateById(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermissions(List<Long> ids) {
        for (Long id : ids) {
            deletePermission(id);
        }
        return true;
    }

    @Override
    public boolean updatePermissionStatus(Long id, Integer status) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setStatus(status);
        permission.setUpdateTime(LocalDateTime.now());
        return this.updateById(permission);
    }

    @Override
    public boolean existsPermissionCode(String permissionCode, Long excludeId) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPermissionCode, permissionCode)
               .eq(Permission::getDeleted, 0);

        if (excludeId != null) {
            wrapper.ne(Permission::getId, excludeId);
        }

        return this.count(wrapper) > 0;
    }

    @Override
    public List<PermissionVO> selectPermissionsByRoleId(Long roleId) {
        List<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleId(roleId);

        if (CollectionUtils.isEmpty(permissionIds)) {
            return List.of();
        }

        List<Permission> permissions = this.listByIds(permissionIds);
        return permissions.stream()
                .filter(p -> p.getStatus() == 1 && p.getDeleted() == 0)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionVO> selectPermissionsByUserId(Long userId) {
        // 获取用户角色权限
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        Set<Long> permissionIds = new HashSet<>();

        if (!CollectionUtils.isEmpty(roleIds)) {
            List<Long> rolePermissionIds = rolePermissionMapper.selectPermissionIdsByRoleIds(roleIds);
            permissionIds.addAll(rolePermissionIds);
        }

        if (permissionIds.isEmpty()) {
            return List.of();
        }

        List<Permission> permissions = this.listByIds(permissionIds);
        return permissions.stream()
                .filter(p -> p.getStatus() == 1 && p.getDeleted() == 0)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionVO> buildPermissionTree(List<PermissionVO> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return List.of();
        }

        // 创建权限映射
        Map<Long, PermissionVO> permissionMap = permissions.stream()
                .collect(Collectors.toMap(PermissionVO::getId, p -> p));

        List<PermissionVO> rootPermissions = new ArrayList<>();

        for (PermissionVO permission : permissions) {
            if (permission.getParentId() == null || permission.getParentId() == -1) {
                rootPermissions.add(permission);
            } else {
                PermissionVO parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }

        // 对每个节点的子节点进行排序
        sortPermissionTree(rootPermissions);

        return rootPermissions;
    }



    private LambdaQueryWrapper<Permission> buildQueryWrapper(PermissionQueryDTO queryDTO) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getPermissionCode()), Permission::getPermissionCode, queryDTO.getPermissionCode())
               .like(StringUtils.hasText(queryDTO.getPermissionName()), Permission::getPermissionName, queryDTO.getPermissionName())
               .eq(queryDTO.getPermissionType() != null, Permission::getPermissionType, queryDTO.getPermissionType())
               .eq(queryDTO.getStatus() != null, Permission::getStatus, queryDTO.getStatus())
               .eq(queryDTO.getParentId() != null, Permission::getParentId, queryDTO.getParentId())
               .eq(Permission::getDeleted, 0)
               .orderByAsc(Permission::getSortOrder)
               .orderByDesc(Permission::getCreateTime);
        return wrapper;
    }

    private void sortPermissionTree(List<PermissionVO> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }

        permissions.sort(Comparator.comparing(PermissionVO::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder())));

        for (PermissionVO permission : permissions) {
            if (!CollectionUtils.isEmpty(permission.getChildren())) {
                sortPermissionTree(permission.getChildren());
            }
        }
    }

    private PermissionVO convertToVO(Permission permission) {
        PermissionVO permissionVO = new PermissionVO();
        BeanUtils.copyProperties(permission, permissionVO);

        // 设置权限类型描述
        switch (permission.getPermissionType()) {
            case PermissionTypeEnum.MENU -> {
                permissionVO.setPermissionTypeDesc("菜单");
                permissionVO.setPermissionType(1);
            }
            case PermissionTypeEnum.BUTTON -> {
                permissionVO.setPermissionTypeDesc("按钮");
                permissionVO.setPermissionType(2);
            }
            case PermissionTypeEnum.LINK -> {
                permissionVO.setPermissionTypeDesc("接口");
                permissionVO.setPermissionType(3);
            }
            default -> permissionVO.setPermissionTypeDesc("未知");
        }

        // 设置状态描述
        permissionVO.setStatusDesc(permission.getStatus() == 1 ? "启用" : "禁用");

        return permissionVO;
    }
}
