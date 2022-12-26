package com.limbo.light.lightlockspringbootstarter.support.core;

import com.limbo.light.lightlockspringbootstarter.support.annotation.LightLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 获取用户自定义key
 *
 * @author limbo
 * @since 2022/12/26 16:42
 */
public class BusinessKeyProvider {

    public String getKeyName(JoinPoint joinPoint, LightLock lightLock) {
        return null;
    }

    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            method = joinPoint.getClass().getDeclaredMethod(
                    methodSignature.getName(), methodSignature.getParameterTypes());
        }
        return method;
    }

}
