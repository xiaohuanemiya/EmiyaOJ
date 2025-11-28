package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h1>博客模块评论</h1>
 *
 * @author Erida
 * @since 2025/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_comment")
public class BlogComment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("blog_id")
    private Long blogId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("content")
    private String content;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField("deleted")
    private Integer deleted;
}
