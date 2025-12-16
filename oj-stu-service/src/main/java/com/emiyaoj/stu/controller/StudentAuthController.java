package com.emiyaoj.stu.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.stu.domain.dto.StudentLoginDTO;
import com.emiyaoj.stu.domain.vo.StudentLoginVO;
import com.emiyaoj.stu.service.IStudentAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 学生认证控制器
 */
@RestController
@RequestMapping("/stu/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StudentAuthController {
    
    private final IStudentAuthService studentAuthService;
    
    @PostMapping("/login")
    @Operation(summary = "学生登录")
    public ResponseResult<StudentLoginVO> login(@RequestBody StudentLoginDTO loginDTO) {
        log.info("========== 学生登录Controller收到请求 ==========");
        log.info("学生登录请求: {}", loginDTO);
        try {
            StudentLoginVO loginVO = studentAuthService.login(loginDTO);
            log.info("登录成功，返回token: {}", loginVO.getToken() != null ? "有token" : "token为空");
            return ResponseResult.success(loginVO);
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @PostMapping("/logout")
    @Operation(summary = "学生退出登录")
    public ResponseResult<Void> logout() {
        log.info("学生ID：{}，退出登录", BaseContext.getCurrentId());
        studentAuthService.logout();
        return ResponseResult.success();
    }
}

