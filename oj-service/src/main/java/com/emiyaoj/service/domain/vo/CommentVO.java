package com.emiyaoj.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h1>博客评论VO</h1>
 *
 * @author Erida
 * @since 2025/10/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    private Long id;
    
    private String userId;
    
    private String username;
    
    private String nickname;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
