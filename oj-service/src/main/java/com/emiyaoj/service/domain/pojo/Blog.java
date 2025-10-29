package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * <h1>博客表</h1>
 *
 * @author Erida
 * @since 2025-10-29
 */
@Data
@Accessors(chain = true)
@TableName("blog")
public class Blog {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("title")
    private String title;
    
    @TableField("content")
    private String content;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
    
    @TableField("deleted")
    private Integer deleted;
}
