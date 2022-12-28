package com.limbo.light.core.enhance.impl;

import com.limbo.light.core.enhance.SpELProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的spel解析
 *
 * @author limbo
 * @since 2022/12/21 18:23
 */
@SuppressWarnings("all")
public class DefaultSpELProcessor<T> implements SpELProcessor<T> {

    private EvaluationContext context;

    public DefaultSpELProcessor(EvaluationContext context) {
        this.context = context;
    }

    @Override
    public T parse(String express, Class<T> clz) {

        if (StringUtils.isBlank(express)) {
            throw new IllegalArgumentException("express is empty");
        }

        return PARSER.parseExpression(express).getValue(context, clz);
    }

    @Override
    public List<T> parseKeys(String[] expressList, Class<T> clz) {
        List<T> res = new ArrayList<>();
        for (String express : expressList) {
            res.add(parse(express, clz));
        }
        return res;
    }
}
