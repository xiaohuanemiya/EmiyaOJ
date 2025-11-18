package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("test_case")
public class TestCase {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long problemId;
    private String input;
    private String output;
    private Integer isSample;
    private Integer score;
    private Integer sortOrder;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
