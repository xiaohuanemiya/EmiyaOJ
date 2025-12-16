package com.emiyaoj.stu.config;

import com.emiyaoj.common.handler.AnonymousAuthenticationHandler;
import com.emiyaoj.common.handler.CustomerAccessDeniedHandler;
import com.emiyaoj.common.handler.LoginFailureHandler;
import com.emiyaoj.stu.config.StudentJwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 学生端SpringSecurity配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final StudentJwtTokenFilter jwtTokenFilter;
    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;
    private final AnonymousAuthenticationHandler anonymousAuthentication;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 添加自定义异常处理类
        http.exceptionHandling(configurer -> {
            configurer.accessDeniedHandler(customerAccessDeniedHandler)
                    .authenticationEntryPoint(anonymousAuthentication);
        });

        // 配置关闭csrf机制
        http.csrf(AbstractHttpConfigurer::disable);
        // 用户认证校验失败处理器
        http.formLogin(conf -> conf.failureHandler(loginFailureHandler));
        // STATELESS（无状态）
        http.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // 配置放行路径
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/doc.html",
                        "/stu/auth/login",  // 放行学生登录接口
                        "/stu/problem/page",  // 放行题目列表查询（公开题目）
                        "/stu/problem/languages"  // 放行语言列表查询
                ).permitAll()
                .anyRequest().authenticated()
        );
        
        // 配置过滤器的执行顺序
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

