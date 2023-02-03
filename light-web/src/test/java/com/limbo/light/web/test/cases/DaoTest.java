package com.limbo.light.web.test.cases;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.limbo.light.web.dao.enums.DeleteState;
import com.limbo.light.web.test.service.TestApp;
import com.limbo.light.web.test.service.biz.ProductBizService;
import com.limbo.light.web.test.service.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApp.class, args = "--spring.profiles.active=mybatisplus")
public class DaoTest {

    @Autowired
    private ProductBizService productBizService;

    @Test
    void testInsert() {
        ProductEntity entity = buildProductEntity("apple", 10L, 20L);
        entity.setId(100L);
        boolean success = productBizService.save(entity);
        Assertions.assertEquals(true, success);

        System.out.println(JSON.toJSONString(productBizService.getById(100L)));
    }


    @Test
    void testInsertBatch() {
        List<ProductEntity> productEntities = List.of(
                buildProductEntity("apple", 10L, 20L),
                buildProductEntity("orange", 11L, 20L),
                buildProductEntity("tomato", 12L, 20L));
        int i = productBizService.insertBatchSomeColumn(productEntities);
        Assertions.assertEquals(3, i);

        LambdaQueryWrapper<ProductEntity> query = new LambdaQueryWrapper<>();
        query.eq(ProductEntity::getStock, 20L);
        List<ProductEntity> list = productBizService.list(query);
        System.out.println(JSON.toJSONString(list));

        // 测试 aop limit 1
        System.out.println(JSON.toJSONString(productBizService.getOnlyOne(query)));
    }

    private ProductEntity buildProductEntity(String productName, Long price, Long stock) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productName);
        productEntity.setSellPrice(price);
        productEntity.setStock(stock);
        productEntity.setDeleteState(DeleteState.NORMAL);
        return productEntity;
    }

}
