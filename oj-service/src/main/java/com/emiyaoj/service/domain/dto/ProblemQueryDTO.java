package com.emiyaoj.service.domain.dto;

import com.emiyaoj.common.domain.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 题目查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemQueryDTO extends PageDTO {
    
    /**
     * 难度：1-简单，2-中等，3-困难
     */
    private Integer difficulty;
    
    /**
     * 状态：0-隐藏，1-公开
     */
    private Integer status;
    
    /**
     * 关键词（题目标题）
     */
    private String keyword;
}

