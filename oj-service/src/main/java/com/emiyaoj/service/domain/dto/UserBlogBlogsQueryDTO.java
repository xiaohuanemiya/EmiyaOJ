package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <h1>查询用户发表博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class UserBlogBlogsQueryDTO {
    @NotNull
    private Long userId;
    
    /**
     * @see com.emiyaoj.common.domain.PageDTO
     */
    @NotNull
    private Integer pageNo;
    
    @NotNull
    private Integer pageSize;
}
