// AppConfig.java
package com.emiyaoj.service.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 获取原有的消息转换器
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();

        // 移除原有的Jackson转换器
        converters.removeIf(converter ->
                converter.getClass().getName().contains("MappingJackson2HttpMessageConverter"));

        // 添加FastJSON转换器
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(
                com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue,
                com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty,
                com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero,
                com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty,
                com.alibaba.fastjson.serializer.SerializerFeature.WriteNullBooleanAsFalse,
                com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect,
                com.alibaba.fastjson.serializer.SerializerFeature.WriteEnumUsingToString
        );

        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(mediaTypes);

        converters.add(fastJsonConverter);

        restTemplate.setMessageConverters(converters);
        return restTemplate;
    }
}