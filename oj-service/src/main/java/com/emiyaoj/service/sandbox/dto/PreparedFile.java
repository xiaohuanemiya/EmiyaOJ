package com.emiyaoj.service.sandbox.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * PreparedFile - 预先准备的文件（通过fileId引用）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PreparedFile extends SandboxFile {
    private String fileId; // 文件ID
    
    public PreparedFile(String fileId) {
        this.setType("prepared");
        this.fileId = fileId;
    }
}
