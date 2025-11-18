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
 * 题目标签关联实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("problem_tag")
public class ProblemTag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 题目ID
     */
    private Long problemId;
    
    /**
     * 标签ID
     */
    private Long tagId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
