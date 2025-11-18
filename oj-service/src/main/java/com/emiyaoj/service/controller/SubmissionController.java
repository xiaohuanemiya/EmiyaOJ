package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.vo.SubmissionVO;
import com.emiyaoj.service.service.ISubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "提交管理")
@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
public class SubmissionController {
    
    private final ISubmissionService submissionService;
    
    @Operation(summary = "提交代码")
    @PostMapping("/submit")
    public ResponseResult<Long> submit(@RequestBody SubmitCodeDTO submitDTO) {
        Long id = submissionService.submit(submitDTO);
        return ResponseResult.success(id);
    }
    
    @Operation(summary = "获取提交详情")
    @GetMapping("/{id}")
    public ResponseResult<SubmissionVO> getById(@PathVariable Long id) {
        SubmissionVO vo = submissionService.getById(id);
        return ResponseResult.success(vo);
    }
    
    @Operation(summary = "获取用户提交列表")
    @GetMapping("/user/{userId}")
    public ResponseResult<List<SubmissionVO>> getUserSubmissions(@PathVariable Long userId) {
        List<SubmissionVO> list = submissionService.getUserSubmissions(userId);
        return ResponseResult.success(list);
    }
    
    @Operation(summary = "获取题目提交列表")
    @GetMapping("/problem/{problemId}")
    public ResponseResult<List<SubmissionVO>> getProblemSubmissions(@PathVariable Long problemId) {
        List<SubmissionVO> list = submissionService.getProblemSubmissions(problemId);
        return ResponseResult.success(list);
    }
}
