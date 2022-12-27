package com.limbo.light.lightlockspringbootstarter.aspect;

import com.limbo.light.lightlockspringbootstarter.annotation.LightLock;
import com.limbo.light.lightlockspringbootstarter.core.LockInfoProvider;
import com.limbo.light.lightlockspringbootstarter.domain.LightLockException;
import com.limbo.light.lightlockspringbootstarter.domain.LockRes;
import com.limbo.light.lightlockspringbootstarter.lock.Lock;
import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import com.limbo.light.lightlockspringbootstarter.lock.LockFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    @Autowired
    private LockFactory lockFactory;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    private final Map<String, LockRes> currentThreadLock = new ConcurrentHashMap<>();

    @Around("@annotation(lightLock)")
    public Object around(ProceedingJoinPoint joinPoint, LightLock lightLock) throws Throwable {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, lightLock);
        log.info("尝试获得锁: {} ...", lockInfo);
        String currentLock = getCurrentLockId(joinPoint, lightLock);
        currentThreadLock.put(currentLock, new LockRes(lockInfo, null, Boolean.FALSE));

        Lock lock = lockFactory.getLockInstance(lockInfo);
        boolean isAcquire = lock.acquire();

        // 如果获取锁失败则进入失败逻辑
        if (Boolean.FALSE.equals(isAcquire)) {
            log.warn("获取锁失败: [{}]", lockInfo);
            if (StringUtils.isNotBlank(lightLock.customLockTimeoutStrategy())) {
                // 处理自定义超时策略
                return handleCustomLockStrategy(lightLock.customLockTimeoutStrategy(), joinPoint);
            } else {
                // 执行默认策略
                lightLock.lockTimeoutStrategy().handle(lockInfo, lock, joinPoint);
            }
        }

        log.info("获得锁成功: {}", lockInfo);
        currentThreadLock.get(currentLock).setLock(lock);
        currentThreadLock.get(currentLock).setState(Boolean.TRUE);

        return joinPoint.proceed();
    }

    @AfterReturning("@annotation(lightLock)")
    public void afterReturning(JoinPoint joinPoint, LightLock lightLock) throws Throwable {
        String currentLockId = getCurrentLockId(joinPoint, lightLock);
        try {
            releaseLock(lightLock, joinPoint, currentLockId);
        } finally {
            cleanThreadMap(currentLockId);
        }
    }

    @AfterThrowing(value = "@annotation(lightLock)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, LightLock lightLock, Throwable ex) throws Throwable {
        String currentLockId = getCurrentLockId(joinPoint, lightLock);
        try {
            releaseLock(lightLock, joinPoint, currentLockId);
        } finally {
            cleanThreadMap(currentLockId);
            throw ex;
        }
    }

    /**
     * 获取当前锁ID
     */
    private String getCurrentLockId(JoinPoint joinPoint, LightLock lock) {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, lock);
        return Thread.currentThread().getId() + "-" + lockInfo.getName();
    }

    /**
     * 释放锁
     */
    private void releaseLock(LightLock lightLock, JoinPoint joinPoint, String currentLockId) throws Throwable {
        LockRes lockRes = currentThreadLock.get(currentLockId);
        if (Objects.isNull(lockRes)) {
            throw new RuntimeException(String.format("找不到该线程对应的LockRes实例: currentLockId: [%s]", currentLockId));
        }

        // 判断上下文中是否拿到了锁，拿到则去释放
        if (Boolean.TRUE.equals(lockRes.getState())) {
            boolean releaseState = lockRes.getLock().release();
            lockRes.setState(releaseState);
            if (Boolean.FALSE.equals(releaseState)) {
                handleReleaseTimeout(lightLock, lockRes.getLockInfo(), joinPoint);
            } else {
                log.info("释放锁成功: {}", lockRes.getLockInfo());
            }
        }
    }


    /**
     * 处理释放超时
     */
    private void handleReleaseTimeout(LightLock lightLock, LockInfo lockInfo, JoinPoint joinPoint) throws Throwable {
        log.warn("释放锁失败: {}", lockInfo);
        if (StringUtils.isNotBlank(lightLock.customReleaseTimeoutStrategy())) {
            // 处理自定义释放锁超时策略
            handleCustomLockStrategy(lightLock.customReleaseTimeoutStrategy(), joinPoint);
        } else {
            // 处理默认策略
            lightLock.releaseTimeoutStrategy().handle(lockInfo);
        }
    }


    private void cleanThreadMap(String currentLockId) {
        if (StringUtils.isNotBlank(currentLockId)) {
            log.debug("clean thread map key: {}", currentLockId);
            currentThreadLock.remove(currentLockId);
        }
    }

    /**
     * 处理自定义锁策略
     *
     * @param customHandler 自定义处理程序
     * @param joinPoint     连接点
     * @return {@link Object}
     */
    private Object handleCustomLockStrategy(String customHandler, JoinPoint joinPoint) throws Throwable {
        log.debug("执行自定义失败处理策略: {}", customHandler);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Method declaredMethod;
        try {
            declaredMethod = target.getClass().getDeclaredMethod(customHandler, methodSignature.getParameterTypes());
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            log.error("Not found the declared method: {} in class : {}", customHandler, target.getClass().getName());
            throw new IllegalArgumentException(String.format("Method: [%s] not found", customHandler), e);
        }

        Object res;
        try {
            res = declaredMethod.invoke(target, joinPoint.getArgs());
        } catch (IllegalAccessException e) {
            log.error("declared method: {} invoke error in class : {}", customHandler, target.getClass().getName());
            throw new LightLockException(String.format("Method: [%s] invoke error", customHandler), e);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
        return res;
    }

}