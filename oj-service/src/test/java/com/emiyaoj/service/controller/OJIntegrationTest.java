package com.emiyaoj.service.controller;

import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.service.OjApplication;
import com.emiyaoj.service.domain.dto.SubmitCodeDTO;
import com.emiyaoj.service.domain.pojo.*;
import com.emiyaoj.service.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OJ系统集成测试
 * 注意：需要数据库和GoJudge服务运行才能通过测试
 */
@SpringBootTest(classes = OjApplication.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:mysql://10.30.22.1:3306/emiya-oj?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai",
    "spring.datasource.username=root",
    "spring.datasource.password=123",
    "gojudge.url=http://10.30.22.1:10000"
})
public class OJIntegrationTest {

    @Autowired
    private ILanguageService languageService;

    @Autowired
    private IProblemService problemService;

    @Autowired
    private ITestCaseService testCaseService;

    @Autowired
    private ISubmissionService submissionService;

    private Long cppLanguageId;
    private Long cLanguageId;
    private Long problemId;

    @BeforeEach
    public void setUp() {
        // 查找C++语言
        Language cppLang = languageService.lambdaQuery()
                .eq(Language::getName, "C++")
                .one();
        if (cppLang != null) {
            cppLanguageId = cppLang.getId();
        }

        // 查找C语言
        Language cLang = languageService.lambdaQuery()
                .eq(Language::getName, "C")
                .one();
        if (cLang != null) {
            cLanguageId = cLang.getId();
        }

        // 查找A+B Problem
        Problem problem = problemService.lambdaQuery()
                .eq(Problem::getTitle, "A+B Problem")
                .one();
        if (problem != null) {
            problemId = problem.getId();
        }
        BaseContext.setCurrentId(-1L); // 设置为系统用户提交

    }

    @Test
    public void testCppSubmissionAccepted() throws InterruptedException {
        if (cppLanguageId == null || problemId == null) {
            System.out.println("跳过测试：数据库中没有必要的数据");
            return;
        }

        // 正确的C++代码
        String correctCode = """
                #include <iostream>
                int main() {
                    int a, b;
                    std::cin >> a >> b;
                    std::cout << a + b << std::endl;
                    return 0;
                }
                """;

        SubmitCodeDTO dto = new SubmitCodeDTO();
        dto.setProblemId(problemId);
        dto.setLanguageId(cppLanguageId);
        dto.setCode(correctCode);
        Long submissionId = submissionService.submitCode(dto);
        assertNotNull(submissionId, "提交ID不应为空");

        // 等待判题完成（异步）
        Thread.sleep(5000);

        Submission submission = submissionService.getById(submissionId);
        assertNotNull(submission, "提交记录不应为空");
        System.out.println("提交状态: " + submission.getStatus());
        System.out.println("通过率: " + submission.getPassRate());
        System.out.println("时间: " + submission.getTimeUsed() + "ms");
        System.out.println("内存: " + submission.getMemoryUsed() + "KB");

        // 验证结果
        assertEquals("Accepted", submission.getStatus(), "应该通过所有测试用例");
    }

    @Test
    public void testCSubmissionAccepted() throws InterruptedException {
        if (cLanguageId == null || problemId == null) {
            System.out.println("跳过测试：数据库中没有必要的数据");
            return;
        }

        // 正确的C代码
        String correctCode = """
                #include <stdio.h>
                int main() {
                    int a, b;
                    scanf("%d %d", &a, &b);
                    printf("%d\\n", a + b);
                    return 0;
                }
                """;

        SubmitCodeDTO dto = new SubmitCodeDTO();
        dto.setProblemId(problemId);
        dto.setLanguageId(cLanguageId);
        dto.setCode(correctCode);

        Long submissionId = submissionService.submitCode(dto);
        assertNotNull(submissionId, "提交ID不应为空");

        // 等待判题完成（异步）
        Thread.sleep(5000);

        Submission submission = submissionService.getById(submissionId);
        assertNotNull(submission, "提交记录不应为空");
        System.out.println("C语言提交状态: " + submission.getStatus());
        System.out.println("通过率: " + submission.getPassRate());

        // 验证结果
        assertEquals("Accepted", submission.getStatus(), "应该通过所有测试用例");
    }

    @Test
    public void testWrongAnswer() throws InterruptedException {
        if (cppLanguageId == null || problemId == null) {
            System.out.println("跳过测试：数据库中没有必要的数据");
            return;
        }

        // 错误的代码（输出a-b而不是a+b）
        String wrongCode = """
                #include <iostream>
                int main() {
                    int a, b;
                    std::cin >> a >> b;
                    std::cout << a - b << std::endl;
                    return 0;
                }
                """;

        SubmitCodeDTO dto = new SubmitCodeDTO();
        dto.setProblemId(problemId);
        dto.setLanguageId(cppLanguageId);
        dto.setCode(wrongCode);

        Long submissionId = submissionService.submitCode(dto);
        assertNotNull(submissionId, "提交ID不应为空");

        // 等待判题完成
        Thread.sleep(5000);

        Submission submission = submissionService.getById(submissionId);
        assertNotNull(submission, "提交记录不应为空");
        System.out.println("错误答案提交状态: " + submission.getStatus());
        System.out.println("通过率: " + submission.getPassRate());

        // 验证结果
        assertEquals("Wrong Answer", submission.getStatus(), "应该是错误答案");
    }

    @Test
    public void testCompileError() throws InterruptedException {
        if (cppLanguageId == null || problemId == null) {
            System.out.println("跳过测试：数据库中没有必要的数据");
            return;
        }

        // 编译错误的代码
        String compileErrorCode = """
                #include <iostream>
                int main() {
                    int a, b
                    std::cin >> a >> b;
                    std::cout << a + b << std::endl;
                    return 0;
                }
                """;

        SubmitCodeDTO dto = new SubmitCodeDTO();
        dto.setProblemId(problemId);
        dto.setLanguageId(cppLanguageId);
        dto.setCode(compileErrorCode);

        Long submissionId = submissionService.submitCode(dto);
        assertNotNull(submissionId, "提交ID不应为空");

        // 等待判题完成
        Thread.sleep(3000);

        Submission submission = submissionService.getById(submissionId);
        assertNotNull(submission, "提交记录不应为空");
        System.out.println("编译错误提交状态: " + submission.getStatus());
        System.out.println("编译信息: " + submission.getCompileMessage());

        // 验证结果
        assertEquals("Compile Error", submission.getStatus(), "应该是编译错误");
        assertNotNull(submission.getCompileMessage(), "应该有编译错误信息");
    }
}
