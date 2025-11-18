package com.emiyaoj.service.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionVO {
    private Long id;
    private Long problemId;
    private String problemTitle;
    private Long userId;
    private String username;
    private String language;
    private String status;
    private Long timeUsed;
    private Long memoryUsed;
    private String passRate;
    private String errorMessage;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}
