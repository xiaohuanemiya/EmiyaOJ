package com.emiyaoj.service.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
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
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long id;
    
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long userId;
    
    private String username;
    
    private String nickname;
    
    private String content;

    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
