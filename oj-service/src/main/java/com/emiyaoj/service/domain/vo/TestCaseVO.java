package com.emiyaoj.service.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试用例VO
 */
@Data
public class TestCaseVO {

    /**
     * 测试用例ID
     */
    private Long id;

    /**
     * 题目ID
     */
    private Long problemId;

    /**
     * 输入数据
     */
    private String input;

    /**
     * 预期输出
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
