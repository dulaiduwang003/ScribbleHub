package com.cn.app.superbot.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间序列化配置
 * The type Local date time serializer config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
public class LocalDateTimeConfig {


    /**
     * 本地日期时间反序列化
     * Local date time deserializer local date time serializer.
     *
     * @return the local date time serializer
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * jackson 2对象映射器
     * Jackson 2 object mapper builder customizer jackson 2 object mapper builder customizer.
     *
     * @return the jackson 2 object mapper builder customizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }

}
