package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * <h1>查询用户发表博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class UserBlogBlogsQueryDTO {
    private Long userId;
    
    private Integer pageNo;
    
    private Integer pageSize;
}
