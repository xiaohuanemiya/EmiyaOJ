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
 * 测试用例判题结果实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("submission_result")
public class SubmissionResult {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 提交ID
     */
    private Long submissionId;
    
    /**
     * 测试用例ID
     */
    private Long testCaseId;
    
    /**
     * 判题状态
     */
    private String status;
    
    /**
     * 使用时间（毫秒）
     */
    private Integer timeUsed;
    
    /**
     * 使用内存（KB）
     */
    private Integer memoryUsed;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
