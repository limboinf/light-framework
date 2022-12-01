package com.limbo.light.log.config;

import com.limbo.light.log.interceptor.FeignMDCInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 20:19
 */
@Configuration
@ConditionalOnClass(name = {"feign.RequestInterceptor"})
public class FeignLogAutoConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignMDCInterceptor();
    }
}
