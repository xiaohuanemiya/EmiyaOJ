package com.emiyaoj.service.domain.vo.oj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 语言VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageVO {
    
    private Long id;
    private String name;
    private String version;
    private String sourceFileExt;
    private Integer isCompiled;
}
