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
 * 提交记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("submission")
public class Submission {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 题目ID
     */
    private Long problemId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 语言ID
     */
    private Long languageId;
    
    /**
     * 源代码
     */
    private String code;
    
    /**
     * 判题状态：Pending, Judging, Accepted, Wrong Answer, Time Limit Exceeded, 
     * Memory Limit Exceeded, Runtime Error, Compile Error, System Error
     */
    private String status;
    
    /**
     * 得分
     */
    private Integer score;
    
    /**
     * 实际使用时间（毫秒）
     */
    private Integer timeUsed;
    
    /**
     * 实际使用内存（KB）
     */
    private Integer memoryUsed;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 编译信息
     */
    private String compileMessage;
    
    /**
     * 通过率，如 10/10
     */
    private String passRate;
    
    /**
     * 提交IP
     */
    private String ipAddress;
    
    /**
     * 提交时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
