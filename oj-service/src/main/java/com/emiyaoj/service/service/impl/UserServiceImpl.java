package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.dto.UserQueryDTO;
import com.emiyaoj.service.domain.dto.UserSaveDTO;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.domain.vo.UserVO;
import com.emiyaoj.service.mapper.*;
import com.emiyaoj.service.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserPermissionMapper userPermissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Page<UserVO> selectUserPage(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
               .like(StringUtils.hasText(queryDTO.getNickname()), User::getNickname, queryDTO.getNickname())
               .like(StringUtils.hasText(queryDTO.getEmail()), User::getEmail, queryDTO.getEmail())
               .like(StringUtils.hasText(queryDTO.getPhone()), User::getPhone, queryDTO.getPhone())
               .eq(queryDTO.getStatus() != null, User::getStatus, queryDTO.getStatus())
               .eq(User::getDeleted, 0)
               .orderByDesc(User::getCreateTime);

        Page<User> userPage = this.page(page, wrapper);

        // 转换为VO并设置角色信息
        List<UserVO> userVOList = userPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());

        Page<UserVO> result = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        result.setRecords(userVOList);

        return result;
    }

    @Override
    public UserVO selectUserById(Long id) {
        User user = this.getById(id);
        if (user == null || user.getDeleted() == 1) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    public User selectUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getDeleted, 0);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserSaveDTO saveDTO) {
        // 检查用户名是否已存在
        if (selectUserByUsername(saveDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(saveDTO, user);
        user.setPassword(passwordEncoder.encode(saveDTO.getPassword()));
        user.setStatus(saveDTO.getStatus() != null ? saveDTO.getStatus() : 1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        boolean result = this.save(user);

        // 分配角色
        if (result && !CollectionUtils.isEmpty(saveDTO.getRoleIds())) {
            assignRoles(user.getId(), saveDTO.getRoleIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserSaveDTO saveDTO) {
        User existUser = this.getById(saveDTO.getId());
        if (existUser == null || existUser.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否已被其他用户使用
        User userWithSameUsername = selectUserByUsername(saveDTO.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(saveDTO.getId())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(saveDTO, user);

        // 如果有新密码，则加密
        if (StringUtils.hasText(saveDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(saveDTO.getPassword()));
        } else {
            user.setPassword(null); // 不更新密码字段
        }

        user.setUpdateTime(LocalDateTime.now());

        boolean result = this.updateById(user);

        // 重新分配角色
        if (result && saveDTO.getRoleIds() != null) {
            assignRoles(saveDTO.getId(), saveDTO.getRoleIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());

        // 同时删除用户角色关联
        userRoleMapper.deleteByUserId(id);
        userPermissionMapper.deleteByUserId(id);

        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(List<Long> ids) {
        for (Long id : ids) {
            deleteUser(id);
        }
        return true;
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 先删除原有的角色关联
        userRoleMapper.deleteByUserId(userId);

        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UserRole> userRoles = roleIds.stream().map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(LocalDateTime.now());
                return userRole;
            }).collect(Collectors.toList());

            userRoleMapper.batchInsert(userRoles);
        }

        return true;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        // 获取用户角色权限
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        List<Long> rolePermissionIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            rolePermissionIds = rolePermissionMapper.selectPermissionIdsByRoleIds(roleIds);
        }

        // 获取用户直接授予的权限
        List<Long> grantedPermissionIds = userPermissionMapper.selectGrantedPermissionIdsByUserId(userId);

        // 获取用户直接拒绝的权限
        List<Long> deniedPermissionIds = userPermissionMapper.selectDeniedPermissionIdsByUserId(userId);

        // 合并权限ID（去除被拒绝的权限）
        rolePermissionIds.addAll(grantedPermissionIds);
        rolePermissionIds.removeAll(deniedPermissionIds);

        if (CollectionUtils.isEmpty(rolePermissionIds)) {
            return new ArrayList<>();
        }

        // 查询权限编码
        List<Permission> permissions = permissionMapper.selectList(
            new LambdaQueryWrapper<Permission>().in(Permission::getId, rolePermissionIds)
        );
        return permissions.stream()
                .filter(p -> p.getStatus() == 1)
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }

    @Override
    public boolean hasRole(Long userId, String roleCode) {
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }

        List<Role> roles = roleMapper.selectList(
            new LambdaQueryWrapper<Role>().in(Role::getId, roleIds)
        );
        return roles.stream()
                .filter(r -> r.getStatus() == 1)
                .anyMatch(r -> r.getRoleCode().equals(roleCode));
    }

    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 设置状态描述
        userVO.setStatusDesc(user.getStatus() == 1 ? "启用" : "禁用");

        return userVO;
    }
}
