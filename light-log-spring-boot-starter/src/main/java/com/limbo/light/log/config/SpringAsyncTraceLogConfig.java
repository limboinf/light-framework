package com.limbo.light.log.config;

import com.limbo.light.log.interceptor.AsyncMDCTaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 20:28
 */
public class SpringAsyncTraceLogConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setTaskDecorator(new AsyncMDCTaskDecorator());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
