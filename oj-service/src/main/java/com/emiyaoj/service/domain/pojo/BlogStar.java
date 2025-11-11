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
 * <h1>用户收藏博客关联表</h1>
 *
 * @author Erida
 * @since 2025/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_star")
public class BlogStar {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("blog_id")
    private Long blogId;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("deleted")
    private Integer deleted;  // 博客或用户不存在时标记，当用户存在且主动取消收藏时，直接删除记录
}
