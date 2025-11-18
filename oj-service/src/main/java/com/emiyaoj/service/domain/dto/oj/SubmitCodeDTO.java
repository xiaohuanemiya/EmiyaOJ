package com.emiyaoj.service.domain.dto.oj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代码提交DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
