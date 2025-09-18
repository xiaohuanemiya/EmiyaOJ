package com.emiyaoj.service.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.RoleQueryDTO;
import com.emiyaoj.service.domain.dto.RoleSaveDTO;
import com.emiyaoj.service.domain.vo.RoleVO;
import com.emiyaoj.service.service.IRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<Page<RoleVO>> getRolePage(@Valid RoleQueryDTO queryDTO) {
        Page<RoleVO> page = roleService.selectRolePage(queryDTO);
        return ResponseResult.success(page);
    }

    /**
     * 查询所有角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<List<RoleVO>> getAllRoles() {
        List<RoleVO> roles = roleService.selectAllRoles();
        return ResponseResult.success(roles);
    }

    /**
     * 根据ID查询角色信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<RoleVO> getRoleById(@PathVariable Long id) {
        RoleVO roleVO = roleService.selectRoleById(id);
        return ResponseResult.success(roleVO);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADD')")
    public ResponseResult<Void> saveRole(@RequestBody @Valid RoleSaveDTO saveDTO) {
        boolean result = roleService.saveRole(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("新增角色失败");
    }

    /**
     * 修改角色
     */
    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    public ResponseResult<Void> updateRole(@RequestBody @Valid RoleSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return ResponseResult.fail("角色ID不能为空");
        }
        boolean result = roleService.updateRole(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("修改角色失败");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        boolean result = roleService.deleteRole(id);
        return result ? ResponseResult.success() : ResponseResult.fail("删除角色失败");
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseResult<Void> deleteRoles(@RequestBody List<Long> ids) {
        boolean result = roleService.deleteRoles(ids);
        return result ? ResponseResult.success() : ResponseResult.fail("批量删除角色失败");
    }

    /**
     * 修改角色状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    public ResponseResult<Void> updateRoleStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = roleService.updateRoleStatus(id, status);
        return result ? ResponseResult.success() : ResponseResult.fail("修改角色状态失败");
    }

    /**
     * 为角色分配权限
     */
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN')")
    public ResponseResult<Void> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        boolean result = roleService.assignPermissions(id, permissionIds);
        return result ? ResponseResult.success() : ResponseResult.fail("分配权限失败");
    }

    /**
     * 获取角色权限列表
     */
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<List<String>> getRolePermissions(@PathVariable Long id) {
        List<String> permissions = roleService.getRolePermissions(id);
        return ResponseResult.success(permissions);
    }

    /**
     * 检查角色编码是否存在
     */
    @GetMapping("/exists")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<Boolean> existsRoleCode(@RequestParam String roleCode,
                                                  @RequestParam(required = false) Long excludeId) {
        boolean exists = roleService.existsRoleCode(roleCode, excludeId);
        return ResponseResult.success(exists);
    }

    /**
     * 根据用户ID查询角色列表
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_LIST')")
    public ResponseResult<List<RoleVO>> getRolesByUserId(@PathVariable Long userId) {
        List<RoleVO> roles = roleService.selectRolesByUserId(userId);
        return ResponseResult.success(roles);
    }
}
