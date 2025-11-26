package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.vo.TestCaseVO;

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
    
    /**
     * 根据题目ID获取测试用例VO列表
     * @param problemId 题目ID
     * @return 测试用例VO列表
     */
    List<TestCaseVO> selectTestCasesByProblemId(Long problemId);
    
    /**
     * 根据ID查询测试用例详情
     * @param id 测试用例ID
     * @return 测试用例详情
     */
    TestCaseVO selectTestCaseById(Long id);
    
    /**
     * 新增测试用例
     * @param saveDTO 测试用例信息
     * @return 是否成功
     */
    boolean saveTestCase(TestCaseSaveDTO saveDTO);
    
    /**
     * 更新测试用例
     * @param saveDTO 测试用例信息
     * @return 是否成功
     */
    boolean updateTestCase(TestCaseSaveDTO saveDTO);
    
    /**
     * 删除测试用例（逻辑删除）
     * @param id 测试用例ID
     * @return 是否成功
     */
    boolean deleteTestCase(Long id);
    
    /**
     * 批量删除测试用例（逻辑删除）
     * @param ids 测试用例ID列表
     * @return 是否成功
     */
    boolean deleteTestCases(List<Long> ids);
    
    /**
     * 根据题目ID删除所有测试用例（逻辑删除）
     * @param problemId 题目ID
     * @return 是否成功
     */
    boolean deleteTestCasesByProblemId(Long problemId);
}
