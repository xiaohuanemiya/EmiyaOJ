package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.IProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {
    
    private final ProblemMapper problemMapper;
    
    @Override
    public PageVO<ProblemVO> page(ProblemQueryDTO queryDTO) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getDeleted, 0)
               .eq(Problem::getStatus, 1);
        
        if (queryDTO.getTitle() != null && !queryDTO.getTitle().isEmpty()) {
            wrapper.like(Problem::getTitle, queryDTO.getTitle());
        }
        
        if (queryDTO.getDifficulty() != null) {
            wrapper.eq(Problem::getDifficulty, queryDTO.getDifficulty());
        }
        
        if (queryDTO.getTags() != null && !queryDTO.getTags().isEmpty()) {
            wrapper.like(Problem::getTags, queryDTO.getTags());
        }
        
        wrapper.orderByDesc(Problem::getCreateTime);
        
        Page<Problem> mpPage = queryDTO.toMpPageDefaultSortByCreateTimeDesc();
        Page<Problem> result = problemMapper.selectPage(mpPage, wrapper);
        
        return PageVO.of(result, this::convertToVO);
    }
    
    @Override
    public ProblemVO getById(Long id) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null || problem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        return convertToVO(problem);
    }
    
    @Override
    public Long save(ProblemSaveDTO saveDTO) {
        Problem problem = new Problem();
        org.springframework.beans.BeanUtils.copyProperties(saveDTO, problem);
        problem.setAcceptedCount(0);
        problem.setSubmitCount(0);
        problem.setStatus(1);
        problem.setDeleted(0);
        problemMapper.insert(problem);
        return problem.getId();
    }
    
    @Override
    public void update(Long id, ProblemSaveDTO saveDTO) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null || problem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        org.springframework.beans.BeanUtils.copyProperties(saveDTO, problem);
        problemMapper.updateById(problem);
    }
    
    @Override
    public void delete(Long id) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null) {
            throw new RuntimeException("题目不存在");
        }
        problem.setDeleted(1);
        problemMapper.updateById(problem);
    }
    
    @Override
    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::delete);
    }
    
    private ProblemVO convertToVO(Problem problem) {
        ProblemVO vo = new ProblemVO();
        org.springframework.beans.BeanUtils.copyProperties(problem, vo);
        
        // 设置难度描述
        switch (problem.getDifficulty()) {
            case 1 -> vo.setDifficultyDesc("简单");
            case 2 -> vo.setDifficultyDesc("中等");
            case 3 -> vo.setDifficultyDesc("困难");
            default -> vo.setDifficultyDesc("未知");
        }
        
        // 计算通过率
        if (problem.getSubmitCount() > 0) {
            double rate = (double) problem.getAcceptedCount() / problem.getSubmitCount() * 100;
            vo.setAcceptRate(Math.round(rate * 100.0) / 100.0);
        } else {
            vo.setAcceptRate(0.0);
        }
        
        return vo;
    }
}
