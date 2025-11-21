package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.pojo.Problem;

/**
 * 题目Service
 */
public interface IProblemService extends IService<Problem> {
    
    /**
     * 增加通过次数
     * @param problemId 题目ID
     */
    void incrementAcceptCount(Long problemId);
    
    /**
     * 增加提交次数
     * @param problemId 题目ID
     */
    void incrementSubmitCount(Long problemId);
}
