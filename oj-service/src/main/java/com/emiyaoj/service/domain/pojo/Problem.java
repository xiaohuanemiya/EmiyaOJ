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
 * 题目表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("problem")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目标题
     */
    @TableField("title")
    private String title;

    /**
     * 题目描述
     */
    @TableField("description")
    private String description;

    /**
     * 输入描述
     */
    @TableField("input_description")
    private String inputDescription;

    /**
     * 输出描述
     */
    @TableField("output_description")
    private String outputDescription;

    /**
     * 样例输入
     */
    @TableField("sample_input")
    private String sampleInput;

    /**
     * 样例输出
     */
    @TableField("sample_output")
    private String sampleOutput;

    /**
     * 提示信息
     */
    @TableField("hint")
    private String hint;

    /**
     * 难度：1-简单，2-中等，3-困难
     */
    @TableField("difficulty")
    private Integer difficulty;

    /**
     * CPU时间限制（毫秒）
     */
    @TableField("time_limit")
    private Integer timeLimit;

    /**
     * 内存限制（MB）
     */
    @TableField("memory_limit")
    private Integer memoryLimit;

    /**
     * 栈内存限制（MB）
     */
    @TableField("stack_limit")
    private Integer stackLimit;

    /**
     * 题目来源
     */
    @TableField("source")
    private String source;

    /**
     * 出题人ID
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 通过次数
     */
    @TableField("accept_count")
    private Integer acceptCount;

    /**
     * 提交次数
     */
    @TableField("submit_count")
    private Integer submitCount;

    /**
     * 状态：0-隐藏，1-公开
     */
    @TableField("status")
    private Integer status;

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

    /**
     * 创建者
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    private Long updateBy;
}
