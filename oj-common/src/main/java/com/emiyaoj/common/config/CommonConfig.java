package com.emiyaoj.common.config;

import com.emiyaoj.common.properties.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.emiyaoj.common")
public class CommonConfig {
    // 公共配置类，可添加公共Bean

    @Bean
    public String helloMessage() {
        return "Hello, World!";
    }
}
