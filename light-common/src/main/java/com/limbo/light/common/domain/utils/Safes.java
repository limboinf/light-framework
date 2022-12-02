package com.limbo.light.common.domain.utils;

import cn.hutool.core.util.NumberUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;


/**
 * 避免异常的工具类
 */
@Slf4j
public class Safes {

    private Safes() {

    }

    public static <K, V> Map<K, V> of(Map<K, V> source) {
        return Optional.ofNullable(source).orElse(Maps.newHashMapWithExpectedSize(0));
    }

    public static <T> Iterator<T> of(Iterator<T> source) {
        return Optional.ofNullable(source).orElse(Collections.emptyIterator());
    }

    public static <T> Collection<T> of(Collection<T> source) {
        return Optional.ofNullable(source).orElse(Lists.newArrayListWithCapacity(0));
    }

    public static <T> Iterable<T> of(Iterable<T> source) {
        return Optional.ofNullable(source).orElse(Lists.newArrayListWithCapacity(0));
    }

    public static <T> List<T> of(List<T> source) {
        return Optional.ofNullable(source).orElse(Lists.newArrayListWithCapacity(0));
    }

    public static <T> Set<T> of(Set<T> source) {
        return Optional.ofNullable(source).orElse(Sets.newHashSetWithExpectedSize(0));
    }

    public static BigDecimal of(BigDecimal source) {
        return Optional.ofNullable(source).orElse(BigDecimal.ZERO);
    }

    public static String of(String source) {
        return Optional.ofNullable(source).orElse(org.apache.commons.lang3.StringUtils.EMPTY);
    }

    public static String of(String source, String defaultStr) {
        return Optional.ofNullable(source).orElse(defaultStr);
    }

    public static <T> T first(Collection<T> source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        T t = null;
        Iterator<T> iterator = source.iterator();
        if (iterator.hasNext()) {
            t = iterator.next();
        }
        return t;
    }

    public static BigDecimal toBigDecimal(String source, BigDecimal defaultValue) {
        Preconditions.checkNotNull(defaultValue);
        try {
            return new BigDecimal(trimToEmpty(source));
        }
        catch (Exception t) {
            return defaultValue;
        }
    }

    public static int toInt(String source, int defaultValue) {
        if (!hasText(source)) {
            return defaultValue;
        }
        try {
            return NumberUtil.parseInt(trimToEmpty(source));
        }
        catch (Exception t) {
            log.error("toInt error, source: [{}]", source, t);
            return defaultValue;
        }
    }

    public static int toInt(Object source, int defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toInt(source.toString(), defaultValue);
    }

    public static Integer toWrapInteger(Object source, Integer defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toInt(source, Objects.isNull(defaultValue) ? 0 : defaultValue);
    }

    public static Integer toWrapInteger(Object source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return toInt(source, 0);
    }

    public static long toLong(String source, long defaultValue) {
        if (!hasText(source)) {
            return defaultValue;
        }
        try {
            return NumberUtil.parseLong(trimToEmpty(source));
        }
        catch (Exception t) {
            log.error("toLong error, source: [{}]", source, t);
            return defaultValue;
        }
    }

    public static long toLong(Object source, long defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toLong(source.toString(), defaultValue);
    }

    public static Long toWrapLong(Object source, Long defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toLong(source, Objects.isNull(defaultValue) ? 0 : defaultValue);
    }

    public static Long toWrapLong(Object source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return toLong(source, 0);
    }

    public static double toDouble(String source, double defaultValue) {
        if (!hasText(source)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(trimToEmpty(source));
        }
        catch (Exception t) {
            return defaultValue;
        }
    }

    public static double toDouble(Object source, double defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toDouble(source.toString(), defaultValue);
    }

    public static Double toWrapDouble(Object source, Double defaultValue) {
        if (Objects.isNull(source)) {
            return defaultValue;
        }
        return toDouble(source, Objects.isNull(defaultValue) ? 0.0 : defaultValue);
    }

    public static Double toWrapDouble(Object source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return toDouble(source, 0.0);
    }

    public static boolean toBoolean(String source, boolean defaultValue) {
        if (!hasText(source)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(trimToEmpty(source));
        }
        catch (Exception t) {
            return defaultValue;
        }
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    static String trimToEmpty(String str) {
        return str == null ? org.apache.commons.lang3.StringUtils.EMPTY : str.trim();
    }

    public static int positive(Integer i, Integer defaultVal) {
        if (Objects.isNull(i) || i < 0) {
            return defaultVal;
        }
        return i;
    }

}