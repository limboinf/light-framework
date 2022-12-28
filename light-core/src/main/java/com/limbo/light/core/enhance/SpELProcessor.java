package com.limbo.light.core.enhance;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

/**
 * 表达式解析器
 *
 * @author limbo
 * @since 2022/12/21 18:21
 */
public interface SpELProcessor<T> {

    /**
     * 参数名称发现器
     */
    ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * SPEL解析器
     */
    ExpressionParser PARSER = new SpelExpressionParser();

    T parse(String express, Class<T> clz);

    List<T> parseKeys(String[] expressList, Class<T> clz);

}
