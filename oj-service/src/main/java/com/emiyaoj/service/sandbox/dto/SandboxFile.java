package com.emiyaoj.service.sandbox.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * 沙箱文件基类
 */
@Data
public abstract class SandboxFile {
    private String type;
}

/**
 * LocalFile - 本地文件
 */
@Data
class LocalFile extends SandboxFile {
    private String src; // 文件绝对路径
    
    public LocalFile() {
        this.setType("local");
    }
    
    public LocalFile(String src) {
        this();
        this.src = src;
    }
}

/**
 * MemoryFile - 内存文件
 */
@Data
class MemoryFile extends SandboxFile {
    private String content; // 文件内容
    
    public MemoryFile() {
        this.setType("memory");
    }
    
    public MemoryFile(String content) {
        this();
        this.content = content;
    }
}

/**
 * PreparedFile - 预准备文件
 */
@Data
class PreparedFile extends SandboxFile {
    private String fileId; // 文件 id
    
    public PreparedFile() {
        this.setType("prepared");
    }
    
    public PreparedFile(String fileId) {
        this();
        this.fileId = fileId;
    }
}

/**
 * Collector - 收集器
 */
@Data
class Collector extends SandboxFile {
    private String name; // copyOut 文件名
    private Long max; // 最大大小限制
    private Boolean pipe; // 通过管道收集
    
    public Collector() {
        this.setType("collector");
    }
    
    public Collector(String name, Long max) {
        this();
        this.name = name;
        this.max = max;
        this.pipe = false;
    }
}

/**
 * Symlink - 符号链接
 */
@Data
class Symlink extends SandboxFile {
    private String symlink; // 符号连接目标
    
    public Symlink() {
        this.setType("symlink");
    }
    
    public Symlink(String symlink) {
        this();
        this.symlink = symlink;
    }
}
