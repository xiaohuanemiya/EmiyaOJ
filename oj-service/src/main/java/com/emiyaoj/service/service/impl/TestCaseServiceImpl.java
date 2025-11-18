package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.ITestCaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试用例服务实现类
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {
    
    @Override
    public List<TestCase> listByProblemId(Long problemId) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId)
                .eq(TestCase::getDeleted, 0)
                .orderByAsc(TestCase::getSortOrder);
        return list(wrapper);
    }
    
    @Override
    public List<TestCase> listNonSampleByProblemId(Long problemId) {
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId)
                .eq(TestCase::getIsSample, 0)
                .eq(TestCase::getDeleted, 0)
                .orderByAsc(TestCase::getSortOrder);
        return list(wrapper);
    }
}
