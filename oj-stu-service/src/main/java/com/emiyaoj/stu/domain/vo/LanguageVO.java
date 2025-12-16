package com.emiyaoj.stu.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 编程语言VO
 */
@Data
public class LanguageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String version;
    private String sourceFileExt;
}

