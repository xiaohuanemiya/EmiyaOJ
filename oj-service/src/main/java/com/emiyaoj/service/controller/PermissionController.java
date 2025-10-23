package com.emiyaoj.service.controller;


import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.PermissionQueryDTO;
import com.emiyaoj.service.domain.dto.PermissionSaveDTO;
import com.emiyaoj.service.domain.vo.PermissionVO;
import com.emiyaoj.service.service.IPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-09-17
 */
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    /**
     * 查询权限列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<List<PermissionVO>> getPermissionList(@Valid PermissionQueryDTO queryDTO) {
        List<PermissionVO> permissions = permissionService.selectPermissionList(queryDTO);
        return ResponseResult.success(permissions);
    }

    /**
     * 查询权限树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<List<PermissionVO>> getPermissionTree(@Valid PermissionQueryDTO queryDTO) {
        List<PermissionVO> tree = permissionService.selectPermissionTree(queryDTO);
        return ResponseResult.success(tree);
    }

    /**
     * 根据ID查询权限信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<PermissionVO> getPermissionById(@PathVariable Long id) {
        PermissionVO permissionVO = permissionService.selectPermissionById(id);
        return ResponseResult.success(permissionVO);
    }

    /**
     * 新增权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_ADD')")
    public ResponseResult<Void> savePermission(@RequestBody @Valid PermissionSaveDTO saveDTO) {
        boolean result = permissionService.savePermission(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("新增权限失败");
    }

    /**
     * 修改权限
     */
    @PutMapping
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    public ResponseResult<Void> updatePermission(@RequestBody @Valid PermissionSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return ResponseResult.fail("权限ID不能为空");
        }
        boolean result = permissionService.updatePermission(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("修改权限失败");
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseResult<Void> deletePermission(@PathVariable Long id) {
        boolean result = permissionService.deletePermission(id);
        return result ? ResponseResult.success() : ResponseResult.fail("删除权限失败");
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseResult<Void> deletePermissions(@RequestBody List<Long> ids) {
        boolean result = permissionService.deletePermissions(ids);
        return result ? ResponseResult.success() : ResponseResult.fail("批量删除权限失败");
    }

    /**
     * 修改权限状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    public ResponseResult<Void> updatePermissionStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = permissionService.updatePermissionStatus(id, status);
        return result ? ResponseResult.success() : ResponseResult.fail("修改权限状态失败");
    }

    /**
     * 检查权限编码是否存在
     */
    @GetMapping("/exists")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<Boolean> existsPermissionCode(@RequestParam String permissionCode,
                                                        @RequestParam(required = false) Long excludeId) {
        boolean exists = permissionService.existsPermissionCode(permissionCode, excludeId);
        return ResponseResult.success(exists);
    }

    /**
     * 根据角色ID查询权限列表
     */
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<List<PermissionVO>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<PermissionVO> permissions = permissionService.selectPermissionsByRoleId(roleId);
        return ResponseResult.success(permissions);
    }

    /**
     * 根据用户ID查询权限列表
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseResult<List<PermissionVO>> getPermissionsByUserId(@PathVariable Long userId) {
        List<PermissionVO> permissions = permissionService.selectPermissionsByUserId(userId);
        return ResponseResult.success(permissions);
    }

    /**
     * 获取用户菜单权限树
     */
    @GetMapping("/menu/{userId}")
    public ResponseResult<List<PermissionVO>> getMenuTree(@PathVariable Long userId) {
        List<PermissionVO> menuTree = permissionService.getMenuTree(userId);
        return ResponseResult.success(menuTree);
    }

    /**
     * 获取用户按钮权限列表
     */
    @GetMapping("/button/{userId}")
    public ResponseResult<List<String>> getButtonPermissions(@PathVariable Long userId) {
        List<String> buttonPermissions = permissionService.getButtonPermissions(userId);
        return ResponseResult.success(buttonPermissions);
    }
}
