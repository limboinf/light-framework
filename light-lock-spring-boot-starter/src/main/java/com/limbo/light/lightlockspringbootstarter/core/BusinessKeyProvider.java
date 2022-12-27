package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.annotation.LightLock;
import com.limbo.light.lightlockspringbootstarter.annotation.LockKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 获取用户自定义key
 *
 * @author limbo
 * @since 2022/12/26 16:42
 */
@Slf4j
public class BusinessKeyProvider {

    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser parser = new SpelExpressionParser();

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
     * get keys
     *
     * @param definitionKeys  自定义业务key
     * @param method          方法
     * @param parameterValues 参数值
     * @return {@link List}<{@link String}>
     */
    private List<String> getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            if (!ObjectUtils.isEmpty(definitionKey)) {
                EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
                Object objKey = parser.parseExpression(definitionKey).getValue(context);
                definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
            }
        }
        return definitionKeyList;
    }

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
                    Object key = parser.parseExpression(lockKey.value()).getValue(context);
                    parameterKeys.add(ObjectUtils.nullSafeToString(key));
                }
            }
        }
        return parameterKeys;
    }

}
