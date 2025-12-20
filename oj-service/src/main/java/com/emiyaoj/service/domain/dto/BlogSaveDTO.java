package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>用户发布博客</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogSaveDTO {
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50")
    private String title;
    
    @NotBlank
    @Size(max = 1000, message = "内容长度不能超过1000")
    private String content;
    
    // 传id
    @NotNull
    @Size(min = 1, message = "至少选择一个标签")
    private List<Long> tagIds;
}
