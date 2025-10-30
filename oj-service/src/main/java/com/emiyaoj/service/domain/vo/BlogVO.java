package com.emiyaoj.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * <h1>博客VO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVO {
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content;
    
    private Date createTime;
    
    private Date updateTime;
}
