package com.limbo.light.log.aop;

import java.lang.annotation.*;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/1 19:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceId {
}
