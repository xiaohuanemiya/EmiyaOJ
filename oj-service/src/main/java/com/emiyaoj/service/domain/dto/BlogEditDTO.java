package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <h1>修改博客基本信息DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class BlogEditDTO {
    /**
     * 博客id，前端参数写到路径上，后端再调用Setter方法
     */
    private Long id;
    
    @NotNull
    private Long userId;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50个字符")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    @Size(max = 10000, message = "内容长度不能超过10000个字符")
    private String content;
}
