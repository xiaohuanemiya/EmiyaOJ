package com.emiyaoj.service.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.oj.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.Language;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.vo.oj.SubmissionDetailVO;
import com.emiyaoj.service.domain.vo.oj.SubmissionVO;
import com.emiyaoj.service.service.ILanguageService;
import com.emiyaoj.service.service.IProblemService;
import com.emiyaoj.service.service.ISubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提交控制器
 */
@Tag(name = "代码提交")
@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
public class SubmissionController {
    
    private final ISubmissionService submissionService;
    private final IProblemService problemService;
    private final ILanguageService languageService;
    
    @Operation(summary = "提交代码")
    @PostMapping("/submit")
    public ResponseResult<Map<String, Object>> submitCode(@RequestBody SubmitCodeDTO submitCodeDTO, 
                                                           HttpServletRequest request) {
        // 验证题目是否存在
        Problem problem = problemService.getById(submitCodeDTO.getProblemId());
        if (problem == null) {
            return ResponseResult.fail("题目不存在");
        }
        
        // 验证语言是否存在
        Language language = languageService.getById(submitCodeDTO.getLanguageId());
        if (language == null) {
            return ResponseResult.fail("不支持的编程语言");
        }
        
        // 获取用户ID (从ThreadLocal或JWT中获取)
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            userId = 1L; // 默认用户，实际应从认证上下文获取
        }
        
        // 获取IP地址
        String ipAddress = request.getRemoteAddr();
        
        // 创建提交记录
        Submission submission = Submission.builder()
                .problemId(submitCodeDTO.getProblemId())
                .userId(userId)
                .languageId(submitCodeDTO.getLanguageId())
                .code(submitCodeDTO.getCode())
                .ipAddress(ipAddress)
                .build();
        
        // 保存并开始判题
        Long submissionId = submissionService.createAndJudge(submission);
        
        // 增加题目提交次数
        problemService.increaseSubmitCount(problem.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("submissionId", submissionId);
        result.put("message", "提交成功，正在判题中...");
        
        return ResponseResult.success(result);
    }
    
    @Operation(summary = "查询提交列表")
    @GetMapping("/list")
    public ResponseResult<PageVO<SubmissionVO>> listSubmissions(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "题目ID") @RequestParam(required = false) Long problemId) {
        
        Page<Submission> submissionPage = submissionService.listSubmissions(page, size, userId, problemId);
        
        List<SubmissionVO> submissionVOS = submissionPage.getRecords().stream()
                .map(submission -> {
                    Problem problem = problemService.getById(submission.getProblemId());
                    Language language = languageService.getById(submission.getLanguageId());
                    
                    return SubmissionVO.builder()
                            .id(submission.getId())
                            .problemId(submission.getProblemId())
                            .problemTitle(problem != null ? problem.getTitle() : "")
                            .userId(submission.getUserId())
                            .languageId(submission.getLanguageId())
                            .languageName(language != null ? language.getName() + " " + language.getVersion() : "")
                            .status(submission.getStatus())
                            .score(submission.getScore())
                            .timeUsed(submission.getTimeUsed())
                            .memoryUsed(submission.getMemoryUsed())
                            .passRate(submission.getPassRate())
                            .createTime(submission.getCreateTime())
                            .build();
                })
                .collect(Collectors.toList());
        
        PageVO<SubmissionVO> pageVO = new PageVO<>(
                submissionPage.getTotal(),
                submissionPage.getPages(),
                submissionVOS
        );
        
        return ResponseResult.success(pageVO);
    }
    
    @Operation(summary = "获取提交详情")
    @GetMapping("/{id}")
    public ResponseResult<SubmissionDetailVO> getSubmission(@PathVariable Long id) {
        Submission submission = submissionService.getById(id);
        if (submission == null) {
            return ResponseResult.fail("提交记录不存在");
        }
        
        Problem problem = problemService.getById(submission.getProblemId());
        Language language = languageService.getById(submission.getLanguageId());
        
        SubmissionDetailVO detailVO = SubmissionDetailVO.builder()
                .id(submission.getId())
                .problemId(submission.getProblemId())
                .problemTitle(problem != null ? problem.getTitle() : "")
                .userId(submission.getUserId())
                .languageId(submission.getLanguageId())
                .languageName(language != null ? language.getName() + " " + language.getVersion() : "")
                .code(submission.getCode())
                .status(submission.getStatus())
                .score(submission.getScore())
                .timeUsed(submission.getTimeUsed())
                .memoryUsed(submission.getMemoryUsed())
                .errorMessage(submission.getErrorMessage())
                .compileMessage(submission.getCompileMessage())
                .passRate(submission.getPassRate())
                .createTime(submission.getCreateTime())
                .build();
        
        return ResponseResult.success(detailVO);
    }
}
