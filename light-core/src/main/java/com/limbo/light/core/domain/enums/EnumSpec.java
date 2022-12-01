package com.limbo.light.core.domain.enums;

import java.util.EnumSet;
import java.util.Objects;

/**
 * enum规范接口
 */
public interface EnumSpec<E extends Enum<E>> {

    static <E extends Enum<E> & EnumSpec<E>> E checkThenOf(int code, Class<E> enumType) {
        E t = of(code, enumType);
        if (t == null) {
            throw new IllegalArgumentException("非法的枚举值：" + code + ",枚举类型：" + enumType.getSimpleName());
        }
        return t;
    }

    static <E extends Enum<E> & EnumSpec<E>> E checkThenOf(Integer code, Class<E> enumType) {
        if (code == null) {
            throw new IllegalArgumentException("非法的枚举值：" + code + ",枚举类型：" + enumType.getSimpleName());
        }
        return checkThenOf(code, enumType);
    }

    static <E extends Enum<E>> EnumSet<E> all(Class<E> enumType) {
        return EnumSet.allOf(enumType);
    }

    static <E extends Enum<E> & EnumSpec<E>> boolean isValid(int code, Class<E> enumType) {
        return all(enumType).stream().anyMatch(e -> e.getCode() == code);
    }

    static <E extends Enum<E> & EnumSpec<E>> E of(int code, Class<E> enumType) {
        return all(enumType).stream().filter(e -> e.getCode() == code).findFirst().orElse(null);
    }

    static <E extends Enum<E> & EnumSpec<E>> E of(int code, Class<E> enumType, E defaultValue) {
        return all(enumType).stream().filter(e -> e.getCode() == code).findFirst().orElse(defaultValue);
    }

    static <E extends Enum<E> & EnumSpec<E>> E of(String name, Class<E> enumType) {
        return all(enumType).stream().filter(e -> Objects.equals(e.getDisplayName(), name)).findFirst().orElse(null);
    }

    int getCode();

    String getDisplayName();


}
