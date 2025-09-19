package com.emiyaoj.service.controller;

import com.emiyaoj.common.domain.ResponseResult;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.common.utils.RedisUtil;
import com.emiyaoj.service.domain.dto.UserLoginDTO;
import com.emiyaoj.service.domain.vo.UserLoginVO;
import com.emiyaoj.service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin // 允许跨域
public class AuthController {

    private final IUserService userService;
    private final RedisUtil redisUtil;

    @PostMapping("/login")
    public ResponseResult<UserLoginVO> login(@RequestBody UserLoginDTO loginDTO){
        log.info("登录请求: {}", loginDTO);
        UserLoginVO loginVO = userService.login(loginDTO);
        return ResponseResult.success(loginVO);
    }

    /**
     * 员工退出登录
     * @return  统一返回结果
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public ResponseResult logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("员工ID：{}，退出登录", BaseContext.getCurrentId());

        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) { // header没有token
            token = request.getParameter("Authorization");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 清除上下文
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            // 清理redis
            redisUtil.delete("token_" + token);
            // 清理ThreadLocal
            BaseContext.remove();

        }
        return ResponseResult.success();
    }
}
