package com.yuban.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将上传的文件目录映射为可访问的静态资源
        String imagesPath = "file:" + uploadPath.replace("\\", "/") + "/images/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath, "classpath:/static/images/");
        
        // 确保用户头像可以被访问
        String usersPath = "file:" + uploadPath.replace("\\", "/") + "/images/users/";
        registry.addResourceHandler("/images/users/**")
                .addResourceLocations(usersPath, "classpath:/static/images/users/");
    }
}