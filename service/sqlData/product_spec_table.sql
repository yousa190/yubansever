-- 创建商品规格参数表
DROP TABLE IF EXISTS `product_spec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_spec` (
  `spec_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规格参数ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `spec_name` varchar(100) NOT NULL COMMENT '规格名称',
  `spec_value` varchar(255) NOT NULL COMMENT '规格值',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`spec_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_product_spec_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品规格参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

-- 示例数据
-- LOCK TABLES `product_spec` WRITE;
-- INSERT INTO `product_spec` VALUES 
-- (1, 1, '颜色', '白色', '2025-10-01 10:05:00', '2025-10-01 10:05:00'),
-- (2, 1, '尺寸', '双门', '2025-10-01 10:05:00', '2025-10-01 10:05:00'),
-- (3, 1, '能效等级', '一级', '2025-10-01 10:05:00', '2025-10-01 10:05:00');
-- UNLOCK TABLES;