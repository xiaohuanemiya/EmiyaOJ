package com.emiyaoj.service.domain.dto.AuthDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户授权DTO
 */
@Data
public class UserAuthDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色列表不能为空")
    private List<Long> roleIds;
}

