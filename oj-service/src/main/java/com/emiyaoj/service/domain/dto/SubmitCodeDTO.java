package com.emiyaoj.service.domain.dto;

import lombok.Data;

@Data
public class SubmitCodeDTO {
    private Long problemId;
    private String language;
    private String code;
}
