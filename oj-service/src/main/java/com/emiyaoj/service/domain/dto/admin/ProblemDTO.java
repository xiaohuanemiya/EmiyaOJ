package com.emiyaoj.service.domain.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题目DTO
 */
@Data
@Schema(description = "题目DTO")
public class ProblemDTO {
    
    @Schema(description = "题目ID（更新时必填）")
    private Long id;
    
    @Schema(description = "题目标题", required = true)
    private String title;
    
    @Schema(description = "题目描述", required = true)
    private String description;
    
    @Schema(description = "输入描述")
    private String inputDescription;
    
    @Schema(description = "输出描述")
    private String outputDescription;
    
    @Schema(description = "样例输入")
    private String sampleInput;
    
    @Schema(description = "样例输出")
    private String sampleOutput;
    
    @Schema(description = "提示")
    private String hint;
    
    @Schema(description = "来源")
    private String source;
    
    @Schema(description = "难度（1-简单，2-中等，3-困难）", required = true)
    private Integer difficulty;
    
    @Schema(description = "时间限制（毫秒）", required = true)
    private Integer timeLimit;
    
    @Schema(description = "内存限制（MB）", required = true)
    private Integer memoryLimit;
    
    @Schema(description = "栈限制（MB）")
    private Integer stackLimit;
    
    @Schema(description = "状态（0-隐藏，1-公开）", required = true)
    private Integer status;
    
    @Schema(description = "是否删除（0-未删除，1-已删除）")
    private Integer deleted;
}
