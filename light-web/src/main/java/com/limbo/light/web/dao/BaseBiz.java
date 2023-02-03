package com.limbo.light.web.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limbo.light.web.dao.entity.LightBaseEntity;
import com.limbo.light.web.dao.enums.DeleteState;
import com.limbo.light.web.dao.mapper.LightBaseMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * biz 基类
 *
 * @author limbo
 * @date 2023/02/03
 */
public class BaseBiz<M extends LightBaseMapper<T>, T extends LightBaseEntity> extends ServiceImpl<M, T> {

    /**
     * 通过id 逻辑删除
     */
    public boolean logicDel(Long id) {
        T entity = BeanUtils.instantiateClass(entityClass);
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setDeleteState(DeleteState.DELETED);
        return baseMapper.updateById(entity) == NumberUtils.INTEGER_ONE;
    }

    public Integer insertBatchSomeColumn(List<T> entityList) {
        return baseMapper.insertBatchSomeColumn(entityList);
    }

    /**
     * mybatis-plus 真正的批量插入
     */
    public int insertBatch(List<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return 0;
        }

        if (NumberUtils.INTEGER_ONE.equals(entityList.size())) {
            return baseMapper.insert(entityList.get(0));
        }
        return insertBatchSomeColumn(entityList);
    }

}
