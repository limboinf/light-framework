package com.limbo.light.web.test.service.biz;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.limbo.light.web.dao.BaseBiz;
import com.limbo.light.web.test.service.entity.ProductEntity;
import com.limbo.light.web.test.service.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductBizService extends BaseBiz<ProductMapper, ProductEntity> {

    public ProductEntity getOnlyOne(Wrapper<ProductEntity> query) {
        return baseMapper.selectOne(query);
    }
}
