package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>用户博客表</h1>
 * 用于在用户模块和博客模块之间加上一层过渡方便后续对用户博客信息做增量操作
 *
 * @author Erida
 * @since 2025-10-29
 */
@Data
@Accessors(chain = true)
@TableName("user_blog")
public class UserBlog {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    
    // TODO: 其他属性待扩展
}
