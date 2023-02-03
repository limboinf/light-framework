package com.limbo.light.web.dao.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态
 *
 * @author limbo
 * @date 2023/02/03
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeleteState implements IEnum<Byte> {

    /**
     * 正常状态
     */
    NORMAL((byte) 0),

    /**
     * 已删除
     */
    DELETED((byte) 1);

    private final byte state;

    @Override
    public Byte getValue() {
        return state;
    }
}
