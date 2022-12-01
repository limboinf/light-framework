package com.limbo.light.log.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

import static com.limbo.light.log.Const.TRACE_ID;

/**
 * 访问日志切面
 */
@Aspect
@Order(10)
public class AccessLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogAspect.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Pointcut(value = "bean(*Controller) && !bean(healthController)")
    public void opLog() {
        // ignore
    }

    @Around("opLog()")
    public Object getAroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object invoke = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        WebLog webLog = new WebLog();
        try {
            before(attributes, joinPoint, webLog);
            invoke = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        } finally {
            after(attributes, joinPoint, webLog, invoke);
        }
        return invoke;
    }

    /**
     * before
     */
    private void before(ServletRequestAttributes attributes, ProceedingJoinPoint joinPoint, WebLog webLog) {
        try {
            HttpServletRequest request = attributes.getRequest();
            MDC.put(TRACE_ID, webLog.traceId);
            webLog.setArgs(Arrays.toString(joinPoint.getArgs()));
            webLog.setClassMethod(joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName());
            webLog.setHttpMethod(request.getMethod());
            webLog.setUrl(request.getRequestURL().toString());
            webLog.setParams(request.getQueryString());
            LOGGER.info("----> {}", webLog);
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
        }
    }

    private void after(ServletRequestAttributes attributes, ProceedingJoinPoint joinPoint, WebLog webLog, Object invoke) {
        try {
            HttpServletResponse response = attributes.getResponse();
            if (invoke != null && response != null) {
                webLog.setResponseCode(response.getStatus());
                String res = mapper.writeValueAsString(invoke);
                res = res.length() > 3500 ? res.substring(0, 3500) + "...超大截断..." : res;
                webLog.setResponseBody(res);
            }
            webLog.setCost(String.format("%dms", System.currentTimeMillis() - webLog.getStartTime()));
            LOGGER.info("<---- {}", webLog);
        } catch (Throwable t) {
            LOGGER.error(t.getMessage());
        }
    }

    private static class WebLog {
        private long startTime;
        private String traceId;
        private String cost;
        private String url;
        private String httpMethod;
        private String classMethod;
        private String args;
        private String params;
        private String responseBody;
        private Integer responseCode;
        private String exception;

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        public String getClassMethod() {
            return classMethod;
        }

        public void setClassMethod(String classMethod) {
            this.classMethod = classMethod;
        }

        public String getArgs() {
            return args;
        }

        public void setArgs(String args) {
            this.args = args;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }

        public Integer getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(Integer responseCode) {
            this.responseCode = responseCode;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "WebLog{" +
                    "traceId='" + traceId + '\'' +
                    ", cost='" + cost + '\'' +
                    ", url='" + url + '\'' +
                    ", httpMethod='" + httpMethod + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args='" + args + '\'' +
                    ", params='" + params + '\'' +
                    ", responseBody='" + responseBody + '\'' +
                    ", responseCode=" + responseCode +
                    '}';
        }

        WebLog() {
            traceId = genTraceId();
            startTime = System.currentTimeMillis();
        }

    }

    private static String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
