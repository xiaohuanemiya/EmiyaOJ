package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.vo.TestCaseVO;
import com.emiyaoj.service.service.ITestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试用例管理控制器
 */
@RestController
@RequestMapping("/testcase")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class TestCaseController {
    
    private final ITestCaseService testCaseService;
    
    /**
     * 根据题目ID查询测试用例列表
     */
    @GetMapping("/problem/{problemId}")
    @PreAuthorize("hasAuthority('TESTCASE.LIST')")
    @Operation(summary = "根据题目ID查询测试用例列表")
    public ResponseResult<List<TestCaseVO>> getTestCasesByProblemId(@PathVariable Long problemId) {
        List<TestCaseVO> testCases = testCaseService.getTestCasesByProblemId(problemId);
        return ResponseResult.success(testCases);
    }
    
    /**
     * 新增测试用例
     */
    @PostMapping
    @PreAuthorize("hasAuthority('TESTCASE.ADD')")
    @Operation(summary = "新增测试用例")
    public ResponseResult<Void> addTestCase(@Valid @RequestBody TestCaseSaveDTO saveDTO) {
        testCaseService.saveTestCase(saveDTO);
        return ResponseResult.success();
    }
    
    /**
     * 修改测试用例
     */
    @PutMapping
    @PreAuthorize("hasAuthority('TESTCASE.EDIT')")
    @Operation(summary = "修改测试用例")
    public ResponseResult<Void> updateTestCase(@Valid @RequestBody TestCaseSaveDTO saveDTO) {
        testCaseService.updateTestCase(saveDTO);
        return ResponseResult.success();
    }
    
    /**
     * 删除测试用例
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TESTCASE.DELETE')")
    @Operation(summary = "删除测试用例")
    public ResponseResult<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseResult.success();
    }
    
    /**
     * 批量删除测试用例
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('TESTCASE.DELETE')")
    @Operation(summary = "批量删除测试用例")
    public ResponseResult<Void> batchDeleteTestCases(@RequestBody List<Long> ids) {
        testCaseService.deleteTestCases(ids);
        return ResponseResult.success();
    }
}

