package com.emiyaoj.service.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色信息VO
 */
@Data
public class RoleVO {

    /**
     * 角色ID - 序列化为字符串，避免前端JavaScript精度丢失
     */
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 权限列表
     */
    private List<PermissionVO> permissions;
}
