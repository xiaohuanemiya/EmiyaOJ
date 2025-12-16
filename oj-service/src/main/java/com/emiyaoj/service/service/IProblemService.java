package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;

import java.util.List;

/**
 * 题目服务接口
 */
public interface IProblemService extends IService<Problem> {
    
    /**
     * 分页查询题目列表
     */
    PageVO<ProblemVO> getProblemPage(ProblemQueryDTO queryDTO);
    
    /**
     * 根据ID查询题目详情
     */
    ProblemVO getProblemById(Long id);
    
    /**
     * 新增题目
     */
    boolean saveProblem(ProblemSaveDTO saveDTO);
    
    /**
     * 修改题目
     */
    boolean updateProblem(ProblemSaveDTO saveDTO);
    
    /**
     * 删除题目（逻辑删除）
     */
    boolean deleteProblem(Long id);
    
    /**
     * 批量删除题目（逻辑删除）
     */
    boolean deleteProblems(List<Long> ids);
    
    /**
     * 修改题目状态
     */
    boolean updateProblemStatus(Long id, Integer status);
}

