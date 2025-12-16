package com.emiyaoj.stu.controller;

import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.stu.domain.dto.ProblemQueryDTO;
import com.emiyaoj.stu.domain.vo.LanguageVO;
import com.emiyaoj.stu.domain.vo.ProblemVO;
import com.emiyaoj.stu.service.IStudentProblemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生题目控制器
 */
@RestController
@RequestMapping("/stu/problem")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StudentProblemController {
    
    private final IStudentProblemService studentProblemService;
    
    @GetMapping("/page")
    @Operation(summary = "分页查询题目列表")
    public ResponseResult<PageVO<ProblemVO>> getProblemPage(ProblemQueryDTO queryDTO) {
        PageVO<ProblemVO> pageVO = studentProblemService.getProblemPage(queryDTO);
        return ResponseResult.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询题目详情")
    public ResponseResult<ProblemVO> getProblemById(@PathVariable Long id) {
        ProblemVO problemVO = studentProblemService.getProblemById(id);
        return ResponseResult.success(problemVO);
    }
    
    @GetMapping("/languages")
    @Operation(summary = "获取所有启用的编程语言")
    public ResponseResult<List<LanguageVO>> getAvailableLanguages() {
        List<LanguageVO> languages = studentProblemService.getAvailableLanguages();
        return ResponseResult.success(languages);
    }
}

