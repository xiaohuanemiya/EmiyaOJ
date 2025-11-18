package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.service.domain.vo.SubmissionVO;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.mapper.SubmissionMapper;
import com.emiyaoj.service.mapper.UserMapper;
import com.emiyaoj.service.service.IJudgeService;
import com.emiyaoj.service.service.ISubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {
    
    private final SubmissionMapper submissionMapper;
    private final ProblemMapper problemMapper;
    private final UserMapper userMapper;
    private final IJudgeService judgeService;
    
    @Override
    public Long submit(SubmitCodeDTO submitDTO) {
        // 验证题目存在
        Problem problem = problemMapper.selectById(submitDTO.getProblemId());
        if (problem == null || problem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        
        // 创建提交记录
        Submission submission = new Submission();
        submission.setProblemId(submitDTO.getProblemId());
        submission.setUserId(BaseContext.getCurrentId());
        submission.setLanguage(submitDTO.getLanguage());
        submission.setCode(submitDTO.getCode());
        submission.setStatus("Pending");
        submission.setTimeUsed(0L);
        submission.setMemoryUsed(0L);
        submission.setCreateTime(LocalDateTime.now());
        
        submissionMapper.insert(submission);
        
        // 异步判题
        judgeAsync(submission);
        
        return submission.getId();
    }
    
    @Async
    protected void judgeAsync(Submission submission) {
        try {
            judgeService.judge(submission);
        } catch (Exception e) {
            log.error("异步判题失败", e);
        }
    }
    
    @Override
    public SubmissionVO getById(Long id) {
        Submission submission = submissionMapper.selectById(id);
        if (submission == null) {
            throw new RuntimeException("提交记录不存在");
        }
        return convertToVO(submission);
    }
    
    @Override
    public List<SubmissionVO> getUserSubmissions(Long userId) {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getUserId, userId)
               .orderByDesc(Submission::getCreateTime);
        
        List<Submission> submissions = submissionMapper.selectList(wrapper);
        return submissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SubmissionVO> getProblemSubmissions(Long problemId) {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getProblemId, problemId)
               .orderByDesc(Submission::getCreateTime)
               .last("LIMIT 100");
        
        List<Submission> submissions = submissionMapper.selectList(wrapper);
        return submissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    private SubmissionVO convertToVO(Submission submission) {
        SubmissionVO vo = new SubmissionVO();
        org.springframework.beans.BeanUtils.copyProperties(submission, vo);
        
        // 获取题目标题
        Problem problem = problemMapper.selectById(submission.getProblemId());
        if (problem != null) {
            vo.setProblemTitle(problem.getTitle());
        }
        
        // 获取用户名
        User user = userMapper.selectById(submission.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        
        return vo;
    }
}
