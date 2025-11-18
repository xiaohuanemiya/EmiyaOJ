package com.emiyaoj.service.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProblemVO {
    private Long id;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private Integer difficulty;
    private String difficultyDesc;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer stackLimit;
    private String examples;
    private String hint;
    private String tags;
    private Integer acceptedCount;
    private Integer submitCount;
    private Double acceptRate;
    private Integer status;
    private LocalDateTime createTime;
}
