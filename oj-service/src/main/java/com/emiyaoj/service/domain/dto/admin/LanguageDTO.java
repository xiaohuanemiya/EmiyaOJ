package com.emiyaoj.service.domain.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 语言DTO
 */
@Data
@Schema(description = "语言DTO")
public class LanguageDTO {
    
    @Schema(description = "语言ID（更新时必填）")
    private Long id;
    
    @Schema(description = "语言名称", required = true)
    private String name;
    
    @Schema(description = "版本", required = true)
    private String version;
    
    @Schema(description = "状态（0-禁用，1-启用）", required = true)
    private Integer status;
    
    @Schema(description = "是否需要编译（0-否，1-是）", required = true)
    private Integer isCompiled;
    
    @Schema(description = "源文件扩展名", example = ".c")
    private String sourceFileExt;
    
    @Schema(description = "可执行文件扩展名", example = "")
    private String executableExt;
    
    @Schema(description = "编译命令", example = "gcc-14 -O2 -Wall -std=c17 -o {executable} {source}")
    private String compileCommand;
    
    @Schema(description = "执行命令", example = "./{executable}")
    private String executeCommand;
    
    @Schema(description = "时间限制倍数", example = "1.0")
    private BigDecimal timeLimitMultiplier;
    
    @Schema(description = "内存限制倍数", example = "1.0")
    private BigDecimal memoryLimitMultiplier;
}
