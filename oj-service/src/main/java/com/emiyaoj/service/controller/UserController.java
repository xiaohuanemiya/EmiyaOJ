package com.emiyaoj.service.controller;


import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.UserSaveDTO;
import com.emiyaoj.service.domain.vo.UserVO;
import com.emiyaoj.service.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<PageVO<UserVO>> getUserPage(@Valid @RequestBody PageDTO pageDTO) {
        PageVO<UserVO> page = userService.selectUserPage(pageDTO);
        return ResponseResult.success(page);
    }

    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.selectUserById(id);
        return ResponseResult.success(userVO);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER.ADD')")
    public ResponseResult<Void> saveUser(@RequestBody @Valid UserSaveDTO saveDTO) {
        boolean result = userService.saveUser(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("新增用户失败");
    }

    /**
     * 修改用户
     */
    @PutMapping
    @PreAuthorize("hasAuthority('USER.EDIT')")
    public ResponseResult<Void> updateUser(@RequestBody @Valid UserSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return ResponseResult.fail("用户ID不能为空");
        }
        boolean result = userService.updateUser(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("修改用户失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER.DELETE')")
    public ResponseResult<Void> deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        return result ? ResponseResult.success() : ResponseResult.fail("删除用户失败");
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('USER.DELETE')")
    public ResponseResult<Void> deleteUsers(@RequestBody List<Long> ids) {
        boolean result = userService.deleteUsers(ids);
        return result ? ResponseResult.success() : ResponseResult.fail("批量删除用户失败");
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('USER.RESET.PASSWORD')")
    public ResponseResult<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        boolean result = userService.resetPassword(id, newPassword);
        return result ? ResponseResult.success() : ResponseResult.fail("重置密码失败");
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('USER.EDIT')")
    public ResponseResult<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = userService.updateUserStatus(id, status);
        return result ? ResponseResult.success() : ResponseResult.fail("修改用户状态失败");
    }

    /**
     * 为用户分配角色
     */
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('USER.EDIT')")
    public ResponseResult<Void> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        boolean result = userService.assignRoles(id, roleIds);
        return result ? ResponseResult.success() : ResponseResult.fail("分配角色失败");
    }

    /**
     * 获取用户权限列表
     */
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<List<String>> getUserPermissions(@PathVariable Long id) {
        List<String> permissions = userService.getUserPermissions(id);
        return ResponseResult.success(permissions);
    }

    /**
     * 检查用户是否拥有指定权限
     */
    @GetMapping("/{id}/has-permission")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<Boolean> hasPermission(@PathVariable Long id, @RequestParam String permissionCode) {
        boolean hasPermission = userService.hasPermission(id, permissionCode);
        return ResponseResult.success(hasPermission);
    }

    /**
     * 检查用户是否拥有指定角色
     */
    @GetMapping("/{id}/has-role")
    @PreAuthorize("hasAuthority('USER.LIST')")
    public ResponseResult<Boolean> hasRole(@PathVariable Long id, @RequestParam String roleCode) {
        boolean hasRole = userService.hasRole(id, roleCode);
        return ResponseResult.success(hasRole);
    }
}
