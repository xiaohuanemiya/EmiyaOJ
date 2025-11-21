package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.pojo.TestCase;

import java.util.List;

/**
 * 测试用例Service
 */
public interface ITestCaseService extends IService<TestCase> {
    
    /**
     * 根据题目ID获取测试用例
     * @param problemId 题目ID
     * @return 测试用例列表
     */
    List<TestCase> getTestCasesByProblemId(Long problemId);
}
