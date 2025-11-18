package com.emiyaoj.service.sandbox.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 沙箱配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sandbox")
public class SandboxProperties {
    
    /**
     * 沙箱服务地址
     */
    private String url = "http://10.30.22.1:10000";
    
    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectionTimeout = 10000;
    
    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = 300000;
    
    /**
     * 是否启用沙箱
     */
    private Boolean enabled = true;
}
