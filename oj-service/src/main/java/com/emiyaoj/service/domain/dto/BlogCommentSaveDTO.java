package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * <h1>发表评论</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class BlogCommentSaveDTO {
    private Long userId;
    
    private String content;
}
