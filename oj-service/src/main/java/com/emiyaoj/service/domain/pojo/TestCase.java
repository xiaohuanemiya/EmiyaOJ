package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 测试用例表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_case")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测试用例ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目ID
     */
    @TableField("problem_id")
    private Long problemId;

    /**
     * 输入数据
     */
    @TableField("input")
    private String input;

    /**
     * 预期输出
     */
    @TableField("output")
    private String output;

    /**
     * 是否为样例：0-否，1-是
     */
    @TableField("is_sample")
    private Integer isSample;

    /**
     * 分值（若支持部分分）
     */
    @TableField("score")
    private Integer score;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
