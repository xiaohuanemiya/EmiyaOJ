package com.emiyaoj.service.sandbox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 沙箱执行请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxRequest {
    
    /**
     * 请求ID（用于 WebSocket）
     */
    private String requestId;
    
    /**
     * 命令列表
     */
    private List<SandboxCmd> cmd;
    
    /**
     * 管道映射
     */
    private List<PipeMap> pipeMapping;
    
    /**
     * 管道索引
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PipeIndex {
        private Integer index; // cmd 的下标
        private Integer fd; // cmd 的 fd
    }
    
    /**
     * 管道映射
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PipeMap {
        private PipeIndex in; // 管道的输入端
        private PipeIndex out; // 管道的输出端
        private Boolean proxy; // 开启管道代理
        private String name; // copyOut 名称
        private Long max; // copyOut 最大大小限制
    }
}
