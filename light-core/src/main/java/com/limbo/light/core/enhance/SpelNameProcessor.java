package com.limbo.light.core.enhance;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 表达式解析器
 *
 * @author limbo
 * @since 2022/12/21 18:21
 */
public interface SpelNameProcessor {

    /**
     * 解析
     *
     * @param args   args 拦截器对象
     * @param method 方法
     * @param key    key表达式
     * @return {@link String}
     */
    String parse(Object[] args, Method method, String key);

    List<String> parseKeys(Object[] args, Method method, String[] keys);
    

}
