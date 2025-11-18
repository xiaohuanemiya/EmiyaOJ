package com.emiyaoj.service.domain.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 题目VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemVO {
    
    private Long id;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String sampleInput;
    private String sampleOutput;
    private String hint;
    private Integer difficulty;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer acceptCount;
    private Integer submitCount;
    private LocalDateTime createTime;
}
