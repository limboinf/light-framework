package com.limbo.light.lightlockspringbootstarter.domain;

import com.limbo.light.lightlockspringbootstarter.lock.Lock;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * lock result
 *
 * @author limbo
 * @since 2022/12/27 10:46
 */
@Data
@AllArgsConstructor
public class LockRes {

    /**
     * 锁信息
     */
    private LockInfo lockInfo;

    /**
     * 锁实例
     */
    private Lock lock;

    /**
     * 锁状态: true 表示拿到锁，false表示已释放
     */
    private Boolean state;

}
