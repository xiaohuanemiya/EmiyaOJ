package com.emiyaoj.stu.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提交记录实体类
 */
@Data
@TableName("submission")
public class Submission {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("problem_id")
    private Long problemId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("language_id")
    private Long languageId;
    
    @TableField("code")
    private String code;
    
    @TableField("status")
    private String status;
    
    @TableField("score")
    private Integer score;
    
    @TableField("time_used")
    private Integer timeUsed;
    
    @TableField("memory_used")
    private Integer memoryUsed;
    
    @TableField("error_message")
    private String errorMessage;
    
    @TableField("compile_message")
    private String compileMessage;
    
    @TableField("pass_rate")
    private String passRate;
    
    @TableField("ip_address")
    private String ipAddress;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}

