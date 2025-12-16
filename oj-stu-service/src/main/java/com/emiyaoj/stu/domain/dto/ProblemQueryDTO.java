package com.emiyaoj.stu.domain.dto;

import com.emiyaoj.common.domain.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 题目查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemQueryDTO extends PageDTO {
    
    private Integer difficulty;
    
    private String keyword;
}

