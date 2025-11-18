package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.service.domain.constant.JudgeStatus;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.mapper.SubmissionMapper;
import com.emiyaoj.service.service.IJudgeService;
import com.emiyaoj.service.service.ISubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 提交服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {
    
    private final IJudgeService judgeService;
    
    @Override
    public Long createAndJudge(Submission submission) {
        // 设置初始状态
        submission.setStatus(JudgeStatus.PENDING.getValue());
        submission.setScore(0);
        submission.setTimeUsed(0);
        submission.setMemoryUsed(0);
        
        // 保存提交记录
        save(submission);
        
        // 异步执行判题
        judgeAsync(submission.getId());
        
        return submission.getId();
    }
    
    @Async
    public void judgeAsync(Long submissionId) {
        try {
            judgeService.judge(submissionId);
        } catch (Exception e) {
            log.error("Judge failed for submission: {}", submissionId, e);
            updateStatus(submissionId, JudgeStatus.SYSTEM_ERROR.getValue(), 0, 0, 
                    e.getMessage(), null, "0/0", 0);
        }
    }
    
    @Override
    public Page<Submission> listSubmissions(int page, int size, Long userId, Long problemId) {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(Submission::getUserId, userId);
        }
        
        if (problemId != null) {
            wrapper.eq(Submission::getProblemId, problemId);
        }
        
        wrapper.orderByDesc(Submission::getCreateTime);
        
        return page(new Page<>(page, size), wrapper);
    }
    
    @Override
    public void updateStatus(Long submissionId, String status, Integer timeUsed, Integer memoryUsed,
                            String errorMessage, String compileMessage, String passRate, Integer score) {
        LambdaUpdateWrapper<Submission> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Submission::getId, submissionId)
                .set(Submission::getStatus, status)
                .set(Submission::getTimeUsed, timeUsed)
                .set(Submission::getMemoryUsed, memoryUsed)
                .set(Submission::getScore, score)
                .set(Submission::getPassRate, passRate);
        
        if (errorMessage != null) {
            wrapper.set(Submission::getErrorMessage, errorMessage);
        }
        
        if (compileMessage != null) {
            wrapper.set(Submission::getCompileMessage, compileMessage);
        }
        
        update(wrapper);
    }
}
