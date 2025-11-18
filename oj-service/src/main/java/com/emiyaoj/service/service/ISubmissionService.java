package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.vo.SubmissionVO;

import java.util.List;

public interface ISubmissionService extends IService<Submission> {
    /**
     * 提交代码
     */
    Long submit(SubmitCodeDTO submitDTO);
    
    /**
     * 获取提交详情
     */
    SubmissionVO getById(Long id);
    
    /**
     * 获取用户提交列表
     */
    List<SubmissionVO> getUserSubmissions(Long userId);
    
    /**
     * 获取题目提交列表
     */
    List<SubmissionVO> getProblemSubmissions(Long problemId);
}
