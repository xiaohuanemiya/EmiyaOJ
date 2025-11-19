package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.oj.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.vo.oj.SubmissionDetailVO;
import com.emiyaoj.service.domain.vo.oj.SubmissionVO;

import java.util.Map;

/**
 * 提交服务接口
 */
public interface ISubmissionService extends IService<Submission> {
    
    /**
     * 创建提交记录（不触发判题）
     * 
     * @param submission 提交信息
     * @return 提交ID
     */
    Long createSubmission(Submission submission);
    
    /**
     * 提交代码
     * 
     * @param submitCodeDTO 提交代码DTO
     * @param userId 用户ID
     * @param ipAddress IP地址
     * @return 提交结果
     */
    Map<String, Object> submitCode(SubmitCodeDTO submitCodeDTO, Long userId, String ipAddress);
    
    /**
     * 分页查询用户提交记录
     * 
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param problemId 题目ID（可选）
     * @return 提交分页
     */
    Page<Submission> listSubmissions(int page, int size, Long userId, Long problemId);
    
    /**
     * 分页查询提交记录（返回VO）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户ID（可选）
     * @param problemId 题目ID（可选）
     * @return 提交VO分页
     */
    PageVO<SubmissionVO> listSubmissionsVO(int page, int size, Long userId, Long problemId);
    
    /**
     * 获取提交详情VO
     * 
     * @param id 提交ID
     * @return 提交详情VO
     */
    SubmissionDetailVO getSubmissionDetailVO(Long id);
    
    /**
     * 更新提交状态
     * 
     * @param submissionId 提交ID
     * @param status 状态
     * @param timeUsed 时间使用（毫秒）
     * @param memoryUsed 内存使用（KB）
     * @param errorMessage 错误信息
     * @param compileMessage 编译信息
     * @param passRate 通过率
     * @param score 得分
     */
    void updateStatus(Long submissionId, String status, Integer timeUsed, Integer memoryUsed, 
                     String errorMessage, String compileMessage, String passRate, Integer score);
}
