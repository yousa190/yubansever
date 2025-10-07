-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dms
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `cat_id` bigint NOT NULL AUTO_INCREMENT,
  `cat_name` varchar(50) NOT NULL,
  `cat_pid` bigint DEFAULT '0',
  `cat_level` tinyint NOT NULL,
  `is_del` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cat_id`),
  KEY `cat_pid` (`cat_pid`),
  CONSTRAINT `category_chk_1` CHECK ((`cat_level` between 1 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'家用电器',0,1,0),(2,'手机数码',0,1,0),(3,'电脑办公',0,1,0),(4,'服装鞋靴',0,1,0),(5,'食品生鲜',0,1,0),(6,'大家电',1,2,0),(7,'厨房电器',1,2,0),(8,'生活电器',1,2,0),(9,'手机通讯',2,2,0),(10,'摄影摄像',2,2,0),(11,'智能设备',2,2,0),(12,'电脑整机',3,2,0),(13,'外设产品',3,2,0),(14,'办公设备',3,2,0),(15,'男装',4,2,0),(16,'女装',4,2,0),(17,'童装',4,2,0),(18,'休闲食品',5,2,0),(19,'生鲜果蔬',5,2,0),(20,'酒水饮料',5,2,0),(21,'空调',6,3,0),(22,'冰箱',6,3,0),(23,'洗衣机',6,3,0),(24,'电饭煲',7,3,0),(25,'微波炉',7,3,0),(26,'榨汁机',7,3,0),(27,'吸尘器',8,3,0),(28,'加湿器',8,3,0),(29,'电风扇',8,3,0),(30,'智能手机',9,3,0),(31,'老人手机',9,3,0),(32,'对讲机',9,3,0),(33,'单反相机',10,3,0),(34,'摄像机',10,3,0),(35,'镜头',10,3,0),(36,'智能手表',11,3,0),(37,'智能家居',11,3,0),(38,'无人机',11,3,0),(39,'笔记本电脑',12,3,0),(40,'台式机',12,3,0),(41,'平板电脑',14,3,0),(42,'键盘',13,3,0),(43,'鼠标',13,3,0),(44,'显示器',13,3,0),(45,'打印机',14,3,0),(46,'扫描仪',14,3,0),(47,'投影仪',14,3,0),(48,'衬衫',15,3,0),(49,'牛仔裤',15,3,0),(50,'夹克',15,3,0),(102,'电脑',0,1,0),(105,'1',102,2,0),(107,'x',105,3,0);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称',
  `cat_id` bigint NOT NULL COMMENT '商品分类ID',
  `brand` varchar(100) DEFAULT '易购' COMMENT '品牌名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `market_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '商品库存',
  `sales_count` int DEFAULT '0' COMMENT '销售数量',
  `product_sn` varchar(64) DEFAULT NULL COMMENT '商品编码',
  `product_desc` text COMMENT '商品描述',
  `detail_content` longtext COMMENT '详情内容',
  `is_on_sale` tinyint(1) DEFAULT '0' COMMENT '是否上架：0-下架，1-上架',
  `is_new` tinyint(1) DEFAULT '0' COMMENT '是否新品',
  `is_hot` tinyint(1) DEFAULT '0' COMMENT '是否热门',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  `unit` varchar(20) DEFAULT '件' COMMENT '商品单位',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '商品状态：1-正常，0-停用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `on_sale_time` datetime DEFAULT NULL COMMENT '上架时间',
  PRIMARY KEY (`product_id`),
  KEY `idx_cat_id` (`cat_id`),
  KEY `idx_brand` (`brand`),
  KEY `idx_status` (`status`),
  KEY `idx_is_new` (`is_new`),
  KEY `idx_is_hot` (`is_hot`),
  KEY `idx_is_on_sale` (`is_on_sale`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'海尔冰箱BCD-258WDVMU1',1,'海尔',2999.00,3599.00,2200.00,100,50,'HP-258WDVMU1','海尔冰箱258升变频风冷无霜家用双门电冰箱','<p>海尔冰箱BCD-258WDVMU1，258升大容量，风冷无霜技术，变频压缩机，节能静音，多维精控，干湿分储。</p>',1,1,1,0,'台',1,'2025-10-01 10:00:00','2025-10-05 15:30:00','2025-10-02 09:00:00'),(2,'美的空调KFR-35GW/WDAA3@',1,'美的',2699.00,3199.00,2000.00,50,120,'MK-35GW-WDAA3','美的大1.5匹变频空调家用冷暖智能舒适型','<p>美的空调KFR-35GW/WDAA3@，大1.5匹，变频技术，自清洁功能，智能控制，舒适送风。</p>',1,0,1,0,'台',1,'2025-10-01 11:00:00','2025-10-06 10:15:00','2025-10-01 11:00:00'),(3,'小米电视EA50 2022款',1,'小米',1599.00,1899.00,1200.00,75,80,'MT-EA50-2022','小米50英寸4K超高清智能网络液晶平板电视机','<p>小米电视EA50 2022款，50英寸，4K分辨率，智能语音遥控，海量内容，金属机身。</p>',1,1,0,0,'台',1,'2025-10-02 09:30:00','2025-10-06 14:20:00','2025-10-03 08:45:00'),(4,'iPhone 15 Pro',2,'Apple',7999.00,8999.00,6000.00,30,25,'IP-15PRO','苹果iPhone 15 Pro (A3122) 128GB 钛金属原色','<p>iPhone 15 Pro，钛金属设计，A17 Pro芯片，4800万像素主摄，灵动岛，USB-C接口。</p>',1,1,1,0,'台',1,'2025-10-02 14:00:00','2025-10-06 16:45:00','2025-10-02 14:00:00'),(5,'华为Mate60 Pro',2,'华为',6999.00,7999.00,5200.00,20,35,'HM-Mate60Pro','华为Mate60 Pro卫星通信版 鸿蒙旗舰手机','<p>华为Mate60 Pro，卫星通话功能，麒麟9000S芯片，超光变摄像头，鸿蒙操作系统。</p>',1,0,1,0,'台',1,'2025-10-03 10:30:00','2025-10-06 11:20:00','2025-10-03 10:30:00'),(6,'小米13',2,'小米',3999.00,4499.00,3000.00,40,90,'XM-13','小米13 第二代骁龙8旗舰处理器 徕卡光学镜头','<p>小米13，第二代骁龙8处理器，徕卡专业光学镜头，6.36英寸直屏，5000mAh大电池。</p>',1,0,1,0,'台',1,'2025-10-03 15:45:00','2025-10-07 09:30:00','2025-10-03 15:45:00'),(7,'联想ThinkPad X1 Carbon',3,'联想',9999.00,11999.00,7500.00,15,12,'TP-X1C','联想ThinkPad X1 Carbon 2023款 14英寸轻薄商务笔记本','<p>ThinkPad X1 Carbon 2023款，第13代英特尔酷睿处理器，14英寸2.8K屏幕，轻至1.12kg，续航长达15.3小时。</p>',1,1,0,0,'台',1,'2025-10-04 09:15:00','2025-10-07 16:04:13','2025-10-04 09:00:00'),(8,'罗技G502 HERO鼠标',3,'罗技',299.00,399.00,180.00,100,65,'L-G502','罗技G502 HERO高性能游戏鼠标 16000DPI','<p>罗技G502 HERO鼠标，16000DPI，11个可编程按键，RGB灯效，人体工学设计。</p>',1,1,0,0,'个',1,'2025-10-04 11:30:00','2025-10-07 16:04:13','2025-10-04 11:00:00'),(9,'雷蛇黑寡妇蜘蛛V3',3,'雷蛇',699.00,899.00,500.00,35,28,'RZ-BlackWidow','雷蛇黑寡妇蜘蛛V3机械游戏键盘 绿轴','<p>雷蛇黑寡妇蜘蛛V3，绿轴机械按键，RGB背光，人体工学设计，专用软件定制。</p>',1,1,0,0,'个',1,'2025-10-04 13:45:00','2025-10-07 16:04:13','2025-10-04 13:00:00'),(10,'耐克男子运动T恤',4,'耐克',199.00,299.00,100.00,200,150,'NK-T恤-男','耐克NIKE男子运动T恤 Dri-FIT科技面料','<p>耐克男子运动T恤，Dri-FIT排汗技术，舒适剪裁，适合多种运动场合。</p>',1,1,0,0,'件',1,'2025-10-05 10:00:00','2025-10-07 16:04:13','2025-10-05 10:00:00'),(11,'阿迪达斯女子运动鞋',4,'阿迪达斯',499.00,699.00,300.00,75,88,'AD-运动鞋-女','阿迪达斯女子运动鞋 Cloudfoam技术缓震','<p>阿迪达斯女子运动鞋，Cloudfoam缓震技术，舒适内底，时尚外观，适合日常穿着。</p>',1,1,1,0,'双',1,'2025-10-05 14:30:00','2025-10-07 16:04:13','2025-10-05 14:00:00'),(12,'优衣库摇粒绒外套',4,'优衣库',129.00,199.00,60.00,150,210,'YY-摇粒绒外套','优衣库摇粒绒外套 保暖舒适 经典款','<p>优衣库摇粒绒外套，柔软保暖，轻便易携带，多种颜色可选，四季皆宜。</p>',1,1,1,0,'件',1,'2025-10-05 16:20:00','2025-10-07 16:04:13','2025-10-05 16:00:00'),(13,'阳澄湖大闸蟹',5,'阳澄湖',199.00,299.00,100.00,50,35,'YC-大闸蟹','阳澄湖大闸蟹 10只装 公蟹4两 母蟹3两','<p>阳澄湖大闸蟹，10只装，鲜活捕捞，黄满膏肥，附送蒸蟹工具和调料包。</p>',1,1,1,0,'盒',1,'2025-10-06 08:30:00','2025-10-07 16:04:13','2025-10-06 08:00:00'),(14,'进口车厘子',5,'智利',89.00,129.00,50.00,80,120,'JC-车厘子','智利进口车厘子 1kg 新鲜水果','<p>智利进口车厘子，JJ级大果，色泽鲜艳，口感香甜，空运直达，新鲜保证。</p>',1,1,1,0,'kg',1,'2025-10-06 11:15:00','2025-10-07 16:04:13','2025-10-06 11:00:00'),(15,'伊利纯牛奶',5,'伊利',49.90,59.90,30.00,200,310,'YL-纯牛奶','伊利纯牛奶 250ml*12盒 营养早餐奶','<p>伊利纯牛奶，12盒装，优质奶源，营养丰富，口感醇香，适合全家饮用。</p>',1,1,0,0,'箱',1,'2025-10-06 15:40:00','2025-10-07 16:04:13','2025-10-06 15:00:00'),(16,'testGood',105,'易购',20.00,10.00,1.00,200,200,'132133123','test','<p>null</p>',1,0,1,0,'件',1,'2025-10-07 15:58:03','2025-10-07 16:05:40',NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `image_id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `sort_order` int DEFAULT '0',
  `is_main` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`image_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_image_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
INSERT INTO `product_image` VALUES (1,1,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-01 10:05:00'),(2,1,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-01 10:05:00'),(3,1,'http://localhost:8080/images/goods/default.jpg',3,0,'2025-10-01 10:05:00'),(4,2,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-01 11:05:00'),(5,2,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-01 11:05:00'),(6,3,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-02 09:35:00'),(7,3,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-02 09:35:00'),(8,3,'http://localhost:8080/images/goods/default.jpg',3,0,'2025-10-02 09:35:00'),(9,4,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-02 14:05:00'),(10,4,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-02 14:05:00'),(11,4,'http://localhost:8080/images/goods/default.jpg',3,0,'2025-10-02 14:05:00'),(12,5,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-03 10:35:00'),(13,5,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-03 10:35:00'),(14,6,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-03 15:50:00'),(15,6,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-03 15:50:00'),(16,6,'http://localhost:8080/images/goods/default.jpg',3,0,'2025-10-03 15:50:00'),(17,7,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-04 09:20:00'),(18,7,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-04 09:20:00'),(19,8,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-04 11:35:00'),(20,8,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-04 11:35:00'),(21,9,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-04 13:50:00'),(22,9,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-04 13:50:00'),(23,10,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-05 10:05:00'),(24,10,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-05 10:05:00'),(25,11,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-05 14:35:00'),(26,11,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-05 14:35:00'),(27,12,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-05 16:25:00'),(28,12,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-05 16:25:00'),(29,13,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-06 08:35:00'),(30,13,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-06 08:35:00'),(31,14,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-06 11:20:00'),(32,14,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-06 11:20:00'),(33,15,'http://localhost:8080/images/goods/default.jpg',1,1,'2025-10-06 15:45:00'),(34,15,'http://localhost:8080/images/goods/default.jpg',2,0,'2025-10-06 15:45:00');
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spec_group`
--

DROP TABLE IF EXISTS `spec_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spec_group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT,
  `cat_name` varchar(50) NOT NULL,
  `cat_id` bigint NOT NULL,
  `group_config` json DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `cat_name` (`cat_name`),
  KEY `cat_id` (`cat_id`),
  CONSTRAINT `spec_group_ibfk_1` FOREIGN KEY (`cat_id`) REFERENCES `category` (`cat_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spec_group`
--

LOCK TABLES `spec_group` WRITE;
/*!40000 ALTER TABLE `spec_group` DISABLE KEYS */;
INSERT INTO `spec_group` VALUES (2,'服装鞋靴',4,'[{\"title\": \"1\", \"children\": []}]'),(4,'老人手机',31,'[{\"title\": \"型号\", \"children\": [{\"title\": \"型号a\"}]}, {\"title\": \"容量\", \"children\": [{\"title\": \"大容量\"}, {\"title\": \"小容量\"}, {\"title\": \"中容量\"}]}, {\"title\": \"品牌\", \"children\": []}]'),(5,'食品生鲜',5,'[{\"title\": \"a\", \"children\": []}]'),(6,'大家电',6,'[{\"title\": \"a\", \"children\": []}]');
/*!40000 ALTER TABLE `spec_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
  `username` varchar(50) NOT NULL COMMENT '登录账号（唯一）',
  `password_hash` char(60) NOT NULL COMMENT '加密后的密码',
  `email` varchar(100) NOT NULL COMMENT '验证邮箱（唯一）',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `sex` tinyint(1) NOT NULL DEFAULT '1' COMMENT '性别',
  `avatar` varchar(255) DEFAULT 'http://localhost:8080/images/users/defaultAvatar.jpg' COMMENT '头像地址',
  `last_login` datetime DEFAULT (now()) COMMENT '最后登录时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `level` int NOT NULL DEFAULT '1' COMMENT '用户权限',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除标记',
  `status` int NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username_unique` (`username`),
  UNIQUE KEY `email_unique` (`email`),
  KEY `phone_index` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'yuban','123456','19959771806@163.com','19959771806',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 14:10:25','2025-02-19 14:10:27','2025-10-07 04:58:58',3,0,1),(2,'yuban2','$2a$10$/D5hvxWueeDDyGDViUXp5O.SZr0q4JmRUaXzieUt.VgYNR1/RWTUi','3471998500@qq.com','13599217014',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 20:15:30','2025-02-19 18:01:32','2025-03-01 15:49:44',1,0,0),(3,'yuban33','$2a$10$HM2.uVWzJ9cxxGYOju8MO.YM5jwdieEx57hahtcicZpu/V41cUIwy','2471998500@qq.com','18595225228',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 20:15:09','2025-02-19 20:15:09','2025-10-07 04:59:53',1,0,1),(4,'yuban4','$2a$10$l8v0FMcabFyP7LwpMzIJ4.5cp0sv9fw/9LlC6ARdQKPuHT74tNPIu','18350580922@163.com','19959771806',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-03 15:38:26','2025-10-03 15:38:26','2025-10-03 15:38:26',1,0,0),(5,'yubanyousa','$2a$10$DS2edoMnPOsfMNXlerJaMuwAsakhpeEc24okzFX8OtIOc9TUp/EAi','2471998501@qq.com','18595225229',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:30:06','2025-10-04 12:30:06','2025-10-04 12:39:10',1,0,0),(6,'yuuban6','$2a$10$wRu4BOksJ6B/wyoIzKxieepRf7WZ1DpDt2ButfXgaIT0WxBW8rfUO','2471998502@qq.com','18595225230',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:34:59','2025-10-04 12:34:59','2025-10-04 12:34:59',1,0,0),(7,'yuban7','$2a$10$jKiKcgw1a3Sjv9JCHf4JhuLHf52TkcfCuuN4mH/PlmpOVsfgcRqRm','2471998503@qq.com','18595225231',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:38:02','2025-10-04 12:38:02','2025-10-04 12:52:39',1,0,0),(8,'yuban8','$2a$10$rNPa3/gs2IuVr6.nYN6kROIVBrcYV28mG4z4BNZhx08ud2nJcCsOK','2471998504@qq.com','18595225232',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:43:50','2025-10-04 12:43:50','2025-10-04 12:52:26',2,0,0),(9,'yuban9','$2a$10$OHchAfoW4TwhE55iTjcd9uf9u0mfecPjv1zJuyVHsgjtcyaY8OlRC','2471998509@qq.com','18595225234',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:54:04','2025-10-04 12:54:04','2025-10-04 05:24:34',2,0,1),(10,'御坂','$2a$10$A5TRU7/5bUHmxAF.QFHh0eA6teoCPy2o5Fy/odPWxAdHpZc0yXnsu','2471998510@qq.com','18595225235',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:55:22','2025-10-04 12:55:22','2025-10-04 12:55:22',1,0,0),(11,'yuban10','$2a$10$C74Ekmb9vfRcTztcJjOdceKAQy2vC.XR7px60OySptb2T7KSFRl76','18350580923@163.com','18595225236',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:56:00','2025-10-04 12:56:00','2025-10-04 05:21:54',1,0,0),(12,'yuban11','$2a$10$K1c5X3KvAbtCQkIsmbgVz.DbpLaV6H.H6gwpLGCnLu6fgEV6IEaTW','2471998511@qq.com','18595225237',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:58:15','2025-10-04 12:58:15','2025-10-04 05:24:30',2,0,0),(13,'testdel1','$2a$10$HBxNXiK6Z9lX7QByFpIdjeBqwJkAosl/XDgw6pKo8DFN0uxejd1Zy','247199850del@qq.com','18595225240',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 13:43:49','2025-10-04 13:43:49','2025-10-04 05:47:40',3,1,0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-07 17:18:44
