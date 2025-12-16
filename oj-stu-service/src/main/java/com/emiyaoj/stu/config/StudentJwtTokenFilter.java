package com.emiyaoj.stu.config;

import com.emiyaoj.common.constant.JwtClaimsConstant;
import com.emiyaoj.common.exception.CustomerAuthenticationException;
import com.emiyaoj.common.handler.LoginFailureHandler;
import com.emiyaoj.common.properties.JwtProperties;
import com.emiyaoj.common.utils.BaseContext;
import com.emiyaoj.common.utils.JwtUtil;
import com.emiyaoj.common.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 学生端JWT过滤器
 */
@Component
@Slf4j
public class StudentJwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    // 白名单路径列表
    private final String[] whitelist = {
            "/stu/auth/login",
            "/stu/problem/page",
            "/stu/problem/languages",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars",
            "/webjars/**",
            "/doc.html"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 判断当前请求是否在白名单中
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("学生端请求 - 方法: {}, URI: {}", method, uri);
        
        if (isWhitelisted(uri)) {
            log.info("请求在白名单中，直接放行: {} {}", method, uri);
            filterChain.doFilter(request, response);
            return;
        }
        
        log.info("请求不在白名单中，需要token验证: {} {}", method, uri);
        try {
            // 2. 校验token
            this.validateToken(request);
        } catch (AuthenticationException e) {
            log.error("token校验失败: {}", e.getMessage());
            loginFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    // 判断请求路径是否在白名单中
    private boolean isWhitelisted(String uri) {
        // 移除可能的context path和查询参数
        String cleanUri = uri.split("\\?")[0];
        // 标准化URI，移除末尾的斜杠（如果有）
        if (cleanUri.endsWith("/") && cleanUri.length() > 1) {
            cleanUri = cleanUri.substring(0, cleanUri.length() - 1);
        }
        
        log.info("检查URI是否在白名单: {}", cleanUri);
        
        for (String pattern : whitelist) {
            if (pattern.endsWith("/**")) {
                // 处理通配符路径，如 /swagger-ui/**
                String basePattern = pattern.substring(0, pattern.length() - 3);
                if (cleanUri.startsWith(basePattern)) {
                    log.info("匹配通配符模式: {} -> {}", pattern, cleanUri);
                    return true;
                }
            } else {
                // 精确匹配
                if (pattern.equals(cleanUri)) {
                    log.info("精确匹配白名单: {} -> {}", pattern, cleanUri);
                    return true;
                }
            }
        }
        
        log.warn("URI不在白名单中: {}", cleanUri);
        return false;
    }

    // 校验token
    private void validateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("Authorization");
        }
        if (ObjectUtils.isEmpty(token) || token.equals("undefined") || token.equals("null")) {
            throw new CustomerAuthenticationException("token为空");
        }
        
        // 去掉前缀 "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            log.info("学生端JWT校验，token长度: {}", token != null ? token.length() : 0);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            
            // 检查claims中是否有USER_ID
            Object userIdObj = claims.get(JwtClaimsConstant.USER_ID);
            if (userIdObj == null) {
                log.error("JWT claims中缺少USER_ID字段");
                throw new CustomerAuthenticationException("token格式错误：缺少用户ID");
            }
            
            Long userId = Long.valueOf(userIdObj.toString());
            log.info("从JWT解析出用户ID: {}", userId);
            
            // Redis进行校验
            String redisKey = "token_" + token;
            if (!redisUtil.hasKey(redisKey)) {
                log.warn("Redis中不存在token，key: {}", redisKey);
                log.warn("尝试检查Redis连接状态...");
                // 尝试获取token值，看看是否能连接Redis
                Object value = redisUtil.get(redisKey);
                log.warn("Redis get操作结果: {}", value);
                throw new CustomerAuthenticationException("token已过期");
            } else {
                // 刷新token有效期（单位：毫秒）
                redisUtil.set(redisKey, userId.toString(), jwtProperties.getTtl());
                log.info("Token验证成功，已刷新有效期");
            }
            
            log.info("当前学生ID：{}", userId);
            BaseContext.setCurrentId(userId);
            
            // 设置SpringSecurity上下文
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (CustomerAuthenticationException e) {
            // 重新抛出认证异常
            throw e;
        } catch (NumberFormatException e) {
            log.error("用户ID格式错误", e);
            throw new CustomerAuthenticationException("token格式错误：用户ID无效");
        } catch (Exception ex) {
            log.error("token校验失败，异常类型: {}, 异常信息: {}", ex.getClass().getName(), ex.getMessage(), ex);
            throw new CustomerAuthenticationException("token校验失败: " + ex.getMessage());
        }
    }
}

