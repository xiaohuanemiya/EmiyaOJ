package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.oj.ProblemVO;

/**
 * 题目服务接口
 */
public interface IProblemService extends IService<Problem> {
    
    /**
     * 分页查询公开题目
     * 
     * @param page 页码
     * @param size 每页大小
     * @param difficulty 难度筛选（可选）
     * @return 题目分页
     */
    Page<Problem> listPublicProblems(int page, int size, Integer difficulty);
    
    /**
     * 分页查询公开题目（返回VO）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param difficulty 难度筛选（可选）
     * @return 题目VO分页
     */
    PageVO<ProblemVO> listPublicProblemsVO(int page, int size, Integer difficulty);
    
    /**
     * 获取题目详情VO
     * 
     * @param id 题目ID
     * @return 题目详情VO
     */
    ProblemVO getProblemVO(Long id);
    
    /**
     * 增加题目提交次数
     * 
     * @param problemId 题目ID
     */
    void increaseSubmitCount(Long problemId);
    
    /**
     * 增加题目通过次数
     * 
     * @param problemId 题目ID
     */
    void increaseAcceptCount(Long problemId);
}
