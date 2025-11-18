package com.emiyaoj.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.service.domain.pojo.Problem;
import com.emiyaoj.service.domain.pojo.Submission;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.domain.sandbox.*;
import com.emiyaoj.service.mapper.ProblemMapper;
import com.emiyaoj.service.mapper.SubmissionMapper;
import com.emiyaoj.service.mapper.TestCaseMapper;
import com.emiyaoj.service.service.IJudgeService;
import com.emiyaoj.service.service.ISandboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements IJudgeService {
    
    private final ISandboxService sandboxService;
    private final SubmissionMapper submissionMapper;
    private final ProblemMapper problemMapper;
    private final TestCaseMapper testCaseMapper;
    
    private static final Map<String, String[]> LANGUAGE_COMMANDS = new HashMap<>();
    
    static {
        LANGUAGE_COMMANDS.put("c", new String[]{"/usr/bin/gcc", "main.c", "-o", "main", "-O2", "-Wall", "-std=c11"});
        LANGUAGE_COMMANDS.put("cpp", new String[]{"/usr/bin/g++", "main.cpp", "-o", "main", "-O2", "-Wall", "-std=c++17"});
        LANGUAGE_COMMANDS.put("java", new String[]{"/usr/bin/javac", "Main.java"});
        LANGUAGE_COMMANDS.put("python", new String[]{"/usr/bin/python3", "main.py"});
        LANGUAGE_COMMANDS.put("go", new String[]{"/usr/bin/go", "build", "-o", "main", "main.go"});
    }
    
    private static final Map<String, String[]> RUN_COMMANDS = new HashMap<>();
    
    static {
        RUN_COMMANDS.put("c", new String[]{"./main"});
        RUN_COMMANDS.put("cpp", new String[]{"./main"});
        RUN_COMMANDS.put("java", new String[]{"/usr/bin/java", "Main"});
        RUN_COMMANDS.put("python", new String[]{"/usr/bin/python3", "main.py"});
        RUN_COMMANDS.put("go", new String[]{"./main"});
    }
    
    private static final Map<String, String> SOURCE_FILE_NAMES = new HashMap<>();
    
    static {
        SOURCE_FILE_NAMES.put("c", "main.c");
        SOURCE_FILE_NAMES.put("cpp", "main.cpp");
        SOURCE_FILE_NAMES.put("java", "Main.java");
        SOURCE_FILE_NAMES.put("python", "main.py");
        SOURCE_FILE_NAMES.put("go", "main.go");
    }
    
    @Override
    public void judge(Submission submission) {
        try {
            // 更新状态为 Judging
            submission.setStatus("Judging");
            submissionMapper.updateById(submission);
            
            // 获取题目信息
            Problem problem = problemMapper.selectById(submission.getProblemId());
            if (problem == null) {
                updateSubmissionError(submission, "题目不存在");
                return;
            }
            
            // 获取测试用例
            LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TestCase::getProblemId, problem.getId())
                   .eq(TestCase::getDeleted, 0)
                   .orderByAsc(TestCase::getSortOrder);
            List<TestCase> testCases = testCaseMapper.selectList(wrapper);
            
            if (testCases.isEmpty()) {
                updateSubmissionError(submission, "没有测试用例");
                return;
            }
            
            // 判题
            JudgeResult result = judgeCode(submission, problem, testCases);
            
            // 更新提交记录
            submission.setStatus(result.getStatus());
            submission.setTimeUsed(result.getMaxTime());
            submission.setMemoryUsed(result.getMaxMemory());
            submission.setPassRate(result.getPassedCount() + "/" + testCases.size());
            submission.setErrorMessage(result.getErrorMessage());
            submission.setJudgeResult(JSON.toJSONString(result.getTestResults()));
            submission.setFinishTime(LocalDateTime.now());
            submissionMapper.updateById(submission);
            
            // 更新题目统计
            if ("Accepted".equals(result.getStatus())) {
                problem.setAcceptedCount(problem.getAcceptedCount() + 1);
            }
            problem.setSubmitCount(problem.getSubmitCount() + 1);
            problemMapper.updateById(problem);
            
        } catch (Exception e) {
            log.error("判题失败", e);
            updateSubmissionError(submission, "判题系统错误: " + e.getMessage());
        }
    }
    
    private JudgeResult judgeCode(Submission submission, Problem problem, List<TestCase> testCases) {
        JudgeResult result = new JudgeResult();
        result.setTestResults(new ArrayList<>());
        
        String language = submission.getLanguage().toLowerCase();
        String code = submission.getCode();
        
        // 编译代码（如果需要）
        if (needsCompilation(language)) {
            TestCaseResult compileResult = compileCode(language, code, problem);
            if (compileResult != null && !"Accepted".equals(compileResult.getStatus())) {
                result.setStatus("Compile Error");
                result.setErrorMessage(compileResult.getErrorMessage());
                return result;
            }
        }
        
        // 运行测试用例
        int passedCount = 0;
        long maxTime = 0;
        long maxMemory = 0;
        String finalStatus = "Accepted";
        
        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            TestCaseResult testResult = runTestCase(language, code, testCase, problem, i + 1);
            
            result.getTestResults().add(testResult);
            
            if ("Accepted".equals(testResult.getStatus())) {
                passedCount++;
            } else if ("Accepted".equals(finalStatus)) {
                finalStatus = testResult.getStatus();
            }
            
            maxTime = Math.max(maxTime, testResult.getTime());
            maxMemory = Math.max(maxMemory, testResult.getMemory());
        }
        
        result.setPassedCount(passedCount);
        result.setTotalCount(testCases.size());
        result.setMaxTime(maxTime);
        result.setMaxMemory(maxMemory);
        result.setStatus(finalStatus);
        
        return result;
    }
    
    private boolean needsCompilation(String language) {
        return "c".equals(language) || "cpp".equals(language) || "java".equals(language) || "go".equals(language);
    }
    
    private TestCaseResult compileCode(String language, String code, Problem problem) {
        TestCaseResult result = new TestCaseResult();
        result.setTestCaseId(-1L);
        result.setStatus("Compiling");
        
        try {
            SandboxRequest request = new SandboxRequest();
            request.setCmd(new ArrayList<>());
            
            Cmd compileCmd = new Cmd();
            compileCmd.setArgs(Arrays.asList(LANGUAGE_COMMANDS.get(language)));
            compileCmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
            
            // 设置资源限制
            compileCmd.setCpuLimit(10000000000L); // 10s
            compileCmd.setMemoryLimit(256 * 1024 * 1024L); // 256MB
            
            // 复制源代码文件
            Map<String, Object> copyIn = new HashMap<>();
            copyIn.put(SOURCE_FILE_NAMES.get(language), new MemoryFile(code));
            compileCmd.setCopyIn(copyIn);
            
            // 收集标准错误输出
            compileCmd.setFiles(Arrays.asList(
                null,  // stdin
                new Collector("stdout", 10240L, false),  // stdout
                new Collector("stderr", 10240L, false)   // stderr
            ));
            compileCmd.setCopyOut(Arrays.asList("stdout", "stderr"));
            
            // 如果是编译型语言，需要复制可执行文件
            if ("c".equals(language) || "cpp".equals(language) || "go".equals(language)) {
                compileCmd.setCopyOutCached(Arrays.asList("main"));
            } else if ("java".equals(language)) {
                compileCmd.setCopyOutCached(Arrays.asList("Main.class"));
            }
            
            request.getCmd().add(compileCmd);
            
            List<SandboxResult> results = sandboxService.run(request);
            
            if (results != null && !results.isEmpty()) {
                SandboxResult sandboxResult = results.get(0);
                
                if (sandboxResult.getStatus() == Status.Accepted && sandboxResult.getExitStatus() == 0) {
                    result.setStatus("Accepted");
                    return result;
                } else {
                    result.setStatus("Compile Error");
                    String stderr = sandboxResult.getFiles() != null ? sandboxResult.getFiles().get("stderr") : "";
                    result.setErrorMessage(stderr != null && !stderr.isEmpty() ? stderr : "编译失败");
                    return result;
                }
            }
            
        } catch (Exception e) {
            log.error("编译失败", e);
            result.setStatus("Compile Error");
            result.setErrorMessage("编译系统错误: " + e.getMessage());
        }
        
        return result;
    }
    
    private TestCaseResult runTestCase(String language, String code, TestCase testCase, Problem problem, int caseNumber) {
        TestCaseResult result = new TestCaseResult();
        result.setTestCaseId(testCase.getId());
        result.setTestCaseNumber(caseNumber);
        
        try {
            SandboxRequest request = new SandboxRequest();
            request.setCmd(new ArrayList<>());
            
            Cmd runCmd = new Cmd();
            runCmd.setArgs(Arrays.asList(RUN_COMMANDS.get(language)));
            runCmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
            
            // 设置资源限制
            long timeLimit = problem.getTimeLimit() * 1000000L; // ms to ns
            runCmd.setCpuLimit(timeLimit);
            runCmd.setClockLimit(timeLimit * 3);
            runCmd.setMemoryLimit(problem.getMemoryLimit() * 1024 * 1024L);
            runCmd.setStackLimit(problem.getStackLimit() * 1024 * 1024L);
            
            // 复制必要的文件
            Map<String, Object> copyIn = new HashMap<>();
            if ("python".equals(language)) {
                copyIn.put(SOURCE_FILE_NAMES.get(language), new MemoryFile(code));
            }
            if (!copyIn.isEmpty()) {
                runCmd.setCopyIn(copyIn);
            }
            
            // 设置输入输出
            runCmd.setFiles(Arrays.asList(
                new MemoryFile(testCase.getInput()),  // stdin
                new Collector("stdout", 10240L, false),  // stdout
                new Collector("stderr", 10240L, false)   // stderr
            ));
            runCmd.setCopyOut(Arrays.asList("stdout", "stderr"));
            
            request.getCmd().add(runCmd);
            
            List<SandboxResult> results = sandboxService.run(request);
            
            if (results != null && !results.isEmpty()) {
                SandboxResult sandboxResult = results.get(0);
                
                result.setTime(sandboxResult.getTime());
                result.setMemory(sandboxResult.getMemory());
                
                // 判断运行结果
                if (sandboxResult.getStatus() == Status.TimeLimitExceeded) {
                    result.setStatus("Time Limit Exceeded");
                } else if (sandboxResult.getStatus() == Status.MemoryLimitExceeded) {
                    result.setStatus("Memory Limit Exceeded");
                } else if (sandboxResult.getStatus() == Status.OutputLimitExceeded) {
                    result.setStatus("Output Limit Exceeded");
                } else if (sandboxResult.getStatus() == Status.Signalled || sandboxResult.getStatus() == Status.NonzeroExitStatus) {
                    result.setStatus("Runtime Error");
                    String stderr = sandboxResult.getFiles() != null ? sandboxResult.getFiles().get("stderr") : "";
                    result.setErrorMessage(stderr);
                } else if (sandboxResult.getStatus() == Status.Accepted) {
                    // 比较输出
                    String actualOutput = sandboxResult.getFiles() != null ? sandboxResult.getFiles().get("stdout") : "";
                    String expectedOutput = testCase.getOutput();
                    
                    if (compareOutput(actualOutput, expectedOutput)) {
                        result.setStatus("Accepted");
                    } else {
                        result.setStatus("Wrong Answer");
                        result.setErrorMessage("期望输出:\n" + expectedOutput + "\n实际输出:\n" + actualOutput);
                    }
                } else {
                    result.setStatus("System Error");
                    result.setErrorMessage(sandboxResult.getError());
                }
            }
            
        } catch (Exception e) {
            log.error("运行测试用例失败", e);
            result.setStatus("System Error");
            result.setErrorMessage("系统错误: " + e.getMessage());
        }
        
        return result;
    }
    
    private boolean compareOutput(String actual, String expected) {
        if (actual == null) actual = "";
        if (expected == null) expected = "";
        
        // 去除行尾空格并比较
        String[] actualLines = actual.split("\n");
        String[] expectedLines = expected.split("\n");
        
        if (actualLines.length != expectedLines.length) {
            return false;
        }
        
        for (int i = 0; i < actualLines.length; i++) {
            if (!actualLines[i].trim().equals(expectedLines[i].trim())) {
                return false;
            }
        }
        
        return true;
    }
    
    private void updateSubmissionError(Submission submission, String errorMessage) {
        submission.setStatus("System Error");
        submission.setErrorMessage(errorMessage);
        submission.setFinishTime(LocalDateTime.now());
        submissionMapper.updateById(submission);
    }
    
    // 内部类：判题结果
    private static class JudgeResult {
        private String status;
        private String errorMessage;
        private int passedCount;
        private int totalCount;
        private long maxTime;
        private long maxMemory;
        private List<TestCaseResult> testResults;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public int getPassedCount() { return passedCount; }
        public void setPassedCount(int passedCount) { this.passedCount = passedCount; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public long getMaxTime() { return maxTime; }
        public void setMaxTime(long maxTime) { this.maxTime = maxTime; }
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        public List<TestCaseResult> getTestResults() { return testResults; }
        public void setTestResults(List<TestCaseResult> testResults) { this.testResults = testResults; }
    }
    
    // 内部类：测试用例结果
    private static class TestCaseResult {
        private Long testCaseId;
        private Integer testCaseNumber;
        private String status;
        private Long time;
        private Long memory;
        private String errorMessage;
        
        public Long getTestCaseId() { return testCaseId; }
        public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }
        public Integer getTestCaseNumber() { return testCaseNumber; }
        public void setTestCaseNumber(Integer testCaseNumber) { this.testCaseNumber = testCaseNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getTime() { return time; }
        public void setTime(Long time) { this.time = time; }
        public Long getMemory() { return memory; }
        public void setMemory(Long memory) { this.memory = memory; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
