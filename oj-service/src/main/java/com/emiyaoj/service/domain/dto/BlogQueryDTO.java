package com.emiyaoj.service.domain.dto;

import lombok.Data;

import java.sql.Date;

/**
 * <h1>BlogQueryDTO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
public class BlogQueryDTO {
    private Long userId;
    
    private String title;
    
    private Date createTime;
    
    private Integer pageNo;
    
    private Integer pageSize;
}
