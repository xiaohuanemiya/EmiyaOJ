package com.emiyaoj.service.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.vo.oj.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        
        Page<Problem> problemPage = problemService.listPublicProblems(page, size, difficulty);
        
        List<ProblemVO> problemVOS = problemPage.getRecords().stream()
                .map(problem -> ProblemVO.builder()
                        .id(problem.getId())
                        .title(problem.getTitle())
                        .difficulty(problem.getDifficulty())
                        .acceptCount(problem.getAcceptCount())
                        .submitCount(problem.getSubmitCount())
                        .createTime(problem.getCreateTime())
                        .build())
                .collect(Collectors.toList());
        
        PageVO<ProblemVO> pageVO = new PageVO<>(
                problemPage.getTotal(),
                problemPage.getPages(),
                problemVOS
        );
        
        return ResponseResult.success(pageVO);
    }
    
    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getProblem(@PathVariable Long id) {
        Problem problem = problemService.getById(id);
        if (problem == null) {
            return ResponseResult.fail("题目不存在");
        }
        
        ProblemVO problemVO = ProblemVO.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .inputDescription(problem.getInputDescription())
                .outputDescription(problem.getOutputDescription())
                .sampleInput(problem.getSampleInput())
                .sampleOutput(problem.getSampleOutput())
                .hint(problem.getHint())
                .difficulty(problem.getDifficulty())
                .timeLimit(problem.getTimeLimit())
                .memoryLimit(problem.getMemoryLimit())
                .acceptCount(problem.getAcceptCount())
                .submitCount(problem.getSubmitCount())
                .createTime(problem.getCreateTime())
                .build();
        
        return ResponseResult.success(problemVO);
    }
}
