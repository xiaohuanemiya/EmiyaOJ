package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.oj.SubmitCodeDTO;
import com.emiyaoj.service.domain.vo.oj.SubmissionDetailVO;
import com.emiyaoj.service.domain.vo.oj.SubmissionVO;
import com.emiyaoj.service.service.ISubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 提交控制器
 */
@Tag(name = "代码提交")
@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
public class SubmissionController {
    
    private final ISubmissionService submissionService;
    
    @Operation(summary = "提交代码")
    @PostMapping("/submit")
    public ResponseResult<Map<String, Object>> submitCode(@RequestBody SubmitCodeDTO submitCodeDTO, 
                                                           HttpServletRequest request) {
        // 获取用户ID (从ThreadLocal或JWT中获取)
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            userId = 1L; // 默认用户，实际应从认证上下文获取
        }
        
        // 获取IP地址
        String ipAddress = request.getRemoteAddr();
        
        try {
            Map<String, Object> result = submissionService.submitCode(submitCodeDTO, userId, ipAddress);
            return ResponseResult.success(result);
        } catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
    
    @Operation(summary = "查询提交列表")
    @GetMapping("/list")
    public ResponseResult<PageVO<SubmissionVO>> listSubmissions(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "题目ID") @RequestParam(required = false) Long problemId) {
        
        PageVO<SubmissionVO> pageVO = submissionService.listSubmissionsVO(page, size, userId, problemId);
        return ResponseResult.success(pageVO);
    }
    
    @Operation(summary = "获取提交详情")
    @GetMapping("/{id}")
    public ResponseResult<SubmissionDetailVO> getSubmission(@PathVariable Long id) {
        SubmissionDetailVO detailVO = submissionService.getSubmissionDetailVO(id);
        if (detailVO == null) {
            return ResponseResult.fail("提交记录不存在");
        }
        return ResponseResult.success(detailVO);
    }
}
