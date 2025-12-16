package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.vo.TestCaseVO;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.ITestCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试用例服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {
    
    @Override
    public List<TestCaseVO> getTestCasesByProblemId(Long problemId) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId)
               .eq(TestCase::getDeleted, 0)
               .orderByAsc(TestCase::getSortOrder);
        
        List<TestCase> testCases = this.list(wrapper);
        
        return testCases.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTestCase(TestCaseSaveDTO saveDTO) {
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(saveDTO, testCase);
        
        testCase.setIsSample(saveDTO.getIsSample() != null ? saveDTO.getIsSample() : 0);
        testCase.setScore(saveDTO.getScore() != null ? saveDTO.getScore() : 10);
        testCase.setSortOrder(saveDTO.getSortOrder() != null ? saveDTO.getSortOrder() : 1);
        testCase.setDeleted(0);
        testCase.setCreateTime(LocalDateTime.now());
        testCase.setUpdateTime(LocalDateTime.now());
        
        return this.save(testCase);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTestCase(TestCaseSaveDTO saveDTO) {
        TestCase existTestCase = this.getById(saveDTO.getId());
        if (existTestCase == null || existTestCase.getDeleted() == 1) {
            throw new RuntimeException("测试用例不存在");
        }
        
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(saveDTO, testCase);
        testCase.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(testCase);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTestCase(Long id) {
        TestCase testCase = this.getById(id);
        if (testCase == null || testCase.getDeleted() == 1) {
            throw new RuntimeException("测试用例不存在");
        }
        
        testCase.setDeleted(1);
        testCase.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(testCase);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTestCases(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        
        List<TestCase> testCases = this.listByIds(ids);
        if (testCases.isEmpty()) {
            throw new RuntimeException("测试用例不存在");
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        for (TestCase testCase : testCases) {
            if (testCase.getDeleted() == 0) {
                testCase.setDeleted(1);
                testCase.setUpdateTime(now);
            }
        }
        
        return this.updateBatchById(testCases);
    }
    
    private TestCaseVO convertToVO(TestCase testCase) {
        TestCaseVO vo = new TestCaseVO();
        BeanUtils.copyProperties(testCase, vo);
        return vo;
    }
}

