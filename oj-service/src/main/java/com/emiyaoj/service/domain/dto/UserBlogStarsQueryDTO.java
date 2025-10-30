package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * <h1>查询用户收藏博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class UserBlogStarsQueryDTO {
    private Long userId;
    
    // TODO: [博客模块] 收藏博客功能待扩展
}
