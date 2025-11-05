package com.emiyaoj.service.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
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
    @JSONField(serializeFeatures = com.alibaba.fastjson2.JSONWriter.Feature.WriteLongAsString)
    private Long id;
    
    private String name;
    
    private String desc;
}
