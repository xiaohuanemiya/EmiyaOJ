package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试用例实体类
 */
@Data
@TableName("test_case")
public class TestCase {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("problem_id")
    private Long problemId;
    
    @TableField("input")
    private String input;
    
    @TableField("output")
    private String output;
    
    @TableField("is_sample")
    private Integer isSample;
    
    @TableField("score")
    private Integer score;
    
    @TableField("sort_order")
    private Integer sortOrder;
    
    @TableField("deleted")
    private Integer deleted;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}

