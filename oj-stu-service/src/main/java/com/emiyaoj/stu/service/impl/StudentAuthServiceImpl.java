package com.emiyaoj.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emiyaoj.common.constant.JwtClaimsConstant;
import com.emiyaoj.common.properties.JwtProperties;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.common.utils.JwtUtil;
import com.emiyaoj.common.utils.RedisUtil;
import com.emiyaoj.service.domain.pojo.User;
import com.emiyaoj.stu.domain.dto.StudentLoginDTO;
import com.emiyaoj.stu.domain.vo.StudentLoginVO;
import com.emiyaoj.stu.mapper.UserMapper;
import com.emiyaoj.stu.service.IStudentAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生认证服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentAuthServiceImpl implements IStudentAuthService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;
    
    @Override
    public StudentLoginVO login(StudentLoginDTO loginDTO) {
        log.info("学生登录请求: {}", loginDTO);
        
        // 1. 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername())
               .eq(User::getDeleted, 0);
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 4. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        
        log.info("开始生成JWT，用户ID: {}", user.getId());
        String token = JwtUtil.createJWT(
            jwtProperties.getSecretKey(),
            jwtProperties.getTtl(),
            claims
        );
        
        if (token == null || token.isEmpty()) {
            log.error("JWT生成失败，token为空");
            throw new RuntimeException("JWT生成失败");
        }
        
        log.info("JWT生成成功，token长度: {}", token.length());
        
        // 5. 将token存入Redis
        String redisKey = "token_" + token;
        long ttlMillis = jwtProperties.getTtl(); // TTL是毫秒
        log.info("准备存储token到Redis，key: {}, TTL: {}毫秒 ({}秒)", redisKey, ttlMillis, ttlMillis / 1000);
        
        // RedisUtil.set(String, String, long) 的第三个参数单位是毫秒
        redisUtil.set(redisKey, user.getId().toString(), ttlMillis);
        
        // 验证是否存储成功
        if (redisUtil.hasKey(redisKey)) {
            log.info("Token已成功存入Redis，key: {}", redisKey);
        } else {
            log.error("Token存储到Redis失败，key: {}", redisKey);
            throw new RuntimeException("Token存储失败，请检查Redis连接");
        }
        
        // 6. 构建返回对象
        StudentLoginVO loginVO = StudentLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .token(token)
                .build();
        
        log.info("登录成功，返回VO，token是否为空: {}", loginVO.getToken() == null || loginVO.getToken().isEmpty());
        return loginVO;
    }
    
    @Override
    public void logout() {
        log.info("学生ID：{}，退出登录", BaseContext.getCurrentId());
        BaseContext.remove();
    }
}

