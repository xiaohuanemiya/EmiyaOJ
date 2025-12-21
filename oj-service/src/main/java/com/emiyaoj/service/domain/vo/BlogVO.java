package com.emiyaoj.service.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long id;
    
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long userId;
    
    private String title;
    
    private String content;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;

    private List<BlogTagVO> tags;
}
