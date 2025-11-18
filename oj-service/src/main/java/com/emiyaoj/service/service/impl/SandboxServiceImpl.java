package com.emiyaoj.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.emiyaoj.service.domain.sandbox.SandboxRequest;
import com.emiyaoj.service.domain.sandbox.SandboxResult;
import com.emiyaoj.service.properties.SandboxProperties;
import com.emiyaoj.service.service.ISandboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SandboxServiceImpl implements ISandboxService {
    
    private final SandboxProperties sandboxProperties;
    private final RestTemplate restTemplate;
    
    @Override
    public List<SandboxResult> run(SandboxRequest request) {
        String url = sandboxProperties.getUrl() + "/run";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<SandboxRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            log.debug("Sending request to sandbox: {}", JSON.toJSONString(request));
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                log.debug("Received response from sandbox: {}", body);
                return JSON.parseObject(body, new TypeReference<List<SandboxResult>>() {});
            } else {
                log.error("Sandbox returned error status: {}", response.getStatusCode());
                throw new RuntimeException("Sandbox service error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error calling sandbox service", e);
            throw new RuntimeException("Failed to call sandbox service", e);
        }
    }
    
    @Override
    public String uploadFile(String content) {
        String url = sandboxProperties.getUrl() + "/file";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        HttpEntity<String> entity = new HttpEntity<>(content, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to upload file: {}", response.getStatusCode());
                throw new RuntimeException("Failed to upload file");
            }
        } catch (Exception e) {
            log.error("Error uploading file to sandbox", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public String downloadFile(String fileId) {
        String url = sandboxProperties.getUrl() + "/file/" + fileId;
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to download file: {}", response.getStatusCode());
                throw new RuntimeException("Failed to download file");
            }
        } catch (Exception e) {
            log.error("Error downloading file from sandbox", e);
            throw new RuntimeException("Failed to download file", e);
        }
    }
    
    @Override
    public void deleteFile(String fileId) {
        String url = sandboxProperties.getUrl() + "/file/" + fileId;
        
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error deleting file from sandbox", e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
