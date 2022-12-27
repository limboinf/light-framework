package com.limbo.light.lightlockspringbootstarter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 锁信息
 *
 * @author limbo
 * @since 2022/12/26 14:00
 */
@Data
@AllArgsConstructor
public class LockInfo {

    private LockType lockType;

    private String name;

    private long waitTime;

    private long leaseTime;
}
