package com.emiyaoj.service.service.impl;

import com.emiyaoj.service.domain.constant.JudgeStatus;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.mapper.SubmissionResultMapper;
import com.emiyaoj.service.sandbox.client.SandboxClient;
import com.emiyaoj.service.sandbox.dto.*;
import com.emiyaoj.service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 判题服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements IJudgeService {
    
    private final ISubmissionService submissionService;
    private final IProblemService problemService;
    private final ILanguageService languageService;
    private final ITestCaseService testCaseService;
    private final SubmissionResultMapper submissionResultMapper;
    private final SandboxClient sandboxClient;
    
    @Async
    @Override
    public void judgeAsync(Long submissionId) {
        try {
            judge(submissionId);
        } catch (Exception e) {
            log.error("Judge failed for submission: {}", submissionId, e);
            submissionService.updateStatus(submissionId, JudgeStatus.SYSTEM_ERROR.getValue(), 0, 0, 
                    e.getMessage(), null, "0/0", 0);
        }
    }
    
    @Override
    public void judge(Long submissionId) {
        log.info("Starting judge for submission: {}", submissionId);
        
        // 1. 获取提交信息
        Submission submission = submissionService.getById(submissionId);
        if (submission == null) {
            log.error("Submission not found: {}", submissionId);
            return;
        }
        
        // 更新状态为判题中
        submissionService.updateStatus(submissionId, JudgeStatus.JUDGING.getValue(), 
                0, 0, null, null, "0/0", 0);
        
        // 2. 获取题目信息
        Problem problem = problemService.getById(submission.getProblemId());
        if (problem == null) {
            log.error("Problem not found: {}", submission.getProblemId());
            submissionService.updateStatus(submissionId, JudgeStatus.SYSTEM_ERROR.getValue(), 
                    0, 0, "Problem not found", null, "0/0", 0);
            return;
        }
        
        // 3. 获取语言信息
        Language language = languageService.getById(submission.getLanguageId());
        if (language == null) {
            log.error("Language not found: {}", submission.getLanguageId());
            submissionService.updateStatus(submissionId, JudgeStatus.SYSTEM_ERROR.getValue(), 
                    0, 0, "Language not found", null, "0/0", 0);
            return;
        }
        
        // 4. 如果需要编译，先编译
        if (language.getIsCompiled() == 1) {
            boolean compileSuccess = compile(submission, language);
            if (!compileSuccess) {
                return; // 编译失败，已更新状态
            }
        }
        
        // 5. 获取测试用例
        List<TestCase> testCases = testCaseService.listByProblemId(problem.getId());
        if (testCases.isEmpty()) {
            log.error("No test cases found for problem: {}", problem.getId());
            submissionService.updateStatus(submissionId, JudgeStatus.SYSTEM_ERROR.getValue(), 
                    0, 0, "No test cases found", null, "0/0", 0);
            return;
        }
        
        // 6. 运行测试用例
        runTestCases(submission, problem, language, testCases);
        
        log.info("Judge completed for submission: {}", submissionId);
    }
    
    /**
     * 编译代码
     */
    private boolean compile(Submission submission, Language language) {
        log.info("Compiling code for submission: {}", submission.getId());
        
        try {
            // 准备源文件
            String sourceFileName = getSourceFileName(language);
            Map<String, SandboxFile> copyIn = new HashMap<>();
            copyIn.put(sourceFileName, new MemoryFile(submission.getCode()));
            
            // 准备编译命令
            String compileCommand = language.getCompileCommand()
                    .replace("{source}", sourceFileName)
                    .replace("{executable}", "main");
            
            String[] cmdParts = compileCommand.split("\\s+");
            List<String> args = new ArrayList<>(Arrays.asList(cmdParts));
            
            // 构建沙箱请求
            SandboxCmd cmd = SandboxCmd.builder()
                    .args(args)
                    .env(Arrays.asList("PATH=/usr/bin:/bin"))
                    .files(Arrays.asList(
                            null, // stdin
                            new Collector("stdout", 10240L), // stdout
                            new Collector("stderr", 10240L)  // stderr
                    ))
                    .cpuLimit(10_000_000_000L) // 10秒编译时间
                    .memoryLimit(512L * 1024 * 1024) // 512MB
                    .copyIn(copyIn)
                    .copyOut(Arrays.asList("main"))
                    .copyOutCached(Arrays.asList("main"))
                    .build();
            
            SandboxRequest request = SandboxRequest.builder()
                    .cmd(Collections.singletonList(cmd))
                    .build();
            
            // 执行编译
            List<SandboxResult> results = sandboxClient.run(request);
            
            if (results == null || results.isEmpty()) {
                log.error("No compile result returned");
                submissionService.updateStatus(submission.getId(), JudgeStatus.SYSTEM_ERROR.getValue(), 
                        0, 0, "Compile failed: No result", null, "0/0", 0);
                return false;
            }
            
            SandboxResult result = results.get(0);
            
            // 检查编译结果
            if (!SandboxResult.Status.ACCEPTED.getValue().equals(result.getStatus())) {
                log.error("Compile failed: {}", result.getStatus());
                String errorMsg = result.getFiles() != null ? result.getFiles().get("stderr") : result.getError();
                submissionService.updateStatus(submission.getId(), JudgeStatus.COMPILE_ERROR.getValue(), 
                        0, 0, null, errorMsg, "0/0", 0);
                return false;
            }
            
            log.info("Compilation successful for submission: {}", submission.getId());
            return true;
            
        } catch (Exception e) {
            log.error("Compile exception", e);
            submissionService.updateStatus(submission.getId(), JudgeStatus.SYSTEM_ERROR.getValue(), 
                    0, 0, "Compile exception: " + e.getMessage(), null, "0/0", 0);
            return false;
        }
    }
    
    /**
     * 运行测试用例
     */
    private void runTestCases(Submission submission, Problem problem, Language language, List<TestCase> testCases) {
        log.info("Running test cases for submission: {}, count: {}", submission.getId(), testCases.size());
        
        int passedCount = 0;
        int totalCount = testCases.size();
        int maxTimeUsed = 0;
        int maxMemoryUsed = 0;
        String finalStatus = JudgeStatus.ACCEPTED.getValue();
        
        for (TestCase testCase : testCases) {
            try {
                // 运行单个测试用例
                TestResult testResult = runSingleTest(submission, problem, language, testCase);
                
                // 保存测试结果
                SubmissionResult submissionResult = SubmissionResult.builder()
                        .submissionId(submission.getId())
                        .testCaseId(testCase.getId())
                        .status(testResult.status)
                        .timeUsed(testResult.timeUsed)
                        .memoryUsed(testResult.memoryUsed)
                        .errorMessage(testResult.errorMessage)
                        .build();
                submissionResultMapper.insert(submissionResult);
                
                // 更新统计信息
                if (JudgeStatus.ACCEPTED.getValue().equals(testResult.status)) {
                    passedCount++;
                } else if (JudgeStatus.ACCEPTED.getValue().equals(finalStatus)) {
                    // 第一次遇到非AC状态，记录下来
                    finalStatus = testResult.status;
                }
                
                maxTimeUsed = Math.max(maxTimeUsed, testResult.timeUsed);
                maxMemoryUsed = Math.max(maxMemoryUsed, testResult.memoryUsed);
                
                // 如果不是AC，可以提前结束（可选优化）
                // if (!JudgeStatus.ACCEPTED.getValue().equals(testResult.status)) {
                //     break;
                // }
                
            } catch (Exception e) {
                log.error("Error running test case: {}", testCase.getId(), e);
                finalStatus = JudgeStatus.SYSTEM_ERROR.getValue();
                break;
            }
        }
        
        // 更新提交状态
        String passRate = passedCount + "/" + totalCount;
        int score = (int) ((double) passedCount / totalCount * 100);
        
        submissionService.updateStatus(submission.getId(), finalStatus, 
                maxTimeUsed, maxMemoryUsed, null, null, passRate, score);
        
        // 如果AC，增加题目通过次数
        if (JudgeStatus.ACCEPTED.getValue().equals(finalStatus)) {
            problemService.increaseAcceptCount(problem.getId());
        }
    }
    
    /**
     * 运行单个测试用例
     */
    private TestResult runSingleTest(Submission submission, Problem problem, Language language, TestCase testCase) {
        try {
            // 准备执行命令
            String executeCommand = language.getExecuteCommand()
                    .replace("{source}", getSourceFileName(language))
                    .replace("{memory}", String.valueOf(problem.getMemoryLimit()));
            
            String[] cmdParts = executeCommand.split("\\s+");
            List<String> args = new ArrayList<>(Arrays.asList(cmdParts));
            
            // 计算资源限制（考虑语言倍数）
            long cpuLimit = (long) (problem.getTimeLimit() * 1_000_000L * language.getTimeLimitMultiplier().doubleValue());
            long memoryLimit = (long) (problem.getMemoryLimit() * 1024L * 1024 * language.getMemoryLimitMultiplier().doubleValue());
            
            // 构建沙箱请求
            Map<String, SandboxFile> copyIn = new HashMap<>();
            if (language.getIsCompiled() == 1) {
                // 使用已编译的文件
                copyIn.put("main", new MemoryFile("placeholder")); // 实际应该从缓存获取
            } else {
                // 直接运行源代码
                copyIn.put(getSourceFileName(language), new MemoryFile(submission.getCode()));
            }
            
            SandboxCmd cmd = SandboxCmd.builder()
                    .args(args)
                    .env(Arrays.asList("PATH=/usr/bin:/bin"))
                    .files(Arrays.asList(
                            new MemoryFile(testCase.getInput()), // stdin
                            new Collector("stdout", 16L * 1024 * 1024), // stdout 16MB
                            new Collector("stderr", 16L * 1024 * 1024)  // stderr 16MB
                    ))
                    .cpuLimit(cpuLimit)
                    .clockLimit(cpuLimit * 2)
                    .memoryLimit(memoryLimit)
                    .stackLimit((long) problem.getStackLimit() * 1024 * 1024)
                    .procLimit(1)
                    .copyIn(copyIn)
                    .copyOutMax(16L * 1024 * 1024)
                    .build();
            
            SandboxRequest request = SandboxRequest.builder()
                    .cmd(Collections.singletonList(cmd))
                    .build();
            
            // 执行代码
            List<SandboxResult> results = sandboxClient.run(request);
            
            if (results == null || results.isEmpty()) {
                return new TestResult(JudgeStatus.SYSTEM_ERROR.getValue(), 0, 0, "No result returned");
            }
            
            SandboxResult result = results.get(0);
            
            // 转换时间和内存单位
            int timeUsed = (int) (result.getTime() / 1_000_000); // 纳秒转毫秒
            int memoryUsed = (int) (result.getMemory() / 1024); // 字节转KB
            
            // 判断运行状态
            String status = convertSandboxStatus(result.getStatus());
            
            // 如果状态是AC，需要比对输出
            if (JudgeStatus.ACCEPTED.getValue().equals(status)) {
                String output = result.getFiles() != null ? result.getFiles().get("stdout") : "";
                if (!compareOutput(output, testCase.getOutput())) {
                    status = JudgeStatus.WRONG_ANSWER.getValue();
                }
            }
            
            String errorMsg = null;
            if (!JudgeStatus.ACCEPTED.getValue().equals(status)) {
                errorMsg = result.getError();
                if (errorMsg == null && result.getFiles() != null) {
                    errorMsg = result.getFiles().get("stderr");
                }
            }
            
            return new TestResult(status, timeUsed, memoryUsed, errorMsg);
            
        } catch (Exception e) {
            log.error("Error in runSingleTest", e);
            return new TestResult(JudgeStatus.SYSTEM_ERROR.getValue(), 0, 0, e.getMessage());
        }
    }
    
    /**
     * 转换沙箱状态到判题状态
     */
    private String convertSandboxStatus(String sandboxStatus) {
        if (sandboxStatus == null) {
            return JudgeStatus.SYSTEM_ERROR.getValue();
        }
        
        return switch (sandboxStatus) {
            case "Accepted" -> JudgeStatus.ACCEPTED.getValue();
            case "Time Limit Exceeded" -> JudgeStatus.TIME_LIMIT_EXCEEDED.getValue();
            case "Memory Limit Exceeded" -> JudgeStatus.MEMORY_LIMIT_EXCEEDED.getValue();
            case "Output Limit Exceeded" -> JudgeStatus.OUTPUT_LIMIT_EXCEEDED.getValue();
            case "Nonzero Exit Status", "Signalled" -> JudgeStatus.RUNTIME_ERROR.getValue();
            default -> JudgeStatus.SYSTEM_ERROR.getValue();
        };
    }
    
    /**
     * 比对输出
     */
    private boolean compareOutput(String userOutput, String expectedOutput) {
        if (userOutput == null || expectedOutput == null) {
            return false;
        }
        
        // 去除末尾空白字符后比较
        String userTrimmed = userOutput.trim();
        String expectedTrimmed = expectedOutput.trim();
        
        return userTrimmed.equals(expectedTrimmed);
    }
    
    /**
     * 获取源文件名
     */
    private String getSourceFileName(Language language) {
        String ext = language.getSourceFileExt();
        if (".java".equals(ext)) {
            return "Main.java";
        }
        return "main" + ext;
    }
    
    /**
     * 测试结果内部类
     */
    private static class TestResult {
        String status;
        int timeUsed;
        int memoryUsed;
        String errorMessage;
        
        TestResult(String status, int timeUsed, int memoryUsed, String errorMessage) {
            this.status = status;
            this.timeUsed = timeUsed;
            this.memoryUsed = memoryUsed;
            this.errorMessage = errorMessage;
        }
    }
}
