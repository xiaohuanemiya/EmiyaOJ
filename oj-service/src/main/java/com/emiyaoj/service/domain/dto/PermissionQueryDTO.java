package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * 权限查询DTO
 */
@Data
public class PermissionQueryDTO {

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer permissionType;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 父权限ID
     */
    private Long parentId;
}
