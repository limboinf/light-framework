package com.limbo.light.web.test.service.mapper;

import com.limbo.light.web.dao.mapper.LightBaseMapper;
import com.limbo.light.web.test.service.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends LightBaseMapper<ProductEntity> {
}
