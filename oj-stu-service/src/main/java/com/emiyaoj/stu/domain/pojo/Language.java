package com.emiyaoj.stu.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 编程语言实体类
 */
@Data
@TableName("language")
public class Language {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("name")
    private String name;
    
    @TableField("version")
    private String version;
    
    @TableField("compile_command")
    private String compileCommand;
    
    @TableField("execute_command")
    private String executeCommand;
    
    @TableField("source_file_ext")
    private String sourceFileExt;
    
    @TableField("executable_ext")
    private String executableExt;
    
    @TableField("is_compiled")
    private Integer isCompiled;
    
    @TableField("time_limit_multiplier")
    private Double timeLimitMultiplier;
    
    @TableField("memory_limit_multiplier")
    private Double memoryLimitMultiplier;
    
    @TableField("status")
    private Integer status;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}

