package com.emiyaoj.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>BlogTagVO</h1>
 *
 * @author Erida
 * @since 2025/10/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogTagVO {
    private Long id;
    
    private String name;
    
    private String desc;
}
