package com.emiyaoj.service.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.admin.TestCaseDTO;
import com.emiyaoj.service.domain.pojo.TestCase;
import com.emiyaoj.service.service.ITestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试用例管理控制器（管理端）
 */
@Tag(name = "测试用例管理（管理端）")
@RestController
@RequestMapping("/admin/testcase")
@RequiredArgsConstructor
public class AdminTestCaseController {
    
    private final ITestCaseService testCaseService;
    
    @Operation(summary = "创建测试用例")
    @PostMapping
    public ResponseResult<Long> createTestCase(@RequestBody TestCaseDTO testCaseDTO) {
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(testCaseDTO, testCase);
        
        boolean success = testCaseService.save(testCase);
        if (success) {
            return ResponseResult.success(testCase.getId());
        }
        return ResponseResult.fail("创建失败");
    }
    
    @Operation(summary = "更新测试用例")
    @PutMapping("/{id}")
    public ResponseResult<Void> updateTestCase(
            @PathVariable Long id,
            @RequestBody TestCaseDTO testCaseDTO) {
        
        TestCase testCase = testCaseService.getById(id);
        if (testCase == null) {
            return ResponseResult.fail("测试用例不存在");
        }
        
        BeanUtils.copyProperties(testCaseDTO, testCase);
        testCase.setId(id);
        
        boolean success = testCaseService.updateById(testCase);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("更新失败");
    }
    
    @Operation(summary = "删除测试用例")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteTestCase(@PathVariable Long id) {
        boolean success = testCaseService.removeById(id);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("删除失败");
    }
    
    @Operation(summary = "获取测试用例详情")
    @GetMapping("/{id}")
    public ResponseResult<TestCase> getTestCase(@PathVariable Long id) {
        TestCase testCase = testCaseService.getById(id);
        if (testCase == null) {
            return ResponseResult.fail("测试用例不存在");
        }
        return ResponseResult.success(testCase);
    }
    
    @Operation(summary = "获取题目的所有测试用例")
    @GetMapping("/list")
    public ResponseResult<List<TestCase>> listTestCases(
            @Parameter(description = "题目ID", required = true) @RequestParam Long problemId,
            @Parameter(description = "是否为样例（可选）") @RequestParam(required = false) Integer isSample) {
        
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId);
        
        if (isSample != null) {
            wrapper.eq(TestCase::getIsSample, isSample);
        }
        
        wrapper.orderByAsc(TestCase::getSortOrder)
               .orderByAsc(TestCase::getId);
        
        List<TestCase> testCases = testCaseService.list(wrapper);
        return ResponseResult.success(testCases);
    }
    
    @Operation(summary = "批量删除题目的测试用例")
    @DeleteMapping("/batch")
    public ResponseResult<Void> batchDeleteTestCases(
            @Parameter(description = "题目ID", required = true) @RequestParam Long problemId) {
        
        LambdaQueryWrapper<TestCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestCase::getProblemId, problemId);
        
        boolean success = testCaseService.remove(wrapper);
        if (success) {
            return ResponseResult.success();
        }
        return ResponseResult.fail("删除失败");
    }
}
