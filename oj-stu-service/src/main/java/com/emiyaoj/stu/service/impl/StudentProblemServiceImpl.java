package com.emiyaoj.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.stu.domain.dto.ProblemQueryDTO;
import com.emiyaoj.stu.domain.pojo.Language;
import com.emiyaoj.stu.domain.pojo.Problem;
import com.emiyaoj.stu.domain.vo.LanguageVO;
import com.emiyaoj.stu.domain.vo.ProblemVO;
import com.emiyaoj.stu.mapper.LanguageMapper;
import com.emiyaoj.stu.mapper.ProblemMapper;
import com.emiyaoj.stu.service.IStudentProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生题目服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IStudentProblemService {
    
    private final LanguageMapper languageMapper;
    
    @Override
    public PageVO<ProblemVO> getProblemPage(ProblemQueryDTO queryDTO) {
        Page<Problem> page = queryDTO.toMpPageDefaultSortByCreateTimeDesc();
        
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        // 只查询公开且未删除的题目
        wrapper.eq(Problem::getStatus, 1)
               .eq(Problem::getDeleted, 0);
        
        // 难度筛选
        if (queryDTO.getDifficulty() != null) {
            wrapper.eq(Problem::getDifficulty, queryDTO.getDifficulty());
        }
        
        // 关键词搜索（标题）
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
               .eq(Problem::getStatus, 1)
               .eq(Problem::getDeleted, 0);
        
        Problem problem = this.getOne(wrapper);
        if (problem == null) {
            throw new RuntimeException("题目不存在或未公开");
        }
        
        return convertToVO(problem);
    }
    
    @Override
    public List<LanguageVO> getAvailableLanguages() {
        LambdaQueryWrapper<Language> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Language::getStatus, 1)
               .orderByAsc(Language::getId);
        
        List<Language> languages = languageMapper.selectList(wrapper);
        
        return languages.stream().map(language -> {
            LanguageVO vo = new LanguageVO();
            BeanUtils.copyProperties(language, vo);
            return vo;
        }).collect(Collectors.toList());
    }
    
    private ProblemVO convertToVO(Problem problem) {
        ProblemVO vo = new ProblemVO();
        BeanUtils.copyProperties(problem, vo);
        return vo;
    }
}

