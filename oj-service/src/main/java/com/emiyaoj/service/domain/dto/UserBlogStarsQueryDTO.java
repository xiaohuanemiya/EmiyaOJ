package com.emiyaoj.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h1>查询用户收藏博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBlogStarsQueryDTO {
    private Long userId;
    
    private Integer pageNo;
    
    private Integer pageSize;
}
