package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.service.IProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {
    
    @Override
    public PageVO<ProblemVO> getProblemPage(ProblemQueryDTO queryDTO) {
        Page<Problem> page = queryDTO.toMpPageDefaultSortByCreateTimeDesc();
        
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getDeleted, 0);
        
        // 难度筛选
        if (queryDTO.getDifficulty() != null) {
            wrapper.eq(Problem::getDifficulty, queryDTO.getDifficulty());
        }
        
        // 状态筛选
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Problem::getStatus, queryDTO.getStatus());
        }
        
        // 关键词搜索（题目标题）
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.like(Problem::getTitle, queryDTO.getKeyword());
        }
        
        page(page, wrapper);
        
        return PageVO.of(page, this::convertToVO);
    }
    
    @Override
    public ProblemVO getProblemById(Long id) {
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getId, id)
               .eq(Problem::getDeleted, 0);
        
        Problem problem = this.getOne(wrapper);
        if (problem == null) {
            throw new RuntimeException("题目不存在");
        }
        
        return convertToVO(problem);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveProblem(ProblemSaveDTO saveDTO) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(saveDTO, problem);
        
        problem.setId(IdWorker.getId());
        problem.setAcceptCount(0);
        problem.setSubmitCount(0);
        problem.setDeleted(0);
        problem.setStatus(saveDTO.getStatus() != null ? saveDTO.getStatus() : 1);
        problem.setDifficulty(saveDTO.getDifficulty() != null ? saveDTO.getDifficulty() : 1);
        problem.setStackLimit(saveDTO.getStackLimit() != null ? saveDTO.getStackLimit() : 128);
        problem.setCreateTime(LocalDateTime.now());
        problem.setUpdateTime(LocalDateTime.now());
        problem.setCreateBy(BaseContext.getCurrentId());
        
        return this.save(problem);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProblem(ProblemSaveDTO saveDTO) {
        Problem existProblem = this.getById(saveDTO.getId());
        if (existProblem == null || existProblem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        
        Problem problem = new Problem();
        BeanUtils.copyProperties(saveDTO, problem);
        problem.setUpdateTime(LocalDateTime.now());
        problem.setUpdateBy(BaseContext.getCurrentId());
        
        return this.updateById(problem);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProblem(Long id) {
        Problem problem = this.getById(id);
        if (problem == null || problem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        
        problem.setDeleted(1);
        problem.setUpdateTime(LocalDateTime.now());
        problem.setUpdateBy(BaseContext.getCurrentId());
        
        return this.updateById(problem);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProblems(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        
        List<Problem> problems = this.listByIds(ids);
        if (problems.isEmpty()) {
            throw new RuntimeException("题目不存在");
        }
        
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = BaseContext.getCurrentId();
        
        for (Problem problem : problems) {
            if (problem.getDeleted() == 0) {
                problem.setDeleted(1);
                problem.setUpdateTime(now);
                problem.setUpdateBy(currentUserId);
            }
        }
        
        return this.updateBatchById(problems);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProblemStatus(Long id, Integer status) {
        Problem problem = this.getById(id);
        if (problem == null || problem.getDeleted() == 1) {
            throw new RuntimeException("题目不存在");
        }
        
        problem.setStatus(status);
        problem.setUpdateTime(LocalDateTime.now());
        problem.setUpdateBy(BaseContext.getCurrentId());
        
        return this.updateById(problem);
    }
    
    private ProblemVO convertToVO(Problem problem) {
        ProblemVO vo = new ProblemVO();
        BeanUtils.copyProperties(problem, vo);
        
        // 设置状态描述
        if (problem.getStatus() != null) {
            vo.setStatusDesc(problem.getStatus() == 1 ? "公开" : "隐藏");
        }
        
        return vo;
    }
}

