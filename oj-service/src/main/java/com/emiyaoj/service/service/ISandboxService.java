package com.emiyaoj.service.service;

import com.emiyaoj.service.domain.sandbox.SandboxRequest;
import com.emiyaoj.service.domain.sandbox.SandboxResult;

import java.util.List;

public interface ISandboxService {
    /**
     * 执行代码运行请求
     * @param request 沙箱请求
     * @return 运行结果列表
     */
    List<SandboxResult> run(SandboxRequest request);
    
    /**
     * 上传文件到沙箱
     * @param content 文件内容
     * @return 文件ID
     */
    String uploadFile(String content);
    
    /**
     * 下载文件从沙箱
     * @param fileId 文件ID
     * @return 文件内容
     */
    String downloadFile(String fileId);
    
    /**
     * 删除文件从沙箱
     * @param fileId 文件ID
     */
    void deleteFile(String fileId);
}
