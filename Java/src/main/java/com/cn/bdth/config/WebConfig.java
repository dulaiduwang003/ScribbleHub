package com.cn.bdth.config;

import com.cn.bdth.enums.FileEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用于文件资源访问
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String path;

    /**
     * 添加处理器映射
     *
     * @param registry the registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(
                        "file:" + path + FileEnum.BLOG.getDec() + "/",
                        "file:" + path + FileEnum.PAINTING.getDec()+ "/",
                        "file:" + path + FileEnum.COVER.getDec()+ "/",
                        "file:" + path + FileEnum.AVATAR.getDec()+ "/");
    }


}
