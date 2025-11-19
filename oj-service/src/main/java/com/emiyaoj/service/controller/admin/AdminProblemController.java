package com.emiyaoj.service.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.admin.ProblemDTO;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目管理控制器（管理端）
 */
@Tag(name = "题目管理（管理端）")
@RestController
@RequestMapping("/admin/problem")
@RequiredArgsConstructor
public class AdminProblemController {
    
    private final IProblemService problemService;
    
    @Operation(summary = "创建题目")
    @PostMapping
    public ResponseResult<Long> createProblem(@RequestBody ProblemDTO problemDTO) {
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemDTO, problem);
        
        // 初始化统计字段
        problem.setSubmitCount(0);
        problem.setAcceptCount(0);
        
        boolean success = problemService.save(problem);
        if (success) {
            return ResponseResult.success(problem.getId());
        }
        return ResponseResult.fail("创建失败");
    }
    
    @Operation(summary = "更新题目")
    @PutMapping("/{id}")
    public ResponseResult<Void> updateProblem(
            @PathVariable Long id,
            @RequestBody ProblemDTO problemDTO) {
        
        Problem problem = problemService.getById(id);
        if (problem == null) {
            return ResponseResult.fail("题目不存在");
        }
        
        // 保存统计字段
        Integer submitCount = problem.getSubmitCount();
        Integer acceptCount = problem.getAcceptCount();
        
        BeanUtils.copyProperties(problemDTO, problem);
        problem.setId(id);
        problem.setSubmitCount(submitCount);
        problem.setAcceptCount(acceptCount);
        
        boolean success = problemService.updateById(problem);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("更新失败");
    }
    
    @Operation(summary = "删除题目")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteProblem(@PathVariable Long id) {
        boolean success = problemService.removeById(id);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("删除失败");
    }
    
    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<Problem> getProblem(@PathVariable Long id) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            return ResponseResult.fail("题目不存在");
        }
        return ResponseResult.success(problem);
    }
    
    @Operation(summary = "分页查询题目列表")
    @GetMapping("/page")
    public ResponseResult<PageVO<Problem>> listProblems(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "难度（可选）") @RequestParam(required = false) Integer difficulty,
            @Parameter(description = "状态（可选，0-隐藏，1-公开）") @RequestParam(required = false) Integer status,
            @Parameter(description = "是否删除（可选，0-未删除，1-已删除）") @RequestParam(required = false) Integer deleted,
            @Parameter(description = "标题搜索（可选）") @RequestParam(required = false) String title) {
        
        LambdaQueryWrapper<Problem> wrapper = new LambdaQueryWrapper<>();
        
        if (difficulty != null) {
            wrapper.eq(Problem::getDifficulty, difficulty);
        }
        if (status != null) {
            wrapper.eq(Problem::getStatus, status);
        }
        if (deleted != null) {
            wrapper.eq(Problem::getDeleted, deleted);
        }
        if (title != null && !title.trim().isEmpty()) {
            wrapper.like(Problem::getTitle, title);
        }
        
        wrapper.orderByDesc(Problem::getCreateTime);
        
        Page<Problem> problemPage = problemService.page(new Page<>(page, size), wrapper);
        
        PageVO<Problem> pageVO = new PageVO<>(
                problemPage.getTotal(),
                problemPage.getPages(),
                problemPage.getRecords()
        );
        
        return ResponseResult.success(pageVO);
    }
}
