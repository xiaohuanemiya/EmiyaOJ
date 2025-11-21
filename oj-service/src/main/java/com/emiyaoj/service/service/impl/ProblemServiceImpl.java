package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.IProblemService;
import org.springframework.stereotype.Service;

/**
 * 题目Service实现
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Override
    public void incrementAcceptCount(Long problemId) {
        Problem problem = this.getById(problemId);
        if (problem != null) {
            problem.setAcceptCount(problem.getAcceptCount() + 1);
            this.updateById(problem);
        }
    }

    @Override
    public void incrementSubmitCount(Long problemId) {
        Problem problem = this.getById(problemId);
        if (problem != null) {
            problem.setSubmitCount(problem.getSubmitCount() + 1);
            this.updateById(problem);
        }
    }
}
