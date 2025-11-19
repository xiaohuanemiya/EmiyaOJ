package com.emiyaoj.service.service;

/**
 * 判题服务接口
 */
public interface IJudgeService {
    
    /**
     * 异步执行判题
     * 
     * @param submissionId 提交ID
     */
    void judgeAsync(Long submissionId);
    
    /**
     * 执行判题（同步方法，由异步调用）
     * 
     * @param submissionId 提交ID
     */
    void judge(Long submissionId);
}
