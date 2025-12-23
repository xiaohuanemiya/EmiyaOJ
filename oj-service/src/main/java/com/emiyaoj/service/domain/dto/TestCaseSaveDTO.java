package com.emiyaoj.service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 测试用例新增/修改DTO
 */
@Data
public class TestCaseSaveDTO {

    /**
     * 测试用例ID（修改时必传）
     */
    private Long id;

    /**
     * 题目ID
     */
    @NotNull(message = "题目ID不能为空")
    private Long problemId;

    /**
     * 输入数据
     */
    @NotBlank(message = "输入数据不能为空")
    private String input;

    /**
     * 预期输出
     */
    @NotBlank(message = "预期输出不能为空")
    private String output;

    /**
     * 是否为样例：0-否，1-是
     */
    private Integer isSample;

    /**
     * 分值（若支持部分分）
     */
    private Integer score;

    /**
     * 排序
     */
    private Integer sortOrder;
}
