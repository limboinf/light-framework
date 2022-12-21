package com.limbo.light.core.enhance.impl;

import com.limbo.light.core.enhance.SpelNameProcessor;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * spel解析
 *
 * @author limbo
 * @since 2022/12/21 18:23
 */
@SuppressWarnings("all")
public class NameSpelExpressionProcessor implements SpelNameProcessor {

    /**
     * 参数名称发现器
     */
    public static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * SPEL解析器
     */
    public static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public String parse(Object[] args, Method method, String key) {

        if (!key.contains("#")) {
            return key;
        }

        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(null, method, args, NAME_DISCOVERER);
        Object value = PARSER.parseExpression(key).getValue(context);
        return Objects.isNull(value) ? null : Objects.toString(value);
    }
}
