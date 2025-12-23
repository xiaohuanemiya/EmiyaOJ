package com.emiyaoj.service.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提交记录VO
 */
@Data
public class SubmissionVO {
    
    /**
     * 提交ID
     */
    private Long id;
    
    /**
     * 题目ID
     */
    private Long problemId;
    
    /**
     * 题目标题
     */
    private String problemTitle;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 语言ID
     */
    private Long languageId;
    
    /**
     * 语言名称
     */
    private String languageName;
    
    /**
     * 判题状态
     */
    private String status;
    
    /**
     * 得分
     */
    private Integer score;
    
    /**
     * 实际使用时间（毫秒）
     */
    private Integer timeUsed;
    
    /**
     * 实际使用内存（KB）
     */
    private Integer memoryUsed;
    
    /**
     * 通过率
     */
    private String passRate;
    
    /**
     * 提交时间
     */
    private LocalDateTime createTime;
}
