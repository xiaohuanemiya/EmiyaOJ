package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h1>分页条件查博客DTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogQueryDTO {
    
    // 模糊搜索，为空默认查全部
    @Size(max = 50, message = "标题长度不能超过50个字符")
    private String title;
    
    // 查当天数据，为空默认查全部
    private LocalDateTime createTime;
    
    /**
     * @see com.emiyaoj.common.domain.PageDTO
     */
    @NotNull
    private Integer pageNo;
    
    @NotNull
    private Integer pageSize;
}
