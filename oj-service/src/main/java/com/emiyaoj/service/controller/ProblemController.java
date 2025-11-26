package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目Controller
 */
@Tag(name = "题目管理")
@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final IProblemService problemService;

    @Operation(summary = "分页查询题目")
    @GetMapping("/page")
    public ResponseResult<PageVO<ProblemVO>> pageProblems(PageDTO pageDTO,
                                                          @RequestParam(required = false) Integer difficulty,
                                                          @RequestParam(required = false) Integer status,
                                                          @RequestParam(required = false) String keyword) {
        PageVO<ProblemVO> result = problemService.selectProblemPage(pageDTO, difficulty, status, keyword);
        return ResponseResult.success(result);
    }

    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getProblemDetail(@PathVariable Long id) {
        ProblemVO vo = problemService.selectProblemById(id);
        if (vo == null) {
            return ResponseResult.fail("题目不存在");
        }
        return ResponseResult.success(vo);
    }
    
    @Operation(summary = "新增题目")
    @PostMapping
    @PreAuthorize("hasAuthority('PROBLEM.ADD')")
    public ResponseResult<Void> saveProblem(@RequestBody @Valid ProblemSaveDTO saveDTO) {
        boolean result = problemService.saveProblem(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("新增题目失败");
    }
    
    @Operation(summary = "修改题目")
    @PutMapping
    @PreAuthorize("hasAuthority('PROBLEM.EDIT')")
    public ResponseResult<Void> updateProblem(@RequestBody @Valid ProblemSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return ResponseResult.fail("题目ID不能为空");
        }
        boolean result = problemService.updateProblem(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("修改题目失败");
    }
    
    @Operation(summary = "删除题目")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PROBLEM.DELETE')")
    public ResponseResult<Void> deleteProblem(@PathVariable Long id) {
        boolean result = problemService.deleteProblem(id);
        return result ? ResponseResult.success() : ResponseResult.fail("删除题目失败");
    }
    
    @Operation(summary = "批量删除题目")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('PROBLEM.DELETE')")
    public ResponseResult<Void> deleteProblems(@RequestBody List<Long> ids) {
        boolean result = problemService.deleteProblems(ids);
        return result ? ResponseResult.success() : ResponseResult.fail("批量删除题目失败");
    }
    
    @Operation(summary = "修改题目状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('PROBLEM.EDIT')")
    public ResponseResult<Void> updateProblemStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = problemService.updateProblemStatus(id, status);
        return result ? ResponseResult.success() : ResponseResult.fail("修改题目状态失败");
    }
}
