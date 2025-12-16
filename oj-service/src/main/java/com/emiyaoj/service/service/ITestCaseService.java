package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.vo.TestCaseVO;

import java.util.List;

/**
 * 测试用例服务接口
 */
public interface ITestCaseService extends IService<TestCase> {
    
    /**
     * 根据题目ID查询测试用例列表
     */
    List<TestCaseVO> getTestCasesByProblemId(Long problemId);
    
    /**
     * 新增测试用例
     */
    boolean saveTestCase(TestCaseSaveDTO saveDTO);
    
    /**
     * 修改测试用例
     */
    boolean updateTestCase(TestCaseSaveDTO saveDTO);
    
    /**
     * 删除测试用例（逻辑删除）
     */
    boolean deleteTestCase(Long id);
    
    /**
     * 批量删除测试用例（逻辑删除）
     */
    boolean deleteTestCases(List<Long> ids);
}

