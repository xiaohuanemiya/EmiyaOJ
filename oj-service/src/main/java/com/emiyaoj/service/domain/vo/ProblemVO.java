package com.emiyaoj.service.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目VO
 */
@Data
public class ProblemVO {
    
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
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
    private Integer status;
    private String statusDesc;
    private Integer acceptCount;
    private Integer submitCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

