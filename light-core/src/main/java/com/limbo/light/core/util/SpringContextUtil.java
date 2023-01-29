package com.limbo.light.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class SpringContextUtil implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext ctx;
    private static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        SpringContextUtil.ctx = ctx;
    }

    @Override
    public void setEnvironment(Environment environment) {
       SpringContextUtil.env = environment;
    }

    public static ApplicationContext getApplicationContext() {
        return SpringContextUtil.ctx;
    }

    /**
     * 获取配置文件变量
     * ContextUtil.getEnvironment().getProperty("spring.job.name", "demo")
     *
     * @return {@link Environment}
     */
    public static Environment getEnvironmentAware() {
        return SpringContextUtil.env;
    }

    public static <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }

}
