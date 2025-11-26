package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.vo.SubmissionVO;
import com.emiyaoj.service.service.ISubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 提交记录Controller
 */
@Tag(name = "提交管理")
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private ISubmissionService submissionService;

    @Operation(summary = "提交代码")
    @PostMapping("/submit")
    public ResponseResult<Long> submitCode(@RequestBody SubmitCodeDTO submitCodeDTO) {
        Long submissionId = submissionService.submitCode(submitCodeDTO);
        return ResponseResult.success(submissionId);
    }

    @Operation(summary = "分页查询提交记录")
    @GetMapping("/page")
    public ResponseResult<PageVO<SubmissionVO>> pageSubmissions(
            PageDTO pageDTO,
            @RequestParam(required = false) Long problemId,
            @RequestParam(required = false) Long userId) {
        PageVO<SubmissionVO> result = submissionService.pageSubmissions(pageDTO, problemId, userId);
        return ResponseResult.success(result);
    }

    @Operation(summary = "获取提交详情")
    @GetMapping("/{id}")
    public ResponseResult<SubmissionVO> getSubmissionDetail(@PathVariable Long id) {
        SubmissionVO result = submissionService.getSubmissionDetail(id);
        return ResponseResult.success(result);
    }
}
