package com.emiyaoj.service.sandbox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 沙箱执行命令配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxCmd {
    
    /**
     * 程序命令行参数
     */
    private List<String> args;
    
    /**
     * 程序环境变量
     */
    private List<String> env;
    
    /**
     * 指定标准输入、标准输出和标准错误的文件
     */
    private List<SandboxFile> files;
    
    /**
     * 开启 TTY
     */
    private Boolean tty;
    
    /**
     * CPU时间限制，单位纳秒
     */
    private Long cpuLimit;
    
    /**
     * 等待时间限制，单位纳秒（通常为 cpuLimit 两倍）
     */
    private Long clockLimit;
    
    /**
     * 内存限制，单位 byte
     */
    private Long memoryLimit;
    
    /**
     * 栈内存限制，单位 byte
     */
    private Long stackLimit;
    
    /**
     * 线程数量限制
     */
    private Integer procLimit;
    
    /**
     * CPU 使用率限制，1000 等于单核 100%
     */
    private Integer cpuRateLimit;
    
    /**
     * 限制 CPU 使用，使用方式和 cpuset cgroup 相同
     */
    private String cpuSetLimit;
    
    /**
     * 开启 rlimit 堆空间限制
     */
    private Boolean dataSegmentLimit;
    
    /**
     * 开启 rlimit 虚拟内存空间限制
     */
    private Boolean addressSpaceLimit;
    
    /**
     * 在执行程序之前复制进容器的文件列表
     */
    private Map<String, SandboxFile> copyIn;
    
    /**
     * 在执行程序后从容器文件系统中复制出来的文件列表
     */
    private List<String> copyOut;
    
    /**
     * 和 copyOut 相同，不过文件不返回内容，而是返回一个对应文件 ID
     */
    private List<String> copyOutCached;
    
    /**
     * 指定 copyOut 复制文件大小限制，单位 byte
     */
    private Long copyOutMax;
    
    /**
     * 在文件大小超过限制后，是否复制截断后的文件
     */
    private Boolean copyOutTruncate;
}
