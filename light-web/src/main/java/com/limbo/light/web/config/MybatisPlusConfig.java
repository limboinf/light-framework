package com.limbo.light.web.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.limbo.light.web.dao.MpCustomSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MP 配置类
 *
 * @author limbo
 * @date 2023/02/03
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Bean
    MpCustomSqlInjector mpCustomSqlInjector() {
        return new MpCustomSqlInjector();
    }

    @Bean
    GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlInjector(new MpCustomSqlInjector());
        return globalConfig;
    }

    @Bean
    MybatisPlusMetaConfig mybatisPlusMetaConfig() {
        return new MybatisPlusMetaConfig();
    }
}
