package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.ITestCaseService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
