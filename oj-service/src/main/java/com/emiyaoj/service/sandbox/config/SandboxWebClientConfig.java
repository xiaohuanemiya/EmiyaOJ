package com.emiyaoj.service.sandbox.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 沙箱 WebClient 配置
 */
@Configuration
public class SandboxWebClientConfig {
    
    @Bean
    public WebClient sandboxWebClient(SandboxProperties sandboxProperties) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, sandboxProperties.getConnectionTimeout())
                .responseTimeout(Duration.ofMillis(sandboxProperties.getReadTimeout()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(sandboxProperties.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(sandboxProperties.getReadTimeout(), TimeUnit.MILLISECONDS))
                );
        
        return WebClient.builder()
                .baseUrl(sandboxProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
