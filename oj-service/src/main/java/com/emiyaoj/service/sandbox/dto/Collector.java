package com.emiyaoj.service.sandbox.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Collector - 收集器
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Collector extends SandboxFile {
    private String name; // copyOut 文件名
    private Long max; // 最大大小限制
    private Boolean pipe; // 通过管道收集
    
    public Collector(String name, Long max) {
        this.setType("collector");
        this.name = name;
        this.max = max;
        this.pipe = false;
    }
}
