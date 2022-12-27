package com.limbo.light.lightlockspringbootstarter;

import com.limbo.light.lightlockspringbootstarter.core.LockInfoProvider;
import com.limbo.light.lightlockspringbootstarter.aspect.LightLockAspect;
import com.limbo.light.lightlockspringbootstarter.config.LockConfig;
import com.limbo.light.lightlockspringbootstarter.core.BusinessKeyProvider;
import com.limbo.light.lightlockspringbootstarter.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Objects;

/**
 * light lock auto configuration
 *
 * @author limbo
 * @since 2022/12/27 17:33
 */
@Configuration
@ConditionalOnProperty(prefix = LockConfig.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(LockConfig.class)
@Import(LightLockAspect.class)
public class LightLockAutoConfiguration {

    @Autowired
    private LockConfig lockConfig;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redissonClient() {
        Config config = new Config();
        if (Objects.nonNull(lockConfig.getClusterServer())) {
            config.useClusterServers()
                    .setPassword(lockConfig.getPassword())
                    .addNodeAddress(lockConfig.getClusterServer().getNodeAddresses());
        } else {
            config.useSingleServer()
                    .setPassword(lockConfig.getPassword())
                    .setDatabase(lockConfig.getDatabase())
                    .setAddress(lockConfig.getAddress());
        }

        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider() {
        return new BusinessKeyProvider();
    }
}
