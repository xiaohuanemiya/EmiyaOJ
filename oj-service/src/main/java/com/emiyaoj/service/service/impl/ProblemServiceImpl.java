package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.IProblemService;
import org.springframework.stereotype.Service;

/**
 * 题目服务实现类
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {
    
    @Override
    public Page<Problem> listPublicProblems(int page, int size, Integer difficulty) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getStatus, 1)
                .eq(Problem::getDeleted, 0);
        
        if (difficulty != null) {
            wrapper.eq(Problem::getDifficulty, difficulty);
        }
        
        wrapper.orderByDesc(Problem::getCreateTime);
        
        return page(new Page<>(page, size), wrapper);
    }
    
    @Override
    public void increaseSubmitCount(Long problemId) {
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Problem::getId, problemId)
                .setSql("submit_count = submit_count + 1");
        update(wrapper);
    }
    
    @Override
    public void increaseAcceptCount(Long problemId) {
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Problem::getId, problemId)
                .setSql("accept_count = accept_count + 1");
        update(wrapper);
    }
}
