// GoJudgeService.java
package com.emiyaoj.service.util;

import com.emiyaoj.service.util.oj.Model.Request;
import com.emiyaoj.service.util.oj.Model.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GoJudgeService {
    
    @Value("${GoJudge.url}")
    private String goJudgeUrl;
    
    private final RestTemplate restTemplate;
    
    public GoJudgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * 执行Go Judge请求
     */
    public List<Result> execute(Request request) {
        String url = goJudgeUrl + "/run";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Request> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Result[]> response = restTemplate.exchange(
            url, HttpMethod.POST, entity, Result[].class);
        
        return Arrays.asList(response.getBody());
    }
    
    /**
     * 删除缓存文件
     */
    public void deleteFile(String fileId) {
        String url = goJudgeUrl + "/file/" + fileId;
        restTemplate.delete(url);
    }
}