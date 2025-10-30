package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * <h1>BlogEditDTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class BlogEditDTO {
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content;
}
