package com.emiyaoj.service.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <h1>博客评论查询DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class CommentQueryDTO {
    private Long userId;
    
    private Long blogId;
    
    private LocalDateTime fromDay;
    
    private LocalDateTime toDay;
}
