package com.emiyaoj.common.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Fastjson2配置类
 * 配置Spring Boot使用Fastjson2替代Jackson进行JSON序列化
 * Long类型的id字段将通过@JSONField注解序列化为String
 */
@Configuration
public class MyFastJsonConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建Fastjson2的消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        
        // 创建Fastjson2配置对象
        FastJsonConfig config = getFastJsonConfig();

        // 设置支持的MediaType
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setFastJsonConfig(config);
        
        // 将Fastjson2转换器添加到转换器列表的首位，优先使用
        converters.addFirst(converter);
    }

    private static FastJsonConfig getFastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();

        // 设置字符集
        config.setCharset(StandardCharsets.UTF_8);

        // 设置日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 设置序列化特性（全局配置）
        config.setWriterFeatures(
                // 美化输出
                JSONWriter.Feature.PrettyFormat,
                // 写入Map时保留null值
                JSONWriter.Feature.WriteMapNullValue,
                // 写入List时保留null值
                JSONWriter.Feature.WriteNullListAsEmpty
        );

        // 设置反序列化特性
        config.setReaderFeatures(
                // 支持数组转Bean
                JSONReader.Feature.SupportArrayToBean,
                // 支持智能匹配（包括字符串到数字的自动转换）
                JSONReader.Feature.SupportSmartMatch
        );
        return config;
    }
}

