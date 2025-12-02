package com.emiyaoj.service.controller.client;

import com.emiyaoj.common.domain.PageDTO;
import com.emiyaoj.common.domain.PageVO;
import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.service.domain.dto.ProblemSaveDTO;
import com.emiyaoj.service.domain.vo.ProblemVO;
import com.emiyaoj.service.service.IProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目Controller
 */
@Tag(name = "题目管理")
@RestController
@RequestMapping("/client/problem")
@RequiredArgsConstructor
public class ClientProblemController {

    private final IProblemService problemService;

    @Operation(summary = "分页查询题目")
    @GetMapping("/page")
    public ResponseResult<PageVO<ProblemVO>> pageProblems(PageDTO pageDTO,
                                                          @RequestParam(required = false) Integer difficulty,
                                                          @RequestParam(required = false) Integer status,
                                                          @RequestParam(required = false) String keyword) {
        PageVO<ProblemVO> result = problemService.selectProblemPage(pageDTO, difficulty, status, keyword);
        return ResponseResult.success(result);
    }

    @Operation(summary = "获取题目详情")
    @GetMapping("/{id}")
    public ResponseResult<ProblemVO> getProblemDetail(@PathVariable Long id) {
        ProblemVO vo = problemService.selectProblemById(id);
        if (vo == null) {
            return ResponseResult.fail("题目不存在");
        }
        return ResponseResult.success(vo);
    }

}
