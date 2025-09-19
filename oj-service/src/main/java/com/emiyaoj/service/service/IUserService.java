package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.service.domain.dto.UserLoginDTO;
import com.emiyaoj.service.domain.dto.UserQueryDTO;
import com.emiyaoj.service.domain.dto.UserSaveDTO;
import com.emiyaoj.service.domain.vo.UserLoginVO;
import com.emiyaoj.service.domain.vo.UserVO;
import com.emiyaoj.service.domain.pojo.User;

import java.util.List;

/**
 * <p>
 * 用户服务接口
 * </p>
 *
 * @author EmiyaOJ
 * @since 2025-09-17
 */
public interface IUserService {

    /**
     * 分页查询用户列表
     */
    Page<UserVO> selectUserPage(UserQueryDTO queryDTO);

    /**
     * 根据ID查询用户信息
     */
    UserVO selectUserById(Long id);

    /**
     * 根据用户名查询用户信息
     */
    User selectUserByUsername(String username);

    /**
     * 新增用户
     */
    boolean saveUser(UserSaveDTO saveDTO);

    /**
     * 修改用户
     */
    boolean updateUser(UserSaveDTO saveDTO);

    /**
     * 删除用户
     */
    boolean deleteUser(Long id);

    /**
     * 批量删除用户
     */
    boolean deleteUsers(List<Long> ids);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id, String newPassword);

    /**
     * 修改用户状态
     */
    boolean updateUserStatus(Long id, Integer status);

    /**
     * 为用户分配角色
     */
    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 获取用户的所有权限（包括角色权限和直接权限）
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 检查用户是否拥有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 检查用户是否拥有指定角色
     */
    boolean hasRole(Long userId, String roleCode);

    UserLoginVO login(UserLoginDTO loginDTO);
}
