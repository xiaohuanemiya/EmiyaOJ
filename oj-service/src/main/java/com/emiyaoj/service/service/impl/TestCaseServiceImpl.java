package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.vo.TestCaseVO;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.ITestCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试用例Service实现
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {

    @Override
    public List<TestCase> getTestCasesByProblemId(Long problemId) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId);
        wrapper.eq(TestCase::getDeleted, 0);
        wrapper.orderByAsc(TestCase::getSortOrder);
        return this.list(wrapper);
    }
    
    @Override
    public List<TestCaseVO> selectTestCasesByProblemId(Long problemId) {
        List<TestCase> testCases = getTestCasesByProblemId(problemId);
        return testCases.stream()
                .map(testCase -> {
                    TestCaseVO vo = new TestCaseVO();
                    BeanUtils.copyProperties(testCase, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public TestCaseVO selectTestCaseById(Long id) {
        TestCase testCase = this.getById(id);
        if (testCase == null || testCase.getDeleted() == 1) {
            return null;
        }
        TestCaseVO vo = new TestCaseVO();
        BeanUtils.copyProperties(testCase, vo);
        return vo;
    }
    
    @Override
    public boolean saveTestCase(TestCaseSaveDTO saveDTO) {
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(saveDTO, testCase);
        
        // 设置默认值
        testCase.setDeleted(0);
        if (testCase.getIsSample() == null) {
            testCase.setIsSample(0);
        }
        if (testCase.getScore() == null) {
            testCase.setScore(0);
        }
        if (testCase.getSortOrder() == null) {
            testCase.setSortOrder(0);
        }
        testCase.setCreateTime(LocalDateTime.now());
        testCase.setUpdateTime(LocalDateTime.now());
        
        return this.save(testCase);
    }
    
    @Override
    public boolean updateTestCase(TestCaseSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return false;
        }
        
        TestCase existingTestCase = this.getById(saveDTO.getId());
        if (existingTestCase == null || existingTestCase.getDeleted() == 1) {
            return false;
        }
        
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(saveDTO, testCase);
        testCase.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(testCase);
    }
    
    @Override
    public boolean deleteTestCase(Long id) {
        LambdaUpdateWrapper<TestCase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TestCase::getId, id);
        wrapper.set(TestCase::getDeleted, 1);
        wrapper.set(TestCase::getUpdateTime, LocalDateTime.now());
        return this.update(wrapper);
    }
    
    @Override
    public boolean deleteTestCases(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<TestCase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(TestCase::getId, ids);
        wrapper.set(TestCase::getDeleted, 1);
        wrapper.set(TestCase::getUpdateTime, LocalDateTime.now());
        return this.update(wrapper);
    }
    
    @Override
    public boolean deleteTestCasesByProblemId(Long problemId) {
        LambdaUpdateWrapper<TestCase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId);
        wrapper.set(TestCase::getDeleted, 1);
        wrapper.set(TestCase::getUpdateTime, LocalDateTime.now());
        return this.update(wrapper);
    }
}
