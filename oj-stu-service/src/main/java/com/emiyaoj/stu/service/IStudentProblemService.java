package com.emiyaoj.stu.service;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.stu.domain.dto.ProblemQueryDTO;
import com.emiyaoj.stu.domain.vo.LanguageVO;
import com.emiyaoj.stu.domain.vo.ProblemVO;

import java.util.List;

/**
 * 学生题目服务接口
 */
public interface IStudentProblemService {
    
    /**
     * 分页查询题目列表（仅公开题目）
     */
    PageVO<ProblemVO> getProblemPage(ProblemQueryDTO queryDTO);
    
    /**
     * 根据ID查询题目详情
     */
    ProblemVO getProblemById(Long id);
    
    /**
     * 获取所有启用的编程语言
     */
    List<LanguageVO> getAvailableLanguages();
}

