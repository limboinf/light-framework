package com.limbo.light.web.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * basic mapper
 *
 * @author limbo
 * @date 2023/02/03
 */
public interface LightBaseMapper<T> extends BaseMapper<T> {

    /**
     * mybatis-plus 真正的批量插入支持
     */
    Integer insertBatchSomeColumn(@Param("list") List<T> entityList);

}
