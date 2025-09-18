package com.emiyaoj.service.domain.dto.AuthDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色授权DTO
 */
@Data
class RoleAuthDTO {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 权限ID列表
     */
    @NotEmpty(message = "权限列表不能为空")
    private List<Long> permissionIds;
}


