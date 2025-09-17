package com.emiyaoj.common.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmiyaUserDetailsService implements UserDetailsService {

    // TODO: 注入实际的用户服务，这里先用模拟数据

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟用户数据
        if ("user".equals(username)) {
            return org.springframework.security.core.userdetails.User
                    .withUsername("user")
                    .password("{noop}password") // {noop}表示不加密
                    .roles("USER")
                    .build();
        } else if ("admin".equals(username)) {
            return org.springframework.security.core.userdetails.User
                    .withUsername("admin")
                    .password("{noop}admin") // {noop}表示不加密
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException("用户未找到: " + username);
        }
    }
}
