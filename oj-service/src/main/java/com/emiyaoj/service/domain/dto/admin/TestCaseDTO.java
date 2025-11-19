package com.emiyaoj.service.domain.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 测试用例DTO
 */
@Data
@Schema(description = "测试用例DTO")
public class TestCaseDTO {
    
    @Schema(description = "测试用例ID（更新时必填）")
    private Long id;
    
    @Schema(description = "题目ID", required = true)
    private Long problemId;
    
    @Schema(description = "输入数据", required = true)
    private String input;
    
    @Schema(description = "输出数据", required = true)
    private String output;
    
    @Schema(description = "是否为样例（0-否，1-是）", required = true)
    private Integer isSample;
    
    @Schema(description = "分值（若支持部分分）")
    private Integer score;
    
    @Schema(description = "排序序号")
    private Integer sortOrder;
}
