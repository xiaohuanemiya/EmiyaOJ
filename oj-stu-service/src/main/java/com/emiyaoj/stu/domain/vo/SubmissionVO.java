package com.emiyaoj.stu.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 提交记录VO
 */
@Data
public class SubmissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long problemId;
    private Long userId;
    private Long languageId;
    private String languageName;
    private String code;
    private String status;
    private Integer score;
    private Integer timeUsed;
    private Integer memoryUsed;
    private String errorMessage;
    private String compileMessage;
    private String passRate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

