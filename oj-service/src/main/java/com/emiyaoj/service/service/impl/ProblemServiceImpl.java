package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.IProblemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目Service实现
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    @Override
    public void incrementAcceptCount(Long problemId) {
        Problem problem = this.getById(problemId);
        if (problem != null) {
            problem.setAcceptCount(problem.getAcceptCount() + 1);
            this.updateById(problem);
        }
    }

    @Override
    public void incrementSubmitCount(Long problemId) {
        Problem problem = this.getById(problemId);
        if (problem != null) {
            problem.setSubmitCount(problem.getSubmitCount() + 1);
            this.updateById(problem);
        }
    }
    
    @Override
    public PageVO<ProblemVO> selectProblemPage(PageDTO pageDTO, Integer difficulty, Integer status, String keyword) {
        Page<Problem> page = pageDTO.toMpPageDefaultSortByCreateTimeDesc();
        
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getDeleted, 0);
        if (difficulty != null) {
            wrapper.eq(Problem::getDifficulty, difficulty);
        }
        if (status != null) {
            wrapper.eq(Problem::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Problem::getTitle, keyword)
                    .or()
                    .like(Problem::getDescription, keyword));
        }
        
        Page<Problem> problemPage = this.page(page, wrapper);
        
        return PageVO.of(problemPage, problem -> {
            ProblemVO vo = new ProblemVO();
            BeanUtils.copyProperties(problem, vo);
            return vo;
        });
    }
    
    @Override
    public ProblemVO selectProblemById(Long id) {
        Problem problem = this.getById(id);
        if (problem == null || problem.getDeleted() == 1) {
            return null;
        }
        ProblemVO vo = new ProblemVO();
        BeanUtils.copyProperties(problem, vo);
        return vo;
    }
    
    @Override
    public boolean saveProblem(ProblemSaveDTO saveDTO) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(saveDTO, problem);
        
        // 设置默认值
        problem.setAcceptCount(0);
        problem.setSubmitCount(0);
        problem.setDeleted(0);
        if (problem.getDifficulty() == null) {
            problem.setDifficulty(1);
        }
        if (problem.getStackLimit() == null) {
            problem.setStackLimit(128);
        }
        if (problem.getStatus() == null) {
            problem.setStatus(1);
        }
        
        // 设置创建信息
        Long currentUserId = BaseContext.getCurrentId();
        problem.setAuthorId(currentUserId);
        problem.setCreateBy(currentUserId);
        problem.setUpdateBy(currentUserId);
        problem.setCreateTime(LocalDateTime.now());
        problem.setUpdateTime(LocalDateTime.now());
        
        return this.save(problem);
    }
    
    @Override
    public boolean updateProblem(ProblemSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return false;
        }
        
        Problem existingProblem = this.getById(saveDTO.getId());
        if (existingProblem == null || existingProblem.getDeleted() == 1) {
            return false;
        }
        
        Problem problem = new Problem();
        BeanUtils.copyProperties(saveDTO, problem);
        
        // 设置更新信息
        problem.setUpdateBy(BaseContext.getCurrentId());
        problem.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(problem);
    }
    
    @Override
    public boolean deleteProblem(Long id) {
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Problem::getId, id);
        wrapper.set(Problem::getDeleted, 1);
        wrapper.set(Problem::getUpdateTime, LocalDateTime.now());
        wrapper.set(Problem::getUpdateBy, BaseContext.getCurrentId());
        return this.update(wrapper);
    }
    
    @Override
    public boolean deleteProblems(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Problem::getId, ids);
        wrapper.set(Problem::getDeleted, 1);
        wrapper.set(Problem::getUpdateTime, LocalDateTime.now());
        wrapper.set(Problem::getUpdateBy, BaseContext.getCurrentId());
        return this.update(wrapper);
    }
    
    @Override
    public boolean updateProblemStatus(Long id, Integer status) {
        LambdaUpdateWrapper<Problem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Problem::getId, id);
        wrapper.set(Problem::getStatus, status);
        wrapper.set(Problem::getUpdateTime, LocalDateTime.now());
        wrapper.set(Problem::getUpdateBy, BaseContext.getCurrentId());
        return this.update(wrapper);
    }
}
