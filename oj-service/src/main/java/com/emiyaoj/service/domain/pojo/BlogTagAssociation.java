package com.emiyaoj.service.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>博客标签表</h1>
 * 基本上由系统管理员决定标签类型
 *
 * @author Erida
 * @since 2025-10-29
 */
@Data
@Accessors(chain = true)
@TableName("blog_tag_association")
public class BlogTagAssociation {
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("blog_id")
    private Long blogId;
    
    @TableField("tag_id")
    private Long tagId;
    
    /* blog和tag为n:m关系，在关系上还可以继续补充属性 */
}
