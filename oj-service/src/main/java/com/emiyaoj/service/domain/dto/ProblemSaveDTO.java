package com.emiyaoj.service.domain.dto;

import lombok.Data;

@Data
public class ProblemSaveDTO {
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private Integer difficulty;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer stackLimit;
    private String examples;
    private String hint;
    private String tags;
}
