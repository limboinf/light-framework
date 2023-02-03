package com.limbo.light.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.limbo.light.web.dommon.DaoConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * mp自动填充类
 *
 * @author limbo
 * @date 2023/02/03
 */
@Slf4j
public class MybatisPlusMetaConfig implements MetaObjectHandler {

    public MybatisPlusMetaConfig() {
        log.info("[mp] 自动填充 ...");
    }

    /**
     * 使用mp做添加操作时候，这个方法执行, 仅当createTime,updateTime字段存在且已设为自动填充注解才生效
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DaoConstants.TABLE_FIELD_CREATE, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, DaoConstants.TABLE_FIELD_UPDATE, LocalDateTime::now, LocalDateTime.class);
    }

    /**
     * 使用mp做修改操作时候，这个方法执行,仅当createTime,updateTime字段存在且已设为自动填充注解才生效
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, DaoConstants.TABLE_FIELD_UPDATE, LocalDateTime.class, LocalDateTime.now());
    }
}
