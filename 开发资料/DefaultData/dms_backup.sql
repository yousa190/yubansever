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
INSERT INTO `category` VALUES (1,'家用电器',0,1,0),(2,'手机数码',0,1,0),(3,'电脑办公',0,1,0),(4,'服装鞋靴',0,1,0),(5,'食品生鲜',0,1,0),(6,'大家电',1,2,0),(7,'厨房电器',1,2,0),(8,'生活电器',1,2,0),(9,'手机通讯',2,2,0),(10,'摄影摄像',2,2,0),(11,'智能设备',2,2,0),(12,'电脑整机',3,2,0),(13,'外设产品',3,2,0),(14,'办公设备',3,2,0),(15,'男装',4,2,0),(16,'女装',4,2,0),(17,'童装',4,2,0),(18,'休闲食品',5,2,0),(19,'生鲜果蔬',5,2,0),(20,'酒水饮料',5,2,0),(21,'空调',6,3,0),(22,'冰箱',6,3,0),(23,'洗衣机',6,3,0),(24,'电饭煲',7,3,0),(25,'微波炉',7,3,0),(26,'榨汁机',7,3,0),(27,'吸尘器',8,3,0),(28,'加湿器',8,3,0),(29,'电风扇',8,3,0),(30,'智能手机',9,3,0),(31,'老人手机',9,3,0),(32,'对讲机',9,3,0),(33,'单反相机',10,3,0),(34,'摄像机',10,3,0),(35,'镜头',10,3,0),(36,'智能手表',11,3,0),(37,'智能家居',11,3,0),(38,'无人机',11,3,0),(39,'笔记本电脑',12,3,0),(40,'台式机',12,3,0),(41,'平板电脑',14,3,0),(42,'键盘',13,3,0),(43,'鼠标',13,3,0),(44,'显示器',13,3,0),(45,'打印机',14,3,0),(46,'扫描仪',14,3,0),(47,'投影仪',14,3,0),(48,'衬衫',15,3,0),(49,'牛仔裤',15,3,0),(50,'夹克',15,3,0),(102,'电脑',0,1,0),(105,'1',0,1,0),(107,'x',4,2,0);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
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
INSERT INTO `spec_group` VALUES (1,'家用电器',1,NULL),(2,'服装鞋靴',4,'[{\"title\": \"1\", \"children\": []}]'),(4,'老人手机',31,'[{\"title\": \"型号\", \"children\": [{\"title\": \"型号a\"}]}, {\"title\": \"容量\", \"children\": [{\"title\": \"大容量\"}, {\"title\": \"小容量\"}, {\"title\": \"中容量\"}]}, {\"title\": \"品牌\", \"children\": []}]'),(5,'食品生鲜',5,'[{\"title\": \"a\", \"children\": []}]'),(6,'大家电',6,'[{\"title\": \"a\", \"children\": []}]');
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
INSERT INTO `sys_user` VALUES (1,'yuban','123456','19959771806@163.com','19959771806',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 14:10:25','2025-02-19 14:10:27','2025-10-04 12:33:11',3,0,0),(2,'yuban2','$2a$10$/D5hvxWueeDDyGDViUXp5O.SZr0q4JmRUaXzieUt.VgYNR1/RWTUi','3471998500@qq.com','13599217014',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 20:15:30','2025-02-19 18:01:32','2025-03-01 15:49:44',1,0,0),(3,'yuban33','$2a$10$HM2.uVWzJ9cxxGYOju8MO.YM5jwdieEx57hahtcicZpu/V41cUIwy','2471998500@qq.com','18595225228',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-02-19 20:15:09','2025-02-19 20:15:09','2025-10-04 05:27:59',1,0,0),(4,'yuban4','$2a$10$l8v0FMcabFyP7LwpMzIJ4.5cp0sv9fw/9LlC6ARdQKPuHT74tNPIu','18350580922@163.com','19959771806',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-03 15:38:26','2025-10-03 15:38:26','2025-10-03 15:38:26',1,0,0),(5,'yubanyousa','$2a$10$DS2edoMnPOsfMNXlerJaMuwAsakhpeEc24okzFX8OtIOc9TUp/EAi','2471998501@qq.com','18595225229',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:30:06','2025-10-04 12:30:06','2025-10-04 12:39:10',1,0,0),(6,'yuuban6','$2a$10$wRu4BOksJ6B/wyoIzKxieepRf7WZ1DpDt2ButfXgaIT0WxBW8rfUO','2471998502@qq.com','18595225230',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:34:59','2025-10-04 12:34:59','2025-10-04 12:34:59',1,0,0),(7,'yuban7','$2a$10$jKiKcgw1a3Sjv9JCHf4JhuLHf52TkcfCuuN4mH/PlmpOVsfgcRqRm','2471998503@qq.com','18595225231',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:38:02','2025-10-04 12:38:02','2025-10-04 12:52:39',1,0,0),(8,'yuban8','$2a$10$rNPa3/gs2IuVr6.nYN6kROIVBrcYV28mG4z4BNZhx08ud2nJcCsOK','2471998504@qq.com','18595225232',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:43:50','2025-10-04 12:43:50','2025-10-04 12:52:26',2,0,0),(9,'yuban9','$2a$10$OHchAfoW4TwhE55iTjcd9uf9u0mfecPjv1zJuyVHsgjtcyaY8OlRC','2471998509@qq.com','18595225234',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:54:04','2025-10-04 12:54:04','2025-10-04 05:24:34',2,0,1),(10,'御坂','$2a$10$A5TRU7/5bUHmxAF.QFHh0eA6teoCPy2o5Fy/odPWxAdHpZc0yXnsu','2471998510@qq.com','18595225235',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:55:22','2025-10-04 12:55:22','2025-10-04 12:55:22',1,0,0),(11,'yuban10','$2a$10$C74Ekmb9vfRcTztcJjOdceKAQy2vC.XR7px60OySptb2T7KSFRl76','18350580923@163.com','18595225236',1,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:56:00','2025-10-04 12:56:00','2025-10-04 05:21:54',1,0,0),(12,'yuban11','$2a$10$K1c5X3KvAbtCQkIsmbgVz.DbpLaV6H.H6gwpLGCnLu6fgEV6IEaTW','2471998511@qq.com','18595225237',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 12:58:15','2025-10-04 12:58:15','2025-10-04 05:24:30',2,0,0),(13,'testdel1','$2a$10$HBxNXiK6Z9lX7QByFpIdjeBqwJkAosl/XDgw6pKo8DFN0uxejd1Zy','247199850del@qq.com','18595225240',0,'http://localhost:8080/images/users/defaultAvatar.jpg','2025-10-04 13:43:49','2025-10-04 13:43:49','2025-10-04 05:47:40',3,1,0);
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

-- Dump completed on 2025-10-04 13:58:04
