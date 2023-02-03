package com.limbo.light.web.aspect;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * mybatis-plus aspect
 *
 * @author limbo
 * @date 2023/02/03
 */
@Slf4j
@Aspect
@Component
@Order(99)
public class MybatisAspect {

    @Pointcut("execution(public * com.baomidou.mybatisplus.core.mapper.BaseMapper.selectOne(..))")
    public void selectOnePoint() {

    }

    /**
     * 对于 MybatisPlus selectOne 做 limit 1 限制避免出现多个记录后报错
     */
    @Before("selectOnePoint()")
    public void selectLimitOne(JoinPoint joinPoint) {
        try {
            Object arg = joinPoint.getArgs()[0];
            if (arg instanceof AbstractWrapper) {
                ((AbstractWrapper<?, ?, ?>) arg).last("limit 1");
            }
        } catch (Exception e) {
            log.warn("mybatis-plus aspect selectLimitOne exception", e);
        }
    }

}
