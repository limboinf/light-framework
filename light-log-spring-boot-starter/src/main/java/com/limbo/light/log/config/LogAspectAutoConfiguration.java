package com.limbo.light.log.config;

import com.limbo.light.log.aop.AccessLogAspect;
import com.limbo.light.log.aop.TraceLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 20:44
 */
@Configuration
public class LogAspectAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TraceLogAspect.class)
    public TraceLogAspect traceLogAspect() {
        return new TraceLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(AccessLogAspect.class)
    public AccessLogAspect accessLogAspect() {
        return new AccessLogAspect();
    }
}
