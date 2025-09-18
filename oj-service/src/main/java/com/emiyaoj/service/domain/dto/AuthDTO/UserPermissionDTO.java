package com.emiyaoj.service.domain.dto.AuthDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户直接权限授权DTO
 */
@Data
class UserPermissionDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 权限ID
     */
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    /**
     * 权限类型：1-授予，0-拒绝
     */
    @NotNull(message = "权限类型不能为空")
    private Integer permissionType;
}