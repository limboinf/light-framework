package com.limbo.light.lightlockspringbootstarter.support.aspect;

import com.limbo.light.lightlockspringbootstarter.support.annotation.LightLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Light Lock aop
 *
 * @author limbo
 * @since 2022/12/26 16:20
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class LightLockAspect {


    @Around("@annotation(lightLock)")
    public Object around(ProceedingJoinPoint joinPoint, LightLock lightLock) throws Throwable {
        return joinPoint.proceed();
    }

    @AfterReturning("@annotation(lightLock)")
    public void afterReturning(JoinPoint joinPoint, LightLock lightLock) throws Throwable {

    }

    @AfterThrowing(value = "@annotation(lightLock)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, LightLock lightLock, Throwable ex) throws Throwable {

    }
}
