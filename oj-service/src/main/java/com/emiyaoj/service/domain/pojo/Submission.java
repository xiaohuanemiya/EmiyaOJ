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
 * 提交记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("submission")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提交ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目ID
     */
    @TableField("problem_id")
    private Long problemId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 语言ID
     */
    @TableField("language_id")
    private Long languageId;

    /**
     * 源代码
     */
    @TableField("code")
    private String code;

    /**
     * 判题状态：Pending, Judging, Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Runtime Error, Compile Error, System Error
     */
    @TableField("status")
    private String status;

    /**
     * 得分
     */
    @TableField("score")
    private Integer score;

    /**
     * 实际使用时间（毫秒）
     */
    @TableField("time_used")
    private Integer timeUsed;

    /**
     * 实际使用内存（KB）
     */
    @TableField("memory_used")
    private Integer memoryUsed;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 编译信息
     */
    @TableField("compile_message")
    private String compileMessage;

    /**
     * 通过率，如 10/10
     */
    @TableField("pass_rate")
    private String passRate;

    /**
     * 提交IP
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 提交时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
