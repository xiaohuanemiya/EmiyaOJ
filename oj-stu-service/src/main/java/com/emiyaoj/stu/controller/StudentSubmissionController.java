package com.emiyaoj.stu.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.stu.domain.dto.SubmissionDTO;
import com.emiyaoj.stu.domain.vo.SubmissionVO;
import com.emiyaoj.stu.service.IStudentSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 学生提交控制器
 */
@RestController
@RequestMapping("/stu/submission")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StudentSubmissionController {
    
    private final IStudentSubmissionService studentSubmissionService;
    
    @PostMapping("/submit")
    @Operation(summary = "提交代码")
    public ResponseResult<SubmissionVO> submitCode(@RequestBody SubmissionDTO submissionDTO) {
        SubmissionVO submissionVO = studentSubmissionService.submitCode(submissionDTO);
        return ResponseResult.success(submissionVO);
    }
    
    @GetMapping("/page")
    @Operation(summary = "分页查询提交记录")
    public ResponseResult<PageVO<SubmissionVO>> getSubmissionPage(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long problemId) {
        PageVO<SubmissionVO> pageVO = studentSubmissionService.getSubmissionPage(pageNo, pageSize, problemId);
        return ResponseResult.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询提交详情")
    public ResponseResult<SubmissionVO> getSubmissionById(@PathVariable Long id) {
        SubmissionVO submissionVO = studentSubmissionService.getSubmissionById(id);
        return ResponseResult.success(submissionVO);
    }
}

