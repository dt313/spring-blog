package com.blog.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        String path = "/Users/dt0803/Documents/WorkSpace/Spring Boot/api/src/main/resources/static/images";
        registry.addResourceHandler("/content/**").addResourceLocations(path);
    }
}
