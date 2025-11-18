package com.emiyaoj.service.sandbox.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MemoryFile - 内存文件
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MemoryFile extends SandboxFile {
    private String content; // 文件内容
    
    public MemoryFile(String content) {
        this.setType("memory");
        this.content = content;
    }
}
