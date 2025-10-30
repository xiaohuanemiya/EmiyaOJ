package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * <h1>用户发布博客</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class BlogSaveDTO {
    private Long userId;
    
    private String title;
    
    private String content;
    
    // 传id
    @NotNull
    @Size(min = 1)
    private List<Long> tagIds;
}
