package com.limbo.light.lightlockspringbootstarter.support.domain;

import lombok.Data;

/**
 * 锁信息
 *
 * @author limbo
 * @since 2022/12/26 14:00
 */
@Data
public class LockInfo {

    private LockType lockType;

    private String name;

    private long waitTime;

    private long leaseTime;
}
