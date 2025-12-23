package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.vo.SubmissionVO;

/**
 * 提交记录Service
 */
public interface ISubmissionService extends IService<Submission> {
    
    /**
     * 提交代码
     * @param submitCodeDTO 提交代码DTO
     * @return 提交ID
     */
    Long submitCode(SubmitCodeDTO submitCodeDTO);
    
    /**
     * 分页查询提交记录
     * @param pageDTO 分页参数
     * @param problemId 题目ID（可选）
     * @param userId 用户ID（可选）
     * @return 提交记录列表
     */
    PageVO<SubmissionVO> pageSubmissions(PageDTO pageDTO, Long problemId, Long userId);
    
    /**
     * 获取提交详情
     * @param id 提交ID
     * @return 提交详情
     */
    SubmissionVO getSubmissionDetail(Long id);
}
