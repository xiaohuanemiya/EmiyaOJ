package com.emiyaoj.stu.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目VO（学生端）
 */
@Data
public class ProblemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    private Integer stackLimit;
    private String source;
    private Integer acceptCount;
    private Integer submitCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

