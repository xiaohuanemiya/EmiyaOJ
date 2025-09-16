package com.emiyaoj.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    // 公共配置类，可添加公共Bean

    @Bean
    public String helloMessage() {
        return "Hello, World!";
    }
}
