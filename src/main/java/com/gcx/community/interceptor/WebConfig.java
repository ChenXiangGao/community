package com.gcx.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 使用了注解后，我们就不用实例化sessionInterceptor了，直接注入就行
    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对传入的Interceptor对象进行设置，那些路径被拦截，哪些路径被放过
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");

    }
}
