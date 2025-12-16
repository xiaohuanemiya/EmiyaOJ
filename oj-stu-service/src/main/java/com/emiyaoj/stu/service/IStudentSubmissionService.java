package com.emiyaoj.stu.service;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.stu.domain.dto.SubmissionDTO;
import com.emiyaoj.stu.domain.vo.SubmissionVO;

/**
 * 学生提交服务接口
 */
public interface IStudentSubmissionService {
    
    /**
     * 提交代码
     */
    SubmissionVO submitCode(SubmissionDTO submissionDTO);
    
    /**
     * 查询提交记录（分页）
     */
    PageVO<SubmissionVO> getSubmissionPage(Integer pageNo, Integer pageSize, Long problemId);
    
    /**
     * 根据ID查询提交详情
     */
    SubmissionVO getSubmissionById(Long id);
}

