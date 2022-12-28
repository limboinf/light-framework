package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.core.enhance.SpELProcessor;
import com.limbo.light.core.enhance.impl.DefaultSpELProcessor;
import com.limbo.light.lightlockspringbootstarter.annotation.LightLock;
import com.limbo.light.lightlockspringbootstarter.annotation.LockKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 获取用户自定义key
 *
 * @author limbo
 * @since 2022/12/26 16:42
 */
@Slf4j
@SuppressWarnings("all")
public class BusinessKeyProvider {

    public String getKeyName(JoinPoint joinPoint, LightLock lightLock) {
        List<String> keyList = new ArrayList<>();
        Method method = getMethod(joinPoint);
        List<String> definitionKeys = getSpelDefinitionKey(lightLock.keys(), method, joinPoint.getArgs());
        keyList.addAll(definitionKeys);
        List<String> parameterKeys = getParameterKey(method.getParameters(), joinPoint.getArgs());
        keyList.addAll(parameterKeys);
        return StringUtils.collectionToDelimitedString(keyList,"","-","");
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getClass().getDeclaredMethod(
                        methodSignature.getName(), methodSignature.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error("getMethod error class: [{}]", joinPoint.getClass().getSimpleName(), e);
            }
        }
        return method;
    }

    /**
     * <p>
     * 解析 LightLock注解中的keys() 自定义的业务key
     * </p>
     * @see LightLock
     *
     * @param definitionKeys  自定义业务key
     * @param method          方法
     * @param parameterValues 参数值
     * @return {@link List}<{@link String}>
     */
    private List<String> getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, SpELProcessor.NAME_DISCOVERER);
        List<String> res = new DefaultSpELProcessor<String>(context).parseKeys(definitionKeys, String.class);
        return res.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * <p>
     * 解析 LockKey注解标记的参数列表中获取参数值
     * </p>
     *
     * @see LockKey
     *
     * @param parameters      参数
     * @param parameterValues 参数值
     * @return {@link List}<{@link String}>
     */
    private List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKeys = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            LockKey lockKey = parameters[i].getAnnotation(LockKey.class);
            if (Objects.nonNull(lockKey)) {
                if (lockKey.value().isEmpty()) {
                    Object parameterValue = parameterValues[i];
                    parameterKeys.add(ObjectUtils.nullSafeToString(parameterValue));
                } else {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    String res = new DefaultSpELProcessor<String>(context).parse(lockKey.value(), String.class);
                    parameterKeys.add(ObjectUtils.nullSafeToString(res));
                }
            }
        }
        return parameterKeys;
    }

}
