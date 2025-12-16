package com.emiyaoj.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.stu.domain.dto.SubmissionDTO;
import com.emiyaoj.stu.domain.pojo.Language;
import com.emiyaoj.stu.domain.pojo.Problem;
import com.emiyaoj.stu.domain.pojo.Submission;
import com.emiyaoj.stu.domain.vo.SubmissionVO;
import com.emiyaoj.stu.mapper.LanguageMapper;
import com.emiyaoj.stu.mapper.ProblemMapper;
import com.emiyaoj.stu.mapper.SubmissionMapper;
import com.emiyaoj.stu.service.IStudentSubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 学生提交服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentSubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements IStudentSubmissionService {
    
    private final ProblemMapper problemMapper;
    private final LanguageMapper languageMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubmissionVO submitCode(SubmissionDTO submissionDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("用户ID：{}，提交代码，题目ID：{}", userId, submissionDTO.getProblemId());
        
        // 1. 验证题目是否存在且公开
        Problem problem = problemMapper.selectById(submissionDTO.getProblemId());
        if (problem == null || problem.getDeleted() == 1 || problem.getStatus() == 0) {
            throw new RuntimeException("题目不存在或未公开");
        }
        
        // 2. 验证语言是否存在且启用
        Language language = languageMapper.selectById(submissionDTO.getLanguageId());
        if (language == null || language.getStatus() == 0) {
            throw new RuntimeException("编程语言不存在或未启用");
        }
        
        // 3. 创建提交记录
        Submission submission = new Submission();
        submission.setProblemId(submissionDTO.getProblemId());
        submission.setUserId(userId);
        submission.setLanguageId(submissionDTO.getLanguageId());
        submission.setCode(submissionDTO.getCode());
        submission.setStatus("Pending");
        submission.setScore(0);
        submission.setTimeUsed(0);
        submission.setMemoryUsed(0);
        submission.setCreateTime(LocalDateTime.now());
        submission.setUpdateTime(LocalDateTime.now());
        
        this.save(submission);
        
        // 4. 更新题目提交次数
        problem.setSubmitCount((problem.getSubmitCount() == null ? 0 : problem.getSubmitCount()) + 1);
        problemMapper.updateById(problem);
        
        // 5. 返回提交记录
        SubmissionVO vo = convertToVO(submission);
        vo.setLanguageName(language.getName());
        
        // TODO: 这里应该调用判题服务进行判题，目前先返回Pending状态
        log.info("提交记录已创建，ID：{}，等待判题", submission.getId());
        
        return vo;
    }
    
    @Override
    public PageVO<SubmissionVO> getSubmissionPage(Integer pageNo, Integer pageSize, Long problemId) {
        Page<Submission> page = Page.of(pageNo, pageSize);
        
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getUserId, BaseContext.getCurrentId());
        
        if (problemId != null) {
            wrapper.eq(Submission::getProblemId, problemId);
        }
        
        wrapper.orderByDesc(Submission::getCreateTime);
        
        page(page, wrapper);
        
        return PageVO.of(page, submission -> {
            SubmissionVO vo = convertToVO(submission);
            // 查询语言名称
            Language language = languageMapper.selectById(submission.getLanguageId());
            if (language != null) {
                vo.setLanguageName(language.getName());
            }
            return vo;
        });
    }
    
    @Override
    public SubmissionVO getSubmissionById(Long id) {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getId, id)
               .eq(Submission::getUserId, BaseContext.getCurrentId());
        
        Submission submission = this.getOne(wrapper);
        if (submission == null) {
            throw new RuntimeException("提交记录不存在");
        }
        
        SubmissionVO vo = convertToVO(submission);
        // 查询语言名称
        Language language = languageMapper.selectById(submission.getLanguageId());
        if (language != null) {
            vo.setLanguageName(language.getName());
        }
        
        return vo;
    }
    
    private SubmissionVO convertToVO(Submission submission) {
        SubmissionVO vo = new SubmissionVO();
        BeanUtils.copyProperties(submission, vo);
        return vo;
    }
}

