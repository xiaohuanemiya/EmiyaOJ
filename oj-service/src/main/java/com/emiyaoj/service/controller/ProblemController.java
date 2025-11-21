package com.emiyaoj.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目Controller
 */
@Tag(name = "题目管理")
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private IProblemService problemService;

    @Operation(summary = "分页查询题目")
    @GetMapping("/page")
    public ResponseResult<PageVO<ProblemVO>> pageProblems(PageDTO pageDTO,
                                                          @RequestParam(required = false) Integer difficulty) {
        Page<Problem> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Problem::getStatus, 1);
        wrapper.eq(Problem::getDeleted, 0);
        if (difficulty != null) {
            wrapper.eq(Problem::getDifficulty, difficulty);
        }
        wrapper.orderByDesc(Problem::getCreateTime);
        
        IPage<Problem> problemPage = problemService.page(page, wrapper);
        
        PageVO<ProblemVO> result = PageVO.of((Page<Problem>) problemPage, problem -> {
            ProblemVO vo = new ProblemVO();
            BeanUtils.copyProperties(problem, vo);
            return vo;
        });
        
        return ResponseResult.success(result);
    }

    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getProblemDetail(@PathVariable Long id) {
        Problem problem = problemService.getById(id);
        if (problem == null || problem.getDeleted() == 1) {
            return ResponseResult.fail("题目不存在");
        }
        
        ProblemVO vo = new ProblemVO();
        BeanUtils.copyProperties(problem, vo);
        return ResponseResult.success(vo);
    }
}
