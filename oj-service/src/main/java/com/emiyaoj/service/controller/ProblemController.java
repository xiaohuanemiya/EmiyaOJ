package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目管理控制器
 */
@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ProblemController {
    
    private final IProblemService problemService;
    
    /**
     * 分页查询题目列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('PROBLEM.LIST')")
    @Operation(summary = "分页查询题目列表")
    public ResponseResult<PageVO<ProblemVO>> getProblemPage(@Valid ProblemQueryDTO queryDTO) {
        // 设置默认值
        if (queryDTO.getPageNo() == null) {
            queryDTO.setPageNo(1);
        }
        if (queryDTO.getPageSize() == null) {
            queryDTO.setPageSize(10);
        }
        PageVO<ProblemVO> pageVO = problemService.getProblemPage(queryDTO);
        return ResponseResult.success(pageVO);
    }
    
    /**
     * 根据ID查询题目详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROBLEM.LIST')")
    @Operation(summary = "根据ID查询题目详情")
    public ResponseResult<ProblemVO> getProblemById(@PathVariable Long id) {
        ProblemVO problemVO = problemService.getProblemById(id);
        return ResponseResult.success(problemVO);
    }
    
    /**
     * 新增题目
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PROBLEM.ADD')")
    @Operation(summary = "新增题目")
    public ResponseResult<Void> addProblem(@Valid @RequestBody ProblemSaveDTO saveDTO) {
        problemService.saveProblem(saveDTO);
        return ResponseResult.success();
    }
    
    /**
     * 修改题目
     */
    @PutMapping
    @PreAuthorize("hasAuthority('PROBLEM.EDIT')")
    @Operation(summary = "修改题目")
    public ResponseResult<Void> updateProblem(@Valid @RequestBody ProblemSaveDTO saveDTO) {
        problemService.updateProblem(saveDTO);
        return ResponseResult.success();
    }
    
    /**
     * 删除题目
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PROBLEM.DELETE')")
    @Operation(summary = "删除题目")
    public ResponseResult<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseResult.success();
    }
    
    /**
     * 批量删除题目
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('PROBLEM.DELETE')")
    @Operation(summary = "批量删除题目")
    public ResponseResult<Void> batchDeleteProblems(@RequestBody List<Long> ids) {
        problemService.deleteProblems(ids);
        return ResponseResult.success();
    }
    
    /**
     * 修改题目状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('PROBLEM.EDIT')")
    @Operation(summary = "修改题目状态")
    public ResponseResult<Void> updateProblemStatus(@PathVariable Long id, @RequestParam Integer status) {
        problemService.updateProblemStatus(id, status);
        return ResponseResult.success();
    }
}

