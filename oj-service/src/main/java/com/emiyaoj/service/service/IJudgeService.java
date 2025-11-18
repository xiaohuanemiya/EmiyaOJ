package com.emiyaoj.service.service;

/**
 * 判题服务接口
 */
public interface IJudgeService {
    
    /**
     * 执行判题
     * 
     * @param submissionId 提交ID
     */
    void judge(Long submissionId);
}
