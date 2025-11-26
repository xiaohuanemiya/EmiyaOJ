package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>发表评论</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCommentSaveDTO {
    private Long userId;
    
    @NotBlank
    @Size(max = 200, message = "评论长度不能超过200")
    private String content;
    
    // 待扩展
}
