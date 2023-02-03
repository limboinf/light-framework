SET MODE=MySQL;
CREATE TABLE IF NOT EXISTS `t_product_info`  (
  `id` bigint(20) UNSIGNED auto_increment,
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={0:正常, 1:已删除}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';