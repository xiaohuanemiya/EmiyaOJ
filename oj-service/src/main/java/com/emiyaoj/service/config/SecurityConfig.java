package com.emiyaoj.service.config;

import com.emiyaoj.common.handler.AnonymousAuthenticationHandler;
import com.emiyaoj.common.handler.CustomerAccessDeniedHandler;
import com.emiyaoj.common.handler.LoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @description SpringSecurity配置类
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 开启SpringSecurity的自定义配置（在SpringBoot项目中可以省略）
@EnableMethodSecurity // 开启全局函数权限
public class SecurityConfig {

    // 自定义的用于认证的过滤器，进行jwt的校验操作
    private final JwtTokenOncePerRequestFilter jwtTokenFilter;

    // 认证用户无权限访问资源的处理器
    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;

    // 客户端进行认证数据的提交时出现异常，或者是匿名用户访问受限资源的处理器
    private final AnonymousAuthenticationHandler anonymousAuthentication;

    // 用户认证校验失败处理器
    private final LoginFailureHandler loginFailureHandler;


    /**
     * 创建BCryptPasswordEncoder注入容器，用于密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录时调用AuthenticationManager.authenticate执行一次校验
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 配置CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 允许所有来源
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 配置CORS
        http.cors(configurer -> configurer.configurationSource(corsConfigurationSource()));

        // 添加自定义异常处理类
        http.exceptionHandling(configurer -> {
            configurer.accessDeniedHandler(customerAccessDeniedHandler) // 配置认证用户无权限访问资源的处理器
                    .authenticationEntryPoint(anonymousAuthentication); // 配置匿名用户未认证的处理器
        });

        // 配置关闭csrf机制
        http.csrf(AbstractHttpConfigurer::disable);
        // 用户认证校验失败处理器
        http.formLogin(conf -> conf.failureHandler(loginFailureHandler));
        // STATELESS（无状态）：表示应用程序是无状态的，不创建会话
        http.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 配置放行路径
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/swagger-ui/**", // 放行Swagger相关路径
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/doc.html",
                        "/auth/login",  // 放行登录接口路径
                        "/client/problem/page",  // 放行题目列表查询
                        "/client/problem/**",  // 放行题目详情查询
                        "/client/language/**",  // 放行语言查询
                        "/client/submission/page",  // 放行提交列表查询
                        "/client/submission/*"  // 放行提交详情查询
                ).permitAll()
                .anyRequest().authenticated()
        );
        // 配置过滤器的执行顺序
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
