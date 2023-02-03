package com.limbo.light.web.test.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.limbo.light.web.dao.entity.LightBaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_product_info")
public class ProductEntity extends LightBaseEntity {

    private String name;
    private Long sellPrice;
    private Long stock;
}
