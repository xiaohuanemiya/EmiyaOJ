package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.TestCaseSaveDTO;
import com.emiyaoj.service.domain.vo.TestCaseVO;
import com.emiyaoj.service.service.ITestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试用例Controller
 */
@Tag(name = "测试用例管理")
@RestController
@RequestMapping("/testcase")
@RequiredArgsConstructor
public class TestCaseController {

    private final ITestCaseService testCaseService;

    @Operation(summary = "根据题目ID获取测试用例列表")
    @GetMapping("/problem/{problemId}")
    public ResponseResult<List<TestCaseVO>> getTestCasesByProblemId(@PathVariable Long problemId) {
        List<TestCaseVO> result = testCaseService.selectTestCasesByProblemId(problemId);
        return ResponseResult.success(result);
    }

    @Operation(summary = "获取测试用例详情")
    @GetMapping("/{id}")
    public ResponseResult<TestCaseVO> getTestCaseDetail(@PathVariable Long id) {
        TestCaseVO vo = testCaseService.selectTestCaseById(id);
        if (vo == null) {
            return ResponseResult.fail("测试用例不存在");
        }
        return ResponseResult.success(vo);
    }

    @Operation(summary = "新增测试用例")
    @PostMapping
    @PreAuthorize("hasAuthority('TESTCASE.ADD')")
    public ResponseResult<Void> saveTestCase(@RequestBody @Valid TestCaseSaveDTO saveDTO) {
        boolean result = testCaseService.saveTestCase(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("新增测试用例失败");
    }

    @Operation(summary = "修改测试用例")
    @PutMapping
    @PreAuthorize("hasAuthority('TESTCASE.EDIT')")
    public ResponseResult<Void> updateTestCase(@RequestBody @Valid TestCaseSaveDTO saveDTO) {
        if (saveDTO.getId() == null) {
            return ResponseResult.fail("测试用例ID不能为空");
        }
        boolean result = testCaseService.updateTestCase(saveDTO);
        return result ? ResponseResult.success() : ResponseResult.fail("修改测试用例失败");
    }

    @Operation(summary = "删除测试用例")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TESTCASE.DELETE')")
    public ResponseResult<Void> deleteTestCase(@PathVariable Long id) {
        boolean result = testCaseService.deleteTestCase(id);
        return result ? ResponseResult.success() : ResponseResult.fail("删除测试用例失败");
    }

    @Operation(summary = "批量删除测试用例")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('TESTCASE.DELETE')")
    public ResponseResult<Void> deleteTestCases(@RequestBody List<Long> ids) {
        boolean result = testCaseService.deleteTestCases(ids);
        return result ? ResponseResult.success() : ResponseResult.fail("批量删除测试用例失败");
    }

    @Operation(summary = "根据题目ID删除所有测试用例")
    @DeleteMapping("/problem/{problemId}")
    @PreAuthorize("hasAuthority('TESTCASE.DELETE')")
    public ResponseResult<Void> deleteTestCasesByProblemId(@PathVariable Long problemId) {
        boolean result = testCaseService.deleteTestCasesByProblemId(problemId);
        return result ? ResponseResult.success() : ResponseResult.fail("删除测试用例失败");
    }
}
