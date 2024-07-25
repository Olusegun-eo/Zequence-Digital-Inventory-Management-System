package com.zequence.ZequenceIms.config;

import com.zequence.ZequenceIms.interceptor.LogRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author Olusegun Emmanuel
 * WebMVC configuration for WMS application to add custom intercepter for logging
 * //TODO======> DON'T KNOW HOW IT WORKS YET
 */

@Configuration
@AllArgsConstructor
public class ZequenceImsAppConfig implements WebMvcConfigurer {

    private LogRequestInterceptor logRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logRequestInterceptor)
                .addPathPatterns("/api/**");//TODO==>fIND OUT WHY THIS
    }
}
