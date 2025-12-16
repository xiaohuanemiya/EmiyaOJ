package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 题目保存DTO
 */
@Data
public class ProblemSaveDTO {
    
    /**
     * 题目ID（修改时必传）
     */
    private Long id;
    
    /**
     * 题目标题
     */
    @NotBlank(message = "题目标题不能为空")
    private String title;
    
    /**
     * 题目描述
     */
    @NotBlank(message = "题目描述不能为空")
    private String description;
    
    /**
     * 输入描述
     */
    private String inputDescription;
    
    /**
     * 输出描述
     */
    private String outputDescription;
    
    /**
     * 样例输入
     */
    private String sampleInput;
    
    /**
     * 样例输出
     */
    private String sampleOutput;
    
    /**
     * 提示信息
     */
    private String hint;
    
    /**
     * 难度：1-简单，2-中等，3-困难
     */
    private Integer difficulty;
    
    /**
     * 时间限制（毫秒）
     */
    @NotNull(message = "时间限制不能为空")
    @Min(value = 100, message = "时间限制最小为100毫秒")
    private Integer timeLimit;
    
    /**
     * 内存限制（MB）
     */
    @NotNull(message = "内存限制不能为空")
    @Min(value = 16, message = "内存限制最小为16MB")
    private Integer memoryLimit;
    
    /**
     * 栈限制（MB）
     */
    private Integer stackLimit;
    
    /**
     * 题目来源
     */
    private String source;
    
    /**
     * 状态：0-隐藏，1-公开
     */
    private Integer status;
}

