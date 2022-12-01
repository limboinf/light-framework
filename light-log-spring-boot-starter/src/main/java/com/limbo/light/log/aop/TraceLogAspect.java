package com.limbo.light.log.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.UUID;

import static com.limbo.light.log.Const.TRACE_ID;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 19:57
 */
@Aspect
@Order(-999)
public class TraceLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceLogAspect.class);

    @Pointcut(
            "@annotation(com.limbo.light.log.aop.TraceId) || " +
            "@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void logPoint() {
        // ignored
    }

    @Before("logPoint()")
    public void beforeSetLogTrace(JoinPoint joinPoint) {
        String traceId = MDC.get(TRACE_ID);
        if (null == traceId || traceId.isEmpty()) {
            traceId = genTraceId();
            MDC.put(TRACE_ID, traceId);
        }
        String args = Arrays.toString(joinPoint.getArgs());
        String method = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
        LOGGER.info("--**--> [{}] method: {}, args: {} ", traceId, method, args);
    }

    @After("logPoint()")
    public void doAfter(JoinPoint joinPoint) {
        MDC.clear();
    }

    private static String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
