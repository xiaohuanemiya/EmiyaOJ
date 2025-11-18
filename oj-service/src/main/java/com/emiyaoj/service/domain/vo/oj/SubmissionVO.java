package com.emiyaoj.service.domain.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 提交VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionVO {
    
    private Long id;
    private Long problemId;
    private String problemTitle;
    private Long userId;
    private String username;
    private Long languageId;
    private String languageName;
    private String status;
    private Integer score;
    private Integer timeUsed;
    private Integer memoryUsed;
    private String passRate;
    private LocalDateTime createTime;
}
