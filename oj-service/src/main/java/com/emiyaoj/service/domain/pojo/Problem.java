package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("problem")
public class Problem {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private Integer difficulty;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Integer stackLimit;
    private String examples;
    private String hint;
    private String tags;
    private Integer acceptedCount;
    private Integer submitCount;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createBy;
    private Long updateBy;
}
