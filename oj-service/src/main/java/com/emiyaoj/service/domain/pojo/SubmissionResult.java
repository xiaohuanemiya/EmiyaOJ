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
 * 测试用例判题结果表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("submission_result")
public class SubmissionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结果ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提交ID
     */
    @TableField("submission_id")
    private Long submissionId;

    /**
     * 测试用例ID
     */
    @TableField("test_case_id")
    private Long testCaseId;

    /**
     * 判题状态
     */
    @TableField("status")
    private String status;

    /**
     * 使用时间（毫秒）
     */
    @TableField("time_used")
    private Integer timeUsed;

    /**
     * 使用内存（KB）
     */
    @TableField("memory_used")
    private Integer memoryUsed;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
