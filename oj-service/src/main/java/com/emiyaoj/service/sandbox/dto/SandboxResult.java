package com.emiyaoj.service.sandbox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 沙箱执行结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxResult {
    
    /**
     * 执行状态
     */
    private String status;
    
    /**
     * 详细错误信息
     */
    private String error;
    
    /**
     * 程序返回值
     */
    private Integer exitStatus;
    
    /**
     * 程序运行 CPU 时间，单位纳秒
     */
    private Long time;
    
    /**
     * 程序运行内存，单位 byte
     */
    private Long memory;
    
    /**
     * 程序运行最大线程数量
     */
    private Integer procPeak;
    
    /**
     * 程序运行现实时间，单位纳秒
     */
    private Long runTime;
    
    /**
     * copyOut 和 pipeCollector 指定的文件内容
     */
    private Map<String, String> files;
    
    /**
     * copyFileCached 指定的文件 id
     */
    private Map<String, String> fileIds;
    
    /**
     * 文件错误详细信息
     */
    private List<FileError> fileError;
    
    /**
     * 状态枚举
     */
    public enum Status {
        ACCEPTED("Accepted"),
        MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded"),
        TIME_LIMIT_EXCEEDED("Time Limit Exceeded"),
        OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded"),
        FILE_ERROR("File Error"),
        NONZERO_EXIT_STATUS("Nonzero Exit Status"),
        SIGNALLED("Signalled"),
        INTERNAL_ERROR("Internal Error");
        
        private final String value;
        
        Status(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * 文件错误类型
     */
    public enum FileErrorType {
        COPY_IN_OPEN_FILE("CopyInOpenFile"),
        COPY_IN_CREATE_FILE("CopyInCreateFile"),
        COPY_IN_COPY_CONTENT("CopyInCopyContent"),
        COPY_OUT_OPEN("CopyOutOpen"),
        COPY_OUT_NOT_REGULAR_FILE("CopyOutNotRegularFile"),
        COPY_OUT_SIZE_EXCEEDED("CopyOutSizeExceeded"),
        COPY_OUT_CREATE_FILE("CopyOutCreateFile"),
        COPY_OUT_COPY_CONTENT("CopyOutCopyContent"),
        COLLECT_SIZE_EXCEEDED("CollectSizeExceeded");
        
        private final String value;
        
        FileErrorType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * 文件错误详细信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileError {
        private String name; // 错误文件名称
        private String type; // 错误代码
        private String message; // 错误信息
    }
}
