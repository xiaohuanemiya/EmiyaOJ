package com.emiyaoj.service.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <h1>查询用户收藏博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class UserBlogStarsQueryDTO {
    private Long userId;
    
    private Integer pageNo;
    
    private Integer pageSize;
}
