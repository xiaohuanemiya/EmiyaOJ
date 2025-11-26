package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;

import java.util.List;

/**
 * 题目Service
 */
public interface IProblemService extends IService<Problem> {
    
    /**
     * 增加通过次数
     * @param problemId 题目ID
     */
    void incrementAcceptCount(Long problemId);
    
    /**
     * 增加提交次数
     * @param problemId 题目ID
     */
    void incrementSubmitCount(Long problemId);
    
    /**
     * 分页查询题目
     * @param pageDTO 分页参数
     * @param difficulty 难度筛选（可选）
     * @param status 状态筛选（可选）
     * @param keyword 关键词搜索（可选）
     * @return 分页结果
     */
    PageVO<ProblemVO> selectProblemPage(PageDTO pageDTO, Integer difficulty, Integer status, String keyword);
    
    /**
     * 根据ID查询题目详情
     * @param id 题目ID
     * @return 题目详情
     */
    ProblemVO selectProblemById(Long id);
    
    /**
     * 新增题目
     * @param saveDTO 题目信息
     * @return 是否成功
     */
    boolean saveProblem(ProblemSaveDTO saveDTO);
    
    /**
     * 更新题目
     * @param saveDTO 题目信息
     * @return 是否成功
     */
    boolean updateProblem(ProblemSaveDTO saveDTO);
    
    /**
     * 删除题目（逻辑删除）
     * @param id 题目ID
     * @return 是否成功
     */
    boolean deleteProblem(Long id);
    
    /**
     * 批量删除题目（逻辑删除）
     * @param ids 题目ID列表
     * @return 是否成功
     */
    boolean deleteProblems(List<Long> ids);
    
    /**
     * 更新题目状态
     * @param id 题目ID
     * @param status 状态：0-隐藏，1-公开
     * @return 是否成功
     */
    boolean updateProblemStatus(Long id, Integer status);
}
