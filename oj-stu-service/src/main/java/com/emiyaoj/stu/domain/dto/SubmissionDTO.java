package com.emiyaoj.stu.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 提交代码DTO
 */
@Data
public class SubmissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "题目ID不能为空")
    private Long problemId;

    @NotNull(message = "语言ID不能为空")
    private Long languageId;

    @NotBlank(message = "代码不能为空")
    private String code;
}

