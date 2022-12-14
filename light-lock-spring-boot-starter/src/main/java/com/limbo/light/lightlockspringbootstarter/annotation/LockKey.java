package com.limbo.light.lightlockspringbootstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义业务key
 *
 * @author limbo
 * @since 2022/12/26 16:16
 */
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LockKey {

    /**
     * 一般value不传，即使要传必须是 #root 引用根对象，或者 #this 引用当前上下文对象
     *
     * @return {@link String}
     */
    String value() default "";

}
