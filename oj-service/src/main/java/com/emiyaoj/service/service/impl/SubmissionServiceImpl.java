package com.emiyaoj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.exception.BadRequestException;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.util.oj.Model.Request;
import com.emiyaoj.service.util.oj.Model.Result;
import com.emiyaoj.service.domain.vo.SubmissionVO;
import com.emiyaoj.service.mapper.SubmissionMapper;
import com.emiyaoj.service.service.*;
import com.emiyaoj.service.util.GoJudgeRequestBuilder;
import com.emiyaoj.service.util.GoJudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 提交记录Service实现
 */
@Slf4j
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {

    @Autowired
    private IProblemService problemService;
    
    @Autowired
    private ILanguageService languageService;
    
    @Autowired
    private ITestCaseService testCaseService;
    
    @Autowired
    private ISubmissionResultService submissionResultService;
    
    @Autowired
    private GoJudgeService goJudgeService;

    @Override
    @Transactional
    public Long submitCode(SubmitCodeDTO submitCodeDTO) {
        // 1. 验证题目和语言是否存在
        Problem problem = problemService.getById(submitCodeDTO.getProblemId());
        if (problem == null || problem.getDeleted() == 1) {
            throw new BadRequestException("题目不存在");
        }
        
        Language language = languageService.getById(submitCodeDTO.getLanguageId());
        if (language == null || language.getStatus() == 0) {
            throw new BadRequestException("语言不支持");
        }
        
        // 2. 创建提交记录
        Submission submission = new Submission();
        submission.setProblemId(submitCodeDTO.getProblemId());
        submission.setUserId(BaseContext.getCurrentId()); // 从上下文获取当前用户ID
        submission.setLanguageId(submitCodeDTO.getLanguageId());
        submission.setCode(submitCodeDTO.getCode());
        submission.setStatus("Pending");
        submission.setScore(0);
        submission.setTimeUsed(0);
        submission.setMemoryUsed(0);
        
        this.save(submission);
        
        // 3. 异步执行判题
        judgeAsync(submission.getId(), problem, language, submitCodeDTO.getCode());
        
        return submission.getId();
    }
    
    /**
     * 异步判题
     */
    @Async
    public void judgeAsync(Long submissionId, Problem problem, Language language, String code) {
        try {
            // 更新状态为判题中
            updateStatus(submissionId, "Judging", null);
            
            // 编译代码（如果需要）
            String executableFileId = null;
            if (language.getIsCompiled() == 1) {
                Result compileResult = compileCode(code, language);
                if (!"Accepted".equals(compileResult.getStatus().getValue())) {
                    // 编译失败
                    String compileError = compileResult.getFiles().getOrDefault("stderr", "编译失败");
                    updateStatus(submissionId, "Compile Error", compileError);
                    return;
                }
                String outputName = "main";
                executableFileId = compileResult.getFileIds().get(outputName);
            }
            
            // 获取测试用例
            List<TestCase> testCases = testCaseService.getTestCasesByProblemId(problem.getId());
            if (testCases.isEmpty()) {
                updateStatus(submissionId, "System Error", "没有可用的测试用例");
                return;
            }
            
            // 执行测试用例
            int passedCount = 0;
            int totalCount = testCases.size();
            int maxTimeUsed = 0;
            int maxMemoryUsed = 0;
            String finalStatus = "Accepted";
            
            for (TestCase testCase : testCases) {
                SubmissionResult result = runTestCase(submissionId, testCase, executableFileId, 
                        problem, language, code);
                
                submissionResultService.save(result);
                
                if ("Accepted".equals(result.getStatus())) {
                    passedCount++;
                } else if (finalStatus.equals("Accepted")) {
                    // 第一个失败的测试用例决定最终状态
                    finalStatus = result.getStatus();
                }
                
                maxTimeUsed = Math.max(maxTimeUsed, result.getTimeUsed());
                maxMemoryUsed = Math.max(maxMemoryUsed, result.getMemoryUsed());
            }
            
            // 清理编译后的文件
            if (executableFileId != null) {
                try {
                    goJudgeService.deleteFile(executableFileId);
                } catch (Exception e) {
                    log.warn("Failed to delete executable file: {}", executableFileId, e);
                }
            }
            
            // 更新提交记录
            Submission submission = this.getById(submissionId);
            submission.setStatus(finalStatus);
            submission.setTimeUsed(maxTimeUsed);
            submission.setMemoryUsed(maxMemoryUsed);
            submission.setPassRate(passedCount + "/" + totalCount);
            if ("Accepted".equals(finalStatus)) {
                submission.setScore(100);
            }
            this.updateById(submission);
            
            // 更新题目统计
            if ("Accepted".equals(finalStatus)) {
                problemService.incrementAcceptCount(problem.getId());
            }
            problemService.incrementSubmitCount(problem.getId());
            
        } catch (Exception e) {
            log.error("判题异常", e);
            updateStatus(submissionId, "System Error", e.getMessage());
        }
    }
    
    /**
     * 编译代码
     */
    private Result compileCode(String sourceCode, Language language) {
        String outputName = "main";
        Request compileRequest;
        
        // 根据语言选择编译命令
        if ("C++".equalsIgnoreCase(language.getName()) || "cpp".equalsIgnoreCase(language.getName())) {
            compileRequest = GoJudgeRequestBuilder.buildCppCompileRequest(sourceCode, outputName);
        } else if ("C".equalsIgnoreCase(language.getName())) {
            compileRequest = GoJudgeRequestBuilder.buildCCompileRequest(sourceCode, outputName);
        } else {
            throw new BadRequestException("暂不支持的编译语言，请联系管理员: " + language.getName());
        }
        
        List<Result> compileResults = goJudgeService.execute(compileRequest);
        return compileResults.get(0);
    }
    
    /**
     * 运行单个测试用例
     */
    private SubmissionResult runTestCase(Long submissionId, TestCase testCase, String executableFileId,
                                         Problem problem, Language language, String code) {
        SubmissionResult result = new SubmissionResult();
        result.setSubmissionId(submissionId);
        result.setTestCaseId(testCase.getId());
        
        try {
            // 计算时间和内存限制（考虑倍数）
            long cpuLimit = (long) (problem.getTimeLimit() / 1000.0 * 
                    language.getTimeLimitMultiplier().doubleValue());
            long memoryLimit = (long) (problem.getMemoryLimit() * 
                    language.getMemoryLimitMultiplier().doubleValue());
            
            String outputName = "main";
            Request runRequest = GoJudgeRequestBuilder.buildRunRequest(
                    executableFileId, testCase.getInput(), outputName, cpuLimit, memoryLimit);
            
            List<Result> runResults = goJudgeService.execute(runRequest);
            Result runResult = runResults.get(0);
            
            // 获取程序输出
            String output = runResult.getFiles().getOrDefault("stdout", "");
            String expectedOutput = testCase.getOutput();
            
            // 判断运行状态
            String status = runResult.getStatus().getValue();
            
            if ("Accepted".equals(status)) {
                // 比较输出
                if (compareOutput(output, expectedOutput)) {
                    result.setStatus("Accepted");
                } else {
                    result.setStatus("Wrong Answer");
                }
            } else if ("Time Limit Exceeded".equals(status)) {
                result.setStatus("Time Limit Exceeded");
            } else if ("Memory Limit Exceeded".equals(status)) {
                result.setStatus("Memory Limit Exceeded");
            } else {
                result.setStatus("Runtime Error");
                String stderr = runResult.getFiles().getOrDefault("stderr", "");
                result.setErrorMessage(stderr);
            }
            
            // 记录资源使用
            result.setTimeUsed((int) (runResult.getTime() / 1000000)); // 纳秒转毫秒
            result.setMemoryUsed((int) (runResult.getMemory() / 1024)); // 字节转KB
            
        } catch (Exception e) {
            log.error("测试用例执行异常", e);
            result.setStatus("System Error");
            result.setErrorMessage(e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 比较输出（忽略行尾空格和末尾空行）
     */
    private boolean compareOutput(String output, String expected) {
        String[] outputLines = output.split("\n");
        String[] expectedLines = expected.split("\n");
        
        // 去除空行
        outputLines = removeEmptyLines(outputLines);
        expectedLines = removeEmptyLines(expectedLines);
        
        if (outputLines.length != expectedLines.length) {
            return false;
        }
        
        for (int i = 0; i < outputLines.length; i++) {
            if (!outputLines[i].trim().equals(expectedLines[i].trim())) {
                return false;
            }
        }
        
        return true;
    }
    
    private String[] removeEmptyLines(String[] lines) {
        return java.util.Arrays.stream(lines)
                .filter(line -> !line.trim().isEmpty())
                .toArray(String[]::new);
    }
    
    /**
     * 更新提交状态
     */
    private void updateStatus(Long submissionId, String status, String errorMessage) {
        Submission submission = this.getById(submissionId);
        submission.setStatus(status);
        if (errorMessage != null) {
            submission.setErrorMessage(errorMessage);
            submission.setCompileMessage(errorMessage);
        }
        this.updateById(submission);
    }

    @Override
    public PageVO<SubmissionVO> pageSubmissions(PageDTO pageDTO, Long problemId, Long userId) {
        Page<Submission> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        if (problemId != null) {
            wrapper.eq(Submission::getProblemId, problemId);
        }
        if (userId != null) {
            wrapper.eq(Submission::getUserId, userId);
        }
        wrapper.orderByDesc(Submission::getCreateTime);
        
        IPage<Submission> submissionPage = this.page(page, wrapper);
        
        PageVO<SubmissionVO> result = PageVO.of((Page<Submission>) submissionPage, submission -> {
            SubmissionVO vo = new SubmissionVO();
            BeanUtils.copyProperties(submission, vo);
            
            // 补充题目和语言信息
            Problem problem = problemService.getById(submission.getProblemId());
            if (problem != null) {
                vo.setProblemTitle(problem.getTitle());
            }
            
            Language language = languageService.getById(submission.getLanguageId());
            if (language != null) {
                vo.setLanguageName(language.getName());
            }
            
            return vo;
        });
        
        return result;
    }

    @Override
    public SubmissionVO getSubmissionDetail(Long id) {
        Submission submission = this.getById(id);
        if (submission == null) {
            throw new BadRequestException("提交记录不存在");
        }
        
        SubmissionVO vo = new SubmissionVO();
        BeanUtils.copyProperties(submission, vo);
        
        // 补充题目和语言信息
        Problem problem = problemService.getById(submission.getProblemId());
        if (problem != null) {
            vo.setProblemTitle(problem.getTitle());
        }
        
        Language language = languageService.getById(submission.getLanguageId());
        if (language != null) {
            vo.setLanguageName(language.getName());
        }
        
        return vo;
    }
}
