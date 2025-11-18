package com.emiyaoj.service.sandbox.client;

import com.emiyaoj.service.sandbox.dto.SandboxRequest;
import com.emiyaoj.service.sandbox.dto.SandboxResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 沙箱客户端服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SandboxClient {
    
    private final WebClient sandboxWebClient;
    
    /**
     * 执行代码
     * 
     * @param request 沙箱请求
     * @return 执行结果列表
     */
    public List<SandboxResult> run(SandboxRequest request) {
        try {
            log.info("Sending sandbox run request: requestId={}", request.getRequestId());
            
            List<SandboxResult> results = sandboxWebClient.post()
                    .uri("/run")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToFlux(SandboxResult.class)
                    .collectList()
                    .block();
            
            log.info("Received sandbox run response: {} results", results != null ? results.size() : 0);
            return results;
        } catch (Exception e) {
            log.error("Error executing sandbox run request", e);
            throw new RuntimeException("Sandbox execution failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 上传文件到沙箱
     * 
     * @param filename 文件名
     * @param content 文件内容
     * @return 文件ID
     */
    public String uploadFile(String filename, byte[] content) {
        try {
            log.info("Uploading file to sandbox: filename={}, size={} bytes", filename, content.length);
            
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", new ByteArrayResource(content))
                    .filename(filename);
            
            String fileId = sandboxWebClient.post()
                    .uri("/file")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(response -> (String) response.get("fileId"))
                    .block();
            
            log.info("File uploaded successfully: fileId={}", fileId);
            return fileId;
        } catch (Exception e) {
            log.error("Error uploading file to sandbox", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从沙箱下载文件
     * 
     * @param fileId 文件ID
     * @return 文件内容
     */
    public byte[] downloadFile(String fileId) {
        try {
            log.info("Downloading file from sandbox: fileId={}", fileId);
            
            byte[] content = sandboxWebClient.get()
                    .uri("/file/{fileId}", fileId)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            
            log.info("File downloaded successfully: fileId={}, size={} bytes", 
                    fileId, content != null ? content.length : 0);
            return content;
        } catch (Exception e) {
            log.error("Error downloading file from sandbox", e);
            throw new RuntimeException("File download failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除沙箱中的文件
     * 
     * @param fileId 文件ID
     */
    public void deleteFile(String fileId) {
        try {
            log.info("Deleting file from sandbox: fileId={}", fileId);
            
            sandboxWebClient.delete()
                    .uri("/file/{fileId}", fileId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            
            log.info("File deleted successfully: fileId={}", fileId);
        } catch (Exception e) {
            log.error("Error deleting file from sandbox", e);
            throw new RuntimeException("File deletion failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取所有文件ID到原始名称的映射
     * 
     * @return 文件映射
     */
    public Map<String, String> listFiles() {
        try {
            log.info("Listing all files in sandbox");
            
            Map<String, String> files = sandboxWebClient.get()
                    .uri("/file")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Listed {} files in sandbox", files != null ? files.size() : 0);
            return files;
        } catch (Exception e) {
            log.error("Error listing files from sandbox", e);
            throw new RuntimeException("File listing failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取沙箱版本信息
     * 
     * @return 版本信息
     */
    public Map<String, Object> getVersion() {
        try {
            log.info("Getting sandbox version");
            
            Map<String, Object> version = sandboxWebClient.get()
                    .uri("/version")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Sandbox version: {}", version);
            return version;
        } catch (Exception e) {
            log.error("Error getting sandbox version", e);
            throw new RuntimeException("Version check failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取沙箱配置信息
     * 
     * @return 配置信息
     */
    public Map<String, Object> getConfig() {
        try {
            log.info("Getting sandbox config");
            
            Map<String, Object> config = sandboxWebClient.get()
                    .uri("/config")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Sandbox config retrieved");
            return config;
        } catch (Exception e) {
            log.error("Error getting sandbox config", e);
            throw new RuntimeException("Config check failed: " + e.getMessage(), e);
        }
    }
}
