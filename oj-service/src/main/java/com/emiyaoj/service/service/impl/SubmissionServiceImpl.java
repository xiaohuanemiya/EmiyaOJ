package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.constant.JudgeStatus;
import com.emiyaoj.service.domain.dto.oj.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.vo.oj.SubmissionDetailVO;
import com.emiyaoj.service.domain.vo.oj.SubmissionVO;
import com.emiyaoj.service.mapper.SubmissionMapper;
import com.emiyaoj.service.service.IJudgeService;
import com.emiyaoj.service.service.ILanguageService;
import com.emiyaoj.service.service.IProblemService;
import com.emiyaoj.service.service.ISubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提交服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {
    
    private final IJudgeService judgeService;
    private final IProblemService problemService;
    private final ILanguageService languageService;
    
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
    
    @Override
    public Map<String, Object> submitCode(SubmitCodeDTO submitCodeDTO, Long userId, String ipAddress) {
        // 验证题目是否存在
        Problem problem = problemService.getById(submitCodeDTO.getProblemId());
        if (problem == null) {
            throw new IllegalArgumentException("题目不存在");
        }
        
        // 验证语言是否存在
        Language language = languageService.getById(submitCodeDTO.getLanguageId());
        if (language == null) {
            throw new IllegalArgumentException("不支持的编程语言");
        }
        
        // 创建提交记录
        Submission submission = Submission.builder()
                .problemId(submitCodeDTO.getProblemId())
                .userId(userId)
                .languageId(submitCodeDTO.getLanguageId())
                .code(submitCodeDTO.getCode())
                .ipAddress(ipAddress)
                .build();
        
        // 保存并开始判题
        Long submissionId = createAndJudge(submission);
        
        // 增加题目提交次数
        problemService.increaseSubmitCount(problem.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("submissionId", submissionId);
        result.put("message", "提交成功，正在判题中...");
        
        return result;
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
    public PageVO<SubmissionVO> listSubmissionsVO(int page, int size, Long userId, Long problemId) {
        Page<Submission> submissionPage = listSubmissions(page, size, userId, problemId);
        
        List<SubmissionVO> submissionVOS = submissionPage.getRecords().stream()
                .map(submission -> {
                    Problem problem = problemService.getById(submission.getProblemId());
                    Language language = languageService.getById(submission.getLanguageId());
                    
                    return SubmissionVO.builder()
                            .id(submission.getId())
                            .problemId(submission.getProblemId())
                            .problemTitle(problem != null ? problem.getTitle() : "")
                            .userId(submission.getUserId())
                            .languageId(submission.getLanguageId())
                            .languageName(language != null ? language.getName() + " " + language.getVersion() : "")
                            .status(submission.getStatus())
                            .score(submission.getScore())
                            .timeUsed(submission.getTimeUsed())
                            .memoryUsed(submission.getMemoryUsed())
                            .passRate(submission.getPassRate())
                            .createTime(submission.getCreateTime())
                            .build();
                })
                .collect(Collectors.toList());
        
        return new PageVO<>(
                submissionPage.getTotal(),
                submissionPage.getPages(),
                submissionVOS
        );
    }
    
    @Override
    public SubmissionDetailVO getSubmissionDetailVO(Long id) {
        Submission submission = getById(id);
        if (submission == null) {
            return null;
        }
        
        Problem problem = problemService.getById(submission.getProblemId());
        Language language = languageService.getById(submission.getLanguageId());
        
        return SubmissionDetailVO.builder()
                .id(submission.getId())
                .problemId(submission.getProblemId())
                .problemTitle(problem != null ? problem.getTitle() : "")
                .userId(submission.getUserId())
                .languageId(submission.getLanguageId())
                .languageName(language != null ? language.getName() + " " + language.getVersion() : "")
                .code(submission.getCode())
                .status(submission.getStatus())
                .score(submission.getScore())
                .timeUsed(submission.getTimeUsed())
                .memoryUsed(submission.getMemoryUsed())
                .errorMessage(submission.getErrorMessage())
                .compileMessage(submission.getCompileMessage())
                .passRate(submission.getPassRate())
                .createTime(submission.getCreateTime())
                .build();
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
