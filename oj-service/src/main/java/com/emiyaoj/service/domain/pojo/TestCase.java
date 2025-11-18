package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 测试用例实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("test_case")
public class TestCase {
    
    @TableId(type = IdType.AUTO)
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
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
