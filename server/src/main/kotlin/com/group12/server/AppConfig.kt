package com.group12.server

import com.group12.server.interceptor.RateLimiterInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig : WebMvcConfigurer{

     val interceptor: RateLimiterInterceptor = RateLimiterInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(interceptor).addPathPatterns("/user/**")
    }

}