package com.emiyaoj.service.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目VO
 */
@Data
public class ProblemVO {
    
    /**
     * 题目ID
     */
    private Long id;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 题目描述
     */
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
    private Integer timeLimit;
    
    /**
     * 内存限制（MB）
     */
    private Integer memoryLimit;
    
    /**
     * 通过次数
     */
    private Integer acceptCount;
    
    /**
     * 提交次数
     */
    private Integer submitCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
