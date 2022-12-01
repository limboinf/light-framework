package com.limbo.light.common.domain.enums;

import com.limbo.light.core.domain.enums.EnumSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * 设备枚举
 */
@AllArgsConstructor
@Getter
public enum DeviceTypeEnum implements EnumSpec<DeviceTypeEnum> {

    UNKNOWN(0, "UNKNOWN"),

    /**
     * 桌面
     */
    DESKTOP(1, "Desktop"),

    /**
     * 平板
     */
    TABLET(2, "Tablet"),

    /**
     * 手机
     */
    PHONE(3, "Phone");

    private int code;
    private String displayName;

    public static DeviceTypeEnum parse(String name) {
        try {
            return Optional.ofNullable(EnumSpec.of(name, DeviceTypeEnum.class)).orElse(DeviceTypeEnum.UNKNOWN);
        } catch (Exception ignored) {
        }

        return DeviceTypeEnum.UNKNOWN;
    }

}
