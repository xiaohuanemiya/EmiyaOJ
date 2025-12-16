package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目实体类
 */
@Data
@TableName("problem")
public class Problem {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("title")
    private String title;
    
    @TableField("description")
    private String description;
    
    @TableField("input_description")
    private String inputDescription;
    
    @TableField("output_description")
    private String outputDescription;
    
    @TableField("sample_input")
    private String sampleInput;
    
    @TableField("sample_output")
    private String sampleOutput;
    
    @TableField("hint")
    private String hint;
    
    @TableField("difficulty")
    private Integer difficulty;
    
    @TableField("time_limit")
    private Integer timeLimit;
    
    @TableField("memory_limit")
    private Integer memoryLimit;
    
    @TableField("stack_limit")
    private Integer stackLimit;
    
    @TableField("source")
    private String source;
    
    @TableField("author_id")
    private Long authorId;
    
    @TableField("accept_count")
    private Integer acceptCount;
    
    @TableField("submit_count")
    private Integer submitCount;
    
    @TableField("status")
    private Integer status;
    
    @TableField("deleted")
    private Integer deleted;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField("create_by")
    private Long createBy;
    
    @TableField("update_by")
    private Long updateBy;
}

