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
 * 题目实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("problem")
public class Problem {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 题目描述
     */
    private String description;
    
    /**
     * 输入描述
     */
    private String inputDescription;
    
    /**
     * 输出描述
     */
    private String outputDescription;
    
    /**
     * 样例输入
     */
    private String sampleInput;
    
    /**
     * 样例输出
     */
    private String sampleOutput;
    
    /**
     * 提示信息
     */
    private String hint;
    
    /**
     * 难度：1-简单，2-中等，3-困难
     */
    private Integer difficulty;
    
    /**
     * CPU时间限制（毫秒）
     */
    private Integer timeLimit;
    
    /**
     * 内存限制（MB）
     */
    private Integer memoryLimit;
    
    /**
     * 栈内存限制（MB）
     */
    private Integer stackLimit;
    
    /**
     * 题目来源
     */
    private String source;
    
    /**
     * 出题人ID
     */
    private Long authorId;
    
    /**
     * 通过次数
     */
    private Integer acceptCount;
    
    /**
     * 提交次数
     */
    private Integer submitCount;
    
    /**
     * 状态：0-隐藏，1-公开
     */
    private Integer status;
    
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
    
    /**
     * 创建者
     */
    private Long createBy;
    
    /**
     * 更新者
     */
    private Long updateBy;
}
