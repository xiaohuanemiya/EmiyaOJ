package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 题目新增/修改DTO
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
    @Size(max = 255, message = "题目标题长度不能超过255个字符")
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
     * CPU时间限制（毫秒）
     */
    @NotNull(message = "时间限制不能为空")
    private Integer timeLimit;

    /**
     * 内存限制（MB）
     */
    @NotNull(message = "内存限制不能为空")
    private Integer memoryLimit;

    /**
     * 栈内存限制（MB）
     */
    private Integer stackLimit;

    /**
     * 题目来源
     */
    @Size(max = 255, message = "题目来源长度不能超过255个字符")
    private String source;

    /**
     * 状态：0-隐藏，1-公开
     */
    private Integer status;
}
