package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>博客模块图片存储</h1>
 *
 * @author Erida
 * @since 2025/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_picture")
public class BlogPicture {
    @TableId("url")
    private String url;
    
    @TableField("deleted")
    private Integer deleted;
}
