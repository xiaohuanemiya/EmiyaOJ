package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.vo.oj.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 题目控制器
 */
@Tag(name = "题目管理")
@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {
    
    private final IProblemService problemService;
    
    @Operation(summary = "分页查询题目列表")
    @GetMapping("/page")
    public ResponseResult<PageVO<ProblemVO>> listProblems(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "难度") @RequestParam(required = false) Integer difficulty) {
        
        PageVO<ProblemVO> pageVO = problemService.listPublicProblemsVO(page, size, difficulty);
        return ResponseResult.success(pageVO);
    }
    
    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getProblem(@PathVariable Long id) {
        ProblemVO problemVO = problemService.getProblemVO(id);
        if (problemVO == null) {
            return ResponseResult.fail("题目不存在");
        }
        return ResponseResult.success(problemVO);
    }
}
