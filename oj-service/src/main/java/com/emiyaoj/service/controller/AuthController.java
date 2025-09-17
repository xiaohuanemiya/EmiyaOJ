package com.emiyaoj.service.controller;

import com.emiyaoj.service.domain.vo.LoginVO;
import com.emiyaoj.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // 这里简化处理，实际项目中应该从数据库验证用户
        // 模拟用户验证：用户名为admin，密码为password
        if ("admin".equals(username) && "password".equals(password)) {
            Long userId = 1L; // 模拟用户ID
            String token = jwtUtils.generateToken(username, userId);
            return ResponseEntity.ok(new LoginVO(token, username, userId));
        }

        return ResponseEntity.badRequest().body("用户名或密码错误");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        // 这个接口需要认证才能访问
        return ResponseEntity.ok("用户个人信息 - 需要JWT认证");
    }

    @PostMapping("/test-encode")
    public ResponseEntity<?> testPasswordEncoder(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String encoded = passwordEncoder.encode(password);
        return ResponseEntity.ok(Map.of("original", password, "encoded", encoded));
    }
}
