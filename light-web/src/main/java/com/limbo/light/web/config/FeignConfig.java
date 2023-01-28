package com.limbo.light.web.config;

import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Feign config
 *
 * @author limbo
 * @date 2023/01/11
 */
@Configuration
public class FeignConfig {

    /**
     * feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(@Autowired DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

}
