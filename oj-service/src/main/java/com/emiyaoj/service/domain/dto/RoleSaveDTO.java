package com.emiyaoj.service.domain.dto;

import lombok.Data;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * 角色新增/修改DTO
 */
@Data
public class RoleSaveDTO {

    /**
     * 角色ID（修改时必传）
     */
    private Long id;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;

    /**
     * 角色描述
     */
    @Size(max = 255, message = "角色描述长度不能超过255个字符")
    private String description;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 权限ID列表
     */
    private List<Long> permissionIds;
}
