package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.ProblemQueryDTO;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "题目管理")
@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {
    
    private final IProblemService problemService;
    
    @Operation(summary = "分页查询题目")
    @GetMapping("/page")
    public ResponseResult<PageVO<ProblemVO>> page(ProblemQueryDTO queryDTO) {
        PageVO<ProblemVO> page = problemService.page(queryDTO);
        return ResponseResult.success(page);
    }
    
    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getById(@PathVariable Long id) {
        ProblemVO vo = problemService.getById(id);
        return ResponseResult.success(vo);
    }
    
    @Operation(summary = "创建题目")
    @PostMapping
    public ResponseResult<Long> save(@RequestBody ProblemSaveDTO saveDTO) {
        Long id = problemService.save(saveDTO);
        return ResponseResult.success(id);
    }
    
    @Operation(summary = "更新题目")
    @PutMapping("/{id}")
    public ResponseResult<Void> update(@PathVariable Long id, @RequestBody ProblemSaveDTO saveDTO) {
        problemService.update(id, saveDTO);
        return ResponseResult.success();
    }
    
    @Operation(summary = "删除题目")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        problemService.delete(id);
        return ResponseResult.success();
    }
    
    @Operation(summary = "批量删除题目")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@RequestBody List<Long> ids) {
        problemService.deleteBatch(ids);
        return ResponseResult.success();
    }
}
