package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.pojo.TestCase;

import java.util.List;

/**
 * 测试用例服务接口
 */
public interface ITestCaseService extends IService<TestCase> {
    
    /**
     * 获取题目的所有测试用例
     * 
     * @param problemId 题目ID
     * @return 测试用例列表
     */
    List<TestCase> listByProblemId(Long problemId);
    
    /**
     * 获取题目的非样例测试用例
     * 
     * @param problemId 题目ID
     * @return 测试用例列表
     */
    List<TestCase> listNonSampleByProblemId(Long problemId);
}
