package com.emiyaoj.service.domain.dto;

import lombok.Data;

/**
 * 代码提交DTO
 */
@Data
public class SubmitCodeDTO {
    
    /**
     * 题目ID
     */
    private Long problemId;
    
    /**
     * 语言ID
     */
    private Long languageId;
    
    /**
     * 源代码
     */
    private String code;
}
