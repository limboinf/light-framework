package com.limbo.light.core.builder;

import java.io.Serializable;

/**
 * 构造器接口
 *
 * @author limbo
 * @since 2022/12/2 18:29
 */
public interface BaseBuilder<T> extends Serializable {

    T build();

}
