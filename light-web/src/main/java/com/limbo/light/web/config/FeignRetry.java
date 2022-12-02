package com.limbo.light.web.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Feign重试
 * 在 application.yml 里配置
 */
@Slf4j
public class FeignRetry implements Retryer {

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.error("Feign 重试失败", e);
        throw e;
    }

    @Override
    public Retryer clone() {
        // retry interval time: 200ms
        // max period: 重试间隔按1s递增
        // retry number: 3
        return new Default(200, TimeUnit.SECONDS.toMillis(1), 3);
    }
}
