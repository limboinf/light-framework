package com.limbo.light.lightlockspringbootstarter.support.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * lock config
 *
 * @author limbo
 * @since 2022/12/26 15:17
 */
@Data
@ConfigurationProperties(prefix = LockConfig.PREFIX)
public class LockConfig {

    public static final String PREFIX = "light.lock";

    /**
     * 默认获取锁等待时间 60s
     */
    public static final long DEFAULT_WAIT_TIME = 60;
    /**
     * 默认释放时间, 60s
     */
    public static final long DEFAULT_RELEASE_TIME = 60;

    private String address;
    private String password;
    private int database = 1;
    private long waitTime = DEFAULT_WAIT_TIME;
    private long leaseTime = DEFAULT_RELEASE_TIME;

    @Data
    public static class ClusterServer {
        private String[] nodeAddresses;
    }

}
