package com.emiyaoj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;

import java.util.List;

public interface IProblemService extends IService<Problem> {
    /**
     * 分页查询题目
     */
    PageVO<ProblemVO> page(ProblemQueryDTO queryDTO);
    
    /**
     * 获取题目详情
     */
    ProblemVO getById(Long id);
    
    /**
     * 创建题目
     */
    Long save(ProblemSaveDTO saveDTO);
    
    /**
     * 更新题目
     */
    void update(Long id, ProblemSaveDTO saveDTO);
    
    /**
     * 删除题目
     */
    void delete(Long id);
    
    /**
     * 批量删除题目
     */
    void deleteBatch(List<Long> ids);
}
