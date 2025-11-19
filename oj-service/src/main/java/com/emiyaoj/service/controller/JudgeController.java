package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.service.IJudgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 判题控制器
 */
@Tag(name = "判题管理")
@RestController
@RequestMapping("/judge")
@RequiredArgsConstructor
public class JudgeController {
    
    private final IJudgeService judgeService;
    
    @Operation(summary = "触发异步判题", description = "为已创建的提交记录触发判题任务")
    @PostMapping("/{submissionId}")
    public ResponseResult<Map<String, Object>> judge(
            @Parameter(description = "提交ID") @PathVariable Long submissionId) {
        
        // 异步触发判题
        judgeService.judgeAsync(submissionId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("submissionId", submissionId);
        result.put("message", "判题任务已触发");
        
        return ResponseResult.success(result);
    }
}
