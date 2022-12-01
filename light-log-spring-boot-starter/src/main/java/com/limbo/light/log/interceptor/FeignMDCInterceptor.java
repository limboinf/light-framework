package com.limbo.light.log.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import static com.limbo.light.log.Const.TRACE_ID;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 20:20
 */
public class FeignMDCInterceptor implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String traceId = MDC.get(TRACE_ID);

        if(null != traceId && traceId.trim().length() > 0){
            requestTemplate.header(TRACE_ID, traceId);
            requestTemplate.header("appName", appName);
        }
    }
}
