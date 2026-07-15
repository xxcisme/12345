-- MySQL dump 10.13  Distrib 8.4.7, for Win64 (x86_64)
--
-- Host: localhost    Database: vtp
-- ------------------------------------------------------
-- Server version	8.4.7

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
-- Table structure for table `res_device`
--

DROP TABLE IF EXISTS `res_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_device` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备主键ID',
  `number` varchar(30) DEFAULT NULL COMMENT '设备编号',
  `name` varchar(50) NOT NULL COMMENT '设备名称',
  `type` varchar(50) DEFAULT NULL COMMENT '类别',
  `version_number` varchar(20) DEFAULT NULL COMMENT '版本号',
  `laboratory_id` bigint DEFAULT NULL COMMENT '所在实验室ID',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '设备状态：0-空闲，1-使用中，2-保修，3-损坏',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_laboratory_id` (`laboratory_id`),
  CONSTRAINT `fk_device_lab` FOREIGN KEY (`laboratory_id`) REFERENCES `res_laboratory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_device`
--

LOCK TABLES `res_device` WRITE;
/*!40000 ALTER TABLE `res_device` DISABLE KEYS */;
INSERT INTO `res_device` VALUES (1,'DEV001','高性能服务器','计算设备','V3.0',1,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(2,'DEV002','VR头盔','显示设备','HTC VIVE Pro',1,1,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(3,'DEV003','图形工作站','计算设备','Dell Precision',1,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(4,'DEV004','思科路由器','网络设备','Cisco 2900',2,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(5,'DEV005','交换机','网络设备','Cisco 3750',2,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(6,'DEV006','防火墙设备','安全设备','Palo Alto PA-220',2,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(7,'DEV007','ARM开发板','嵌入式设备','STM32F407',3,1,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(8,'DEV008','传感器套件','感知设备','Arduino Sensor Kit',3,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(9,'DEV009','示波器','测量设备','Tektronix TBS1052B',3,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0),(10,'DEV010','投影仪','显示设备','Epson CB-X05',1,0,'2026-07-14 14:17:17','2026-07-14 14:17:17',0);
/*!40000 ALTER TABLE `res_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `res_lab_application`
--

DROP TABLE IF EXISTS `res_lab_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_lab_application` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请主键ID',
  `number` varchar(50) DEFAULT NULL COMMENT '申请编号',
  `lab_id` bigint NOT NULL COMMENT '实验室ID',
  `applicant_name` varchar(30) NOT NULL COMMENT '申请人姓名',
  `contact_phone` varchar(11) NOT NULL COMMENT '联系电话',
  `purpose` longtext COMMENT '申请说明',
  `name` varchar(255) NOT NULL COMMENT '实验名称',
  `objective` varchar(500) DEFAULT NULL COMMENT '实验目标',
  `steps` longtext COMMENT '实验步骤',
  `start_time` datetime NOT NULL COMMENT '使用开始时间',
  `end_time` datetime NOT NULL COMMENT '使用结束时间',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审批状态：0-待审批，1-已通过，2-已拒绝',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注/驳回原因',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_lab_id` (`lab_id`),
  CONSTRAINT `fk_app_lab` FOREIGN KEY (`lab_id`) REFERENCES `res_laboratory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_lab_application`
--

LOCK TABLES `res_lab_application` WRITE;
/*!40000 ALTER TABLE `res_lab_application` DISABLE KEYS */;
INSERT INTO `res_lab_application` VALUES (1,'APP2023001',1,'张伟','13800000002','进行数据结构实验','线性表实验','完成线性表基本操作','步骤略','2023-09-05 08:00:00','2023-09-05 12:00:00',1,'批准','2023-08-30 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'APP2023002',2,'李娜','13800000003','网络设备配置实验','路由器配置','完成静态路由配置','步骤略','2023-09-07 14:00:00','2023-09-07 17:00:00',2,'设备冲突，请改期','2023-09-01 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'APP2023003',3,'王强','13800000004','嵌入式开发','ARM环境搭建','搭建开发环境','步骤略','2023-09-10 09:00:00','2023-09-10 16:00:00',0,NULL,NULL,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'APP2023004',1,'赵丽','13800000005','图形学课程设计','VR场景构建','使用Unity构建虚拟场景','步骤略','2023-09-15 08:00:00','2023-09-15 12:00:00',1,'同意','2023-09-12 11:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'APP2023005',2,'孙洋','13800000010','网络抓包实验','Wireshark使用','捕获并分析数据包','步骤略','2023-09-20 10:00:00','2023-09-20 12:00:00',0,NULL,NULL,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `res_lab_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `res_laboratory`
--

DROP TABLE IF EXISTS `res_laboratory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_laboratory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '实验室主键ID',
  `number` varchar(50) DEFAULT NULL COMMENT '实验室编号',
  `name` varchar(100) NOT NULL COMMENT '实验室名称',
  `station_num` int DEFAULT NULL COMMENT '工位数量',
  `area` decimal(10,2) DEFAULT NULL COMMENT '面积（㎡）',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `profile` varchar(1000) DEFAULT NULL COMMENT '简介',
  `description` longtext COMMENT '概要介绍（富文本）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验室表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_laboratory`
--

LOCK TABLES `res_laboratory` WRITE;
/*!40000 ALTER TABLE `res_laboratory` DISABLE KEYS */;
INSERT INTO `res_laboratory` VALUES (1,'LAB001','虚拟仿真实验室A',50,120.50,'实训楼3楼301','配备高性能计算机和VR设备','<p>虚拟仿真实验室A主要用于计算机图形学、虚拟现实等课程的实验教学。</p>','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(2,'LAB002','网络工程实验室B',30,80.00,'实训楼3楼302','配备思科路由器和交换机','<p>网络工程实验室B提供网络设备配置和网络协议分析的实验环境。</p>','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(3,'LAB003','嵌入式系统实验室C',20,60.00,'实训楼4楼401','配备ARM开发板和传感器','<p>嵌入式系统实验室C支持嵌入式软件开发与硬件调试。</p>','2026-07-14 14:17:17','2026-07-14 14:17:17',0);
/*!40000 ALTER TABLE `res_laboratory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `res_media`
--

DROP TABLE IF EXISTS `res_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_media` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件地址',
  PRIMARY KEY (`id`),
  KEY `idx_resource_id` (`resource_id`),
  CONSTRAINT `fk_media_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='媒体资源详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_media`
--

LOCK TABLES `res_media` WRITE;
/*!40000 ALTER TABLE `res_media` DISABLE KEYS */;
INSERT INTO `res_media` VALUES (1,1,'/files/videos/datastructure.mp4'),(2,2,'/files/audios/os_lecture.mp3'),(3,3,'/files/docs/network_lab.pdf'),(4,4,'/files/videos/platform_tutorial.mp4'),(5,5,'/files/docs/java_ebook.pdf'),(6,6,'/files/videos/python_analysis.mp4'),(7,7,'/files/audios/english_listening.mp3'),(8,8,'/files/docs/machine_learning.pdf'),(9,9,'/files/videos/innovation_guide.mp4'),(10,10,'/files/docs/ethics_cases.pdf');
/*!40000 ALTER TABLE `res_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `res_rating`
--

DROP TABLE IF EXISTS `res_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_rating` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `score` tinyint(1) NOT NULL COMMENT '评分值（1-5星）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_resource` (`user_id`,`resource_id`),
  KEY `fk_rating_resource` (`resource_id`),
  CONSTRAINT `fk_rating_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rating_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源评分表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_rating`
--

LOCK TABLES `res_rating` WRITE;
/*!40000 ALTER TABLE `res_rating` DISABLE KEYS */;
INSERT INTO `res_rating` VALUES (1,2,1,5,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,3,1,4,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,4,2,3,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,5,3,5,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,8,4,4,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,10,5,5,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,2,6,4,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(8,3,8,5,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(9,4,9,3,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(10,5,10,4,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `res_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `res_resource`
--

DROP TABLE IF EXISTS `res_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `res_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源主键ID',
  `number` varchar(50) DEFAULT NULL COMMENT '资源编号',
  `name` varchar(255) NOT NULL COMMENT '资源名称',
  `type` tinyint(1) NOT NULL COMMENT '资源类型：1-视频，2-音频，3-文档',
  `thumbnail` varchar(500) DEFAULT NULL COMMENT '缩略图地址',
  `school` varchar(100) DEFAULT NULL COMMENT '所属学校',
  `leader` varchar(30) DEFAULT NULL COMMENT '负责人',
  `category` varchar(100) DEFAULT NULL COMMENT '专业大类',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核，1-已驳回，2-已发布',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注/驳回原因',
  `is_shared` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开放共享：0-不共享，1-共享',
  `profile` longtext COMMENT '概要介绍',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `res_resource`
--

LOCK TABLES `res_resource` WRITE;
/*!40000 ALTER TABLE `res_resource` DISABLE KEYS */;
INSERT INTO `res_resource` VALUES (1,'R001','数据结构精品课程视频',1,'thumb1.jpg','SC001','陈教授','计算机',2,NULL,1,'本视频详细讲解数据结构核心知识点，适合期末复习。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(2,'R002','操作系统原理音频讲座',2,'thumb2.jpg','SC001','刘副教授','计算机',2,NULL,1,'操作系统各章节重点音频讲解，适合碎片时间学习。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(3,'R003','计算机网络实验指导书',3,'thumb3.jpg','SC002','王老师','网络',2,NULL,0,'包含完整的网络实验操作步骤和截图。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(4,'R004','虚拟仿真实验平台使用教程',1,'thumb4.jpg','SC003','李老师','综合',0,NULL,1,'平台使用入门视频，涵盖登录、选课、提交报告等操作。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(5,'R005','Java程序设计电子书',3,'thumb5.jpg','SC002','赵教授','软件',2,NULL,1,'Java基础到高级，包含大量代码示例。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(6,'R006','Python科学计算视频',1,'thumb6.jpg','SC001','孙老师','数据科学',2,NULL,1,'使用NumPy、Pandas进行数据分析的实战视频。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(7,'R007','英语听力练习音频',2,'thumb7.jpg','SC003','外教','外语',1,'内容质量不符合要求',0,'基础听力练习，含对话和短文。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(8,'R008','机器学习入门文档',3,'thumb8.jpg','SC002','周教授','人工智能',2,NULL,1,'机器学习基础概念和算法概述。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(9,'R009','创新创业大赛指导视频',1,'thumb9.jpg','SC001','杨老师','创业',2,NULL,1,'往届获奖项目分析，商业计划书撰写技巧。','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(10,'R010','工程伦理案例集',3,'thumb10.jpg','SC003','陈老师','工程',2,NULL,1,'包含30个工程伦理典型案例及讨论要点。','2026-07-14 14:17:17','2026-07-14 14:17:17',0);
/*!40000 ALTER TABLE `res_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sm_user`
--

DROP TABLE IF EXISTS `sm_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sm_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名（学号/教师ID）',
  `password_hash` varchar(255) NOT NULL COMMENT '密码哈希',
  `role` tinyint(1) NOT NULL COMMENT '用户角色：1-学生，2-老师，3-社会人士，4-管理员',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `statu` tinyint(1) NOT NULL DEFAULT '1' COMMENT '账号状态：0-停用，1-启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sm_user`
--

LOCK TABLES `sm_user` WRITE;
/*!40000 ALTER TABLE `sm_user` DISABLE KEYS */;
INSERT INTO `sm_user` VALUES (1,'admin','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',4,'13800000001','admin@vtp.edu.cn',1,'2026-07-14 08:00:00','192.168.1.1','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(2,'20230101','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',1,'13800000002','stu01@example.com',1,'2026-07-13 10:00:00','192.168.1.10','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(3,'20230102','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',1,'13800000003','stu02@example.com',1,'2026-07-12 09:30:00','192.168.1.11','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(4,'20230103','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',1,'13800000004','stu03@example.com',1,'2026-07-11 14:20:00','192.168.1.12','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(5,'20230104','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',1,'13800000005','stu04@example.com',1,'2026-07-10 16:45:00','192.168.1.13','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(6,'T001','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000006','teacher01@vtp.edu.cn',1,'2026-07-14 07:30:00','192.168.1.5','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(7,'T002','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000007','teacher02@vtp.edu.cn',1,'2026-07-13 08:15:00','192.168.1.6','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(8,'society01','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',3,'13800000008','soc01@example.com',1,'2026-07-12 11:00:00','192.168.1.20','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(9,'society02','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',3,'13800000009','soc02@example.com',0,'2026-07-11 09:00:00','192.168.1.21','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(10,'20230105','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',1,'13800000010','stu05@example.com',1,'2026-07-14 12:00:00','192.168.1.14','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(11,'T003','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000011','teacher03@vtp.edu.cn',1,'2026-07-14 08:30:00','192.168.1.8','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(12,'T004','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000012','teacher04@vtp.edu.cn',1,'2026-07-13 09:00:00','192.168.1.9','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(13,'T005','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000013','teacher05@vtp.edu.cn',1,'2026-07-12 10:00:00','192.168.1.15','2026-07-14 14:17:17','2026-07-14 14:17:17',0),(14,'T006','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',2,'13800000014','teacher06@vtp.edu.cn',1,'2026-07-11 11:00:00','192.168.1.16','2026-07-14 14:17:17','2026-07-14 14:17:17',0);
/*!40000 ALTER TABLE `sm_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sm_user_favorite`
--

DROP TABLE IF EXISTS `sm_user_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sm_user_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_resource` (`user_id`,`resource_id`),
  KEY `fk_fav_resource` (`resource_id`),
  CONSTRAINT `fk_fav_resource` FOREIGN KEY (`resource_id`) REFERENCES `res_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fav_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sm_user_favorite`
--

LOCK TABLES `sm_user_favorite` WRITE;
/*!40000 ALTER TABLE `sm_user_favorite` DISABLE KEYS */;
INSERT INTO `sm_user_favorite` VALUES (1,2,1,'2026-07-14 14:17:17'),(2,3,3,'2026-07-14 14:17:17'),(3,4,5,'2026-07-14 14:17:17'),(4,5,6,'2026-07-14 14:17:17'),(5,8,8,'2026-07-14 14:17:17'),(6,10,9,'2026-07-14 14:17:17'),(7,2,10,'2026-07-14 14:17:17'),(8,3,2,'2026-07-14 14:17:17'),(9,4,4,'2026-07-14 14:17:17'),(10,5,7,'2026-07-14 14:17:17');
/*!40000 ALTER TABLE `sm_user_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sm_user_message`
--

DROP TABLE IF EXISTS `sm_user_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sm_user_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(255) NOT NULL COMMENT '消息标题',
  `content` longtext NOT NULL COMMENT '消息内容',
  `type` tinyint(1) NOT NULL COMMENT '消息类型：1-审核，2-课程，3-成绩',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_msg_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sm_user_message`
--

LOCK TABLES `sm_user_message` WRITE;
/*!40000 ALTER TABLE `sm_user_message` DISABLE KEYS */;
INSERT INTO `sm_user_message` VALUES (1,2,'实验室申请审核通过','您申请的实验室LAB001已通过审核，请准时使用。',1,1,'2023-08-30 10:05:00'),(2,3,'实验室申请被驳回','您申请的实验室LAB002因设备冲突被驳回，请重新选择时间段。',1,0,'2023-09-01 09:05:00'),(3,2,'课程提醒：数据结构实验','您有数据结构实验安排于2023-09-05 08:00，请提前准备。',2,0,'2023-09-04 08:00:00'),(4,4,'课程提醒：操作系统实验','您有操作系统实验安排于2023-09-07 14:00，请准时参加。',2,1,'2023-09-06 10:00:00'),(5,5,'成绩发布通知','您的数据结构实验成绩已发布，请查看。',3,0,'2023-09-20 12:00:00'),(6,8,'资源审核通过','您上传的资源\"Python科学计算视频\"已审核通过并发布。',1,1,'2023-09-15 09:00:00'),(7,10,'课程提醒：网络抓包实验','您有网络抓包实验于2023-09-20 10:00，请做好准备。',2,0,'2023-09-19 08:00:00'),(8,2,'成绩发布通知','您的操作系统实验成绩已发布，请查看详情。',3,1,'2023-09-22 14:00:00'),(9,3,'资源审核驳回','您上传的资源\"英语听力练习音频\"因内容不符合要求被驳回，请修改后重新提交。',1,0,'2023-09-18 16:00:00'),(10,4,'系统通知','平台将于2023-10-01进行系统维护，届时可能无法访问。',2,0,'2023-09-28 10:00:00');
/*!40000 ALTER TABLE `sm_user_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sm_user_profile`
--

DROP TABLE IF EXISTS `sm_user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sm_user_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '详情主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `real_name` varchar(30) NOT NULL COMMENT '真实姓名',
  `school_code` varchar(50) DEFAULT NULL COMMENT '学校编号',
  `class_name` varchar(255) DEFAULT NULL COMMENT '班级名称',
  `class_id` bigint DEFAULT NULL COMMENT '班级ID',
  `occupation_type` varchar(50) DEFAULT NULL COMMENT '职业类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_class_id` (`class_id`),
  CONSTRAINT `fk_profile_class` FOREIGN KEY (`class_id`) REFERENCES `tc_class` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sm_user_profile`
--

LOCK TABLES `sm_user_profile` WRITE;
/*!40000 ALTER TABLE `sm_user_profile` DISABLE KEYS */;
INSERT INTO `sm_user_profile` VALUES (1,1,'管理员','SC001',NULL,NULL,'管理员','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,2,'张伟','SC001','计算机科学与技术2023级1班',1,'学生','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,3,'李娜','SC001','计算机科学与技术2023级1班',1,'学生','2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,4,'王强','SC002','软件工程2023级2班',2,'学生','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,5,'赵丽','SC002','软件工程2023级2班',2,'学生','2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,6,'陈教授','SC001',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,7,'刘副教授','SC001',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(8,8,'社会人士一',NULL,NULL,NULL,'工程师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(9,9,'社会人士二',NULL,NULL,NULL,'研究员','2026-07-14 14:17:17','2026-07-14 14:17:17'),(10,10,'孙洋','SC003','自动化2023级3班',3,'学生','2026-07-14 14:17:17','2026-07-14 14:17:17'),(11,11,'张副教授','SC001',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(12,12,'李教授','SC002',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(13,13,'王讲师','SC001',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17'),(14,14,'赵高级工程师','SC003',NULL,NULL,'教师','2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `sm_user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_news`
--

DROP TABLE IF EXISTS `sys_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '新闻主键ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `origin` varchar(255) DEFAULT NULL COMMENT '来源',
  `editor` varchar(50) DEFAULT NULL COMMENT '编辑人员',
  `enclosure` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `content` longtext NOT NULL COMMENT '内容',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_news`
--

LOCK TABLES `sys_news` WRITE;
/*!40000 ALTER TABLE `sys_news` DISABLE KEYS */;
INSERT INTO `sys_news` VALUES (1,'虚拟仿真实验教学中心获批国家级示范中心','教育部官网','张编辑','/images/news1.jpg','近日，教育部公布了2023年度国家级虚拟仿真实验教学中心名单，我校虚拟仿真实验教学中心成功入选。该中心依托计算机、自动化等学科，建设了多个高水平虚拟仿真实验项目，为培养创新型人才提供了有力支撑。','2023-07-01 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'2023年虚拟仿真实训教学研讨会顺利召开','教务处','李编辑','/images/news2.jpg','2023年7月10日，我校举办了2023年虚拟仿真实训教学研讨会。来自全国20所高校的专家和教师代表齐聚一堂，围绕虚拟仿真教学资源建设、教学模式创新、产教融合等议题展开深入交流。会议达成多项合作意向。','2023-07-10 14:30:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'平台新增\"人工智能通识课\"虚拟仿真模块','平台运营部','王编辑','/images/news3.jpg','为进一步丰富虚拟仿真教学资源，平台近期上线了\"人工智能通识课\"虚拟仿真模块。该模块包含机器学习、深度学习、计算机视觉等实验项目，学生可通过浏览器直接在线实验，无需配置复杂环境。','2023-08-01 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'虚拟仿真实验教学平台用户数突破万人','数据部','赵编辑','/images/news4.jpg','截至2023年8月底，虚拟仿真实验教学平台注册用户已突破一万，覆盖本校及合作院校师生。平台累计开设实验课程50余门，完成实验报告超2万份，成为我校实践教学的重要支撑。','2023-09-01 08:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'2023秋季学期虚拟仿真实验选课指南发布','教务科','孙编辑','/images/news5.jpg','2023秋季学期虚拟仿真实验选课指南已发布，涵盖计算机、电子、机械等多个专业的实验项目。学生可登录平台查看课表并预约实验时间。选课从9月5日开始，请同学们及时关注。','2023-08-25 16:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `sys_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告主键ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `editor` varchar(50) DEFAULT NULL COMMENT '编辑人员',
  `content` longtext NOT NULL COMMENT '内容',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'平台系统升级通知','系统管理员','为提升服务稳定性，平台将于2023年9月30日22:00至10月1日06:00进行系统升级，期间可能无法正常访问，请提前安排好实验计划。','2023-09-28 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'关于实验报告提交截止时间调整的通知','教务处','因学期末考试安排，本学期所有实验报告提交截止时间统一延长至2023年12月25日，请同学们合理安排时间，避免最后一天集中提交造成系统拥堵。','2023-12-01 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'新增虚拟仿真实验项目上线','教学资源部','平台新增\"物联网应用开发\"和\"智能制造仿真\"两个虚拟仿真实验项目，欢迎同学们选课体验。项目介绍可查看平台首页\"新项目推荐\"板块。','2023-11-15 14:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'寒假期间平台服务安排','平台运营部','寒假期间（2024年1月15日至2月25日），平台将保持开放，但部分实验设备可能因维护暂不可用。如有疑问请联系技术支持邮箱：support@vtp.edu.cn。','2024-01-10 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'关于教师评价系统上线的通知','质量监控中心','为提升教学质量，平台将于2024年3月1日上线教师评价系统，学生可在每次实验结束后对指导教师的授课质量进行匿名评价，评价结果将作为教师教学考核的参考依据。','2024-02-20 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation_log`
--

DROP TABLE IF EXISTS `sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `user_role` varchar(20) DEFAULT NULL COMMENT '账号类型',
  `username` varchar(50) DEFAULT NULL COMMENT '账号',
  `action` varchar(500) NOT NULL COMMENT '操作内容',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `sm_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation_log`
--

LOCK TABLES `sys_operation_log` WRITE;
/*!40000 ALTER TABLE `sys_operation_log` DISABLE KEYS */;
INSERT INTO `sys_operation_log` VALUES (1,1,'管理员','admin','登录系统','192.168.1.1','2026-07-14 08:00:00'),(2,6,'教师','T001','发布课程：数据结构','192.168.1.5','2026-07-13 09:30:00'),(3,7,'教师','T002','新增实验：进程管理实验','192.168.1.6','2026-07-12 14:20:00'),(4,2,'学生','20230101','提交实验报告：线性表实验','192.168.1.10','2026-07-11 10:00:00'),(5,3,'学生','20230102','查看课程列表','192.168.1.11','2026-07-10 16:45:00'),(6,8,'社会人士','society01','注册账号','192.168.1.20','2026-07-09 11:00:00'),(7,1,'管理员','admin','审核线上资源：R004','192.168.1.1','2026-07-08 09:00:00'),(8,6,'教师','T001','修改课程：计算机网络','192.168.1.5','2026-07-07 11:30:00'),(9,4,'学生','20230103','申请实验室：LAB003','192.168.1.12','2026-07-06 08:15:00'),(10,1,'管理员','admin','导出操作日志','192.168.1.1','2026-07-05 17:00:00'),(11,11,'教师','T003','新增课程：数据库原理','192.168.1.8','2026-07-04 09:00:00'),(12,12,'教师','T004','新增课程：软件工程','192.168.1.9','2026-07-03 10:00:00');
/*!40000 ALTER TABLE `sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_class`
--

DROP TABLE IF EXISTS `tc_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_class` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级主键ID',
  `class_name` varchar(255) NOT NULL COMMENT '班级名称',
  `school_code` varchar(50) DEFAULT NULL COMMENT '学校编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_class`
--

LOCK TABLES `tc_class` WRITE;
/*!40000 ALTER TABLE `tc_class` DISABLE KEYS */;
INSERT INTO `tc_class` VALUES (1,'计算机科学与技术2023级1班','SC001','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'软件工程2023级2班','SC002','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'自动化2023级3班','SC003','2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_course`
--

DROP TABLE IF EXISTS `tc_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程主键ID',
  `course_code` varchar(50) DEFAULT NULL COMMENT '课程编号',
  `course_name` varchar(255) NOT NULL COMMENT '课程名称',
  `course_type` varchar(100) DEFAULT NULL COMMENT '课程类型',
  `class_hours` int DEFAULT NULL COMMENT '课时',
  `credit` decimal(3,1) DEFAULT NULL COMMENT '学分',
  `teacher_id` bigint NOT NULL COMMENT '负责教师ID（tc_teacher.id）',
  `introduction` longtext COMMENT '课程介绍',
  `outline` longtext COMMENT '课程大纲',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '课程状态：0-草稿，1-已发布，2-已下架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`course_code`),
  KEY `idx_teacher_id` (`teacher_id`),
  CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `tc_teacher` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_course`
--

LOCK TABLES `tc_course` WRITE;
/*!40000 ALTER TABLE `tc_course` DISABLE KEYS */;
INSERT INTO `tc_course` VALUES (1,'CS101','数据结构','专业核心',48,3.0,1,'本课程介绍线性表、栈、队列、树、图等基本数据结构及其算法实现。','第一章 绪论\n第二章 线性表\n第三章 栈和队列\n第四章 串\n第五章 树和二叉树\n第六章 图',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'CS102','操作系统','专业核心',40,2.5,2,'操作系统是计算机系统的核心，本课程涵盖进程管理、内存管理、文件系统和设备管理。','第一章 操作系统概述\n第二章 进程管理\n第三章 内存管理\n第四章 文件系统\n第五章 设备管理',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'CS103','计算机网络','专业必修',36,2.0,1,'计算机网络基本原理，包括OSI模型、TCP/IP协议、路由技术等。','第一章 网络概述\n第二章 物理层\n第三章 数据链路层\n第四章 网络层\n第五章 传输层\n第六章 应用层',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'CS104','数据库原理','专业核心',40,2.5,3,'本课程介绍数据库系统的基本概念、关系模型、SQL语言、事务管理等核心内容。','第一章 数据库概述\n第二章 关系模型\n第三章 SQL语言\n第四章 数据库设计\n第五章 事务管理',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'CS105','软件工程','专业必修',32,2.0,4,'本课程涵盖软件开发生命周期、需求分析、系统设计、测试与维护等软件工程核心知识。','第一章 软件工程概述\n第二章 需求工程\n第三章 软件设计\n第四章 软件测试\n第五章 项目管理',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,'CS106','算法设计与分析','专业选修',32,2.0,5,'本课程介绍分治、动态规划、贪心、回溯等经典算法设计策略及复杂度分析。','第一章 算法基础\n第二章 分治策略\n第三章 动态规划\n第四章 贪心算法\n第五章 图算法',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,'CS107','网络空间安全导论','专业选修',32,2.0,6,'本课程介绍网络安全基础、密码学、系统安全、应用安全等核心安全知识。','第一章 安全概述\n第二章 密码学基础\n第三章 系统安全\n第四章 网络安全\n第五章 应用安全',0,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_experiment`
--

DROP TABLE IF EXISTS `tc_experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_experiment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '实验主键ID',
  `number` varchar(50) DEFAULT NULL COMMENT '实验编号',
  `name` varchar(255) NOT NULL COMMENT '实验名称',
  `course_id` bigint NOT NULL COMMENT '所属课程ID',
  `objective` varchar(500) DEFAULT NULL COMMENT '实验目标',
  `steps` longtext COMMENT '实验步骤',
  `report_template` longtext COMMENT '报告模板',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '实验状态：0-草稿，1-已发布',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_experiment_course` FOREIGN KEY (`course_id`) REFERENCES `tc_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_experiment`
--

LOCK TABLES `tc_experiment` WRITE;
/*!40000 ALTER TABLE `tc_experiment` DISABLE KEYS */;
INSERT INTO `tc_experiment` VALUES (1,'EXP10101','线性表实验',1,'掌握线性表的顺序存储和链式存储，实现插入、删除、查找等基本操作。','1. 定义线性表结构\n2. 实现初始化、插入、删除函数\n3. 编写测试程序验证','实验报告模板：\n一、实验目的\n二、实验内容\n三、程序代码\n四、运行结果截图\n五、心得体会',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'EXP10102','栈和队列实验',1,'理解栈和队列的工作原理，实现进制转换和表达式求值。','1. 实现顺序栈\n2. 实现循环队列\n3. 应用：十进制转二进制','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'EXP10103','二叉树实验',1,'掌握二叉树的遍历（先序、中序、后序）及哈夫曼树的构造。','1. 创建二叉树\n2. 三种遍历递归实现\n3. 构建哈夫曼树','同模板',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'EXP10201','进程管理实验',2,'模拟进程调度算法，实现FCFS、SJF、RR调度策略。','1. 定义进程控制块\n2. 实现三种调度算法\n3. 统计周转时间','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'EXP10202','内存管理实验',2,'模拟分页存储管理，实现地址转换和页面置换算法（FIFO、LRU）。','1. 建立页表\n2. 实现地址转换\n3. 实现FIFO和LRU置换','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,'EXP10301','路由配置实验',3,'使用模拟器配置静态路由和RIP动态路由，实现网络互通。','1. 搭建简单网络拓扑\n2. 配置静态路由\n3. 配置RIP协议','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,'EXP10302','网络抓包实验',3,'使用Wireshark捕获HTTP和TCP数据包，分析协议结构。','1. 启动Wireshark\n2. 访问网页捕获数据包\n3. 分析IP、TCP、HTTP头部','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(8,'EXP10104','排序实验',1,'实现多种排序算法（冒泡、选择、插入、快速、归并）并比较性能。','1. 生成随机数据\n2. 实现各排序函数\n3. 比较执行时间','同模板',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(9,'EXP10401','SQL查询实验',4,'掌握SQL语言的基本查询、连接查询、子查询和聚合函数的使用。','1. 创建数据库和表\n2. 编写各类查询语句\n3. 优化查询性能','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(10,'EXP10402','事务管理实验',4,'理解事务的ACID特性，掌握事务的隔离级别和并发控制。','1. 开启事务\n2. 提交和回滚\n3. 测试不同隔离级别','同模板',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(11,'EXP10501','需求分析实验',5,'针对实际案例进行软件需求分析，撰写需求规格说明书。','1. 调研用户需求\n2. 绘制用例图\n3. 编写需求文档','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(12,'EXP10502','软件测试实验',5,'掌握白盒测试和黑盒测试方法，编写测试用例和执行测试。','1. 设计测试用例\n2. 执行白盒测试\n3. 执行黑盒测试','同模板',1,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_experiment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_grade`
--

DROP TABLE IF EXISTS `tc_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_grade` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评定主键ID',
  `schedule_id` bigint NOT NULL COMMENT '排课ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `overall_score` decimal(5,2) DEFAULT NULL COMMENT '综合成绩',
  `comment` varchar(500) DEFAULT NULL COMMENT '评语',
  `publish_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发布状态：0-未发布，1-已发布',
  `graded_at` datetime DEFAULT NULL COMMENT '评定时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_schedule_student` (`schedule_id`,`student_id`),
  KEY `fk_grade_student` (`student_id`),
  CONSTRAINT `fk_grade_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `tc_plan_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_grade_student` FOREIGN KEY (`student_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成绩评定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_grade`
--

LOCK TABLES `tc_grade` WRITE;
/*!40000 ALTER TABLE `tc_grade` DISABLE KEYS */;
INSERT INTO `tc_grade` VALUES (1,1,2,85.50,'实现基本功能，代码规范，可以进一步完善。',1,'2023-09-10 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,1,3,72.00,'链表操作有bug，需加强练习。',0,'2023-09-10 10:05:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,1,10,90.00,'优秀，程序结构清晰，测试充分。',1,'2023-09-10 10:10:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,2,2,65.00,'表达式求值部分未完成，请补充。',1,'2023-09-15 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,2,3,78.00,'良好，继续努力。',1,'2023-09-15 09:05:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,4,2,92.50,'非常优秀，界面友好，算法实现完整。',1,'2023-09-12 14:30:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,4,3,68.00,'RR算法需重新理解时间片概念。',0,'2023-09-12 14:35:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(8,6,4,80.00,'配置正确，过程详细。',1,'2023-09-25 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(9,6,5,70.00,'基本完成，排错过程记录清晰。',1,'2023-09-25 09:05:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(10,11,2,88.00,'SQL查询基础扎实，可考虑优化复杂查询。',1,'2023-09-12 10:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(11,13,4,75.00,'算法实现基本正确，复杂度分析需加强。',0,'2023-10-10 09:00:00','2026-07-14 14:17:17','2026-07-14 14:17:17'),(12,16,4,NULL,'等待提交报告',0,NULL,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_lab_report`
--

DROP TABLE IF EXISTS `tc_lab_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_lab_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报告主键ID',
  `schedule_id` bigint NOT NULL COMMENT '排课ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `report_content` longtext COMMENT '报告详情',
  `attachment` varchar(500) DEFAULT NULL COMMENT '报告附件',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩（分）',
  `evaluation_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评定状态：0-待评定，1-已评定',
  `submitted_at` datetime DEFAULT NULL COMMENT '提交时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_student_id` (`student_id`),
  CONSTRAINT `fk_report_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `tc_plan_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_report_student` FOREIGN KEY (`student_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实验报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_lab_report`
--

LOCK TABLES `tc_lab_report` WRITE;
/*!40000 ALTER TABLE `tc_lab_report` DISABLE KEYS */;
INSERT INTO `tc_lab_report` VALUES (1,1,2,'本次实验完成了线性表的顺序存储和链式存储的插入、删除、查找操作，代码运行正确，截图见附件。','/reports/report1.docx',85.50,1,'2023-09-05 11:00:00','2026-07-14 14:17:17'),(2,1,3,'实现了线性表的基本操作，但链表的删除函数存在bug，已修复。','/reports/report2.docx',72.00,1,'2023-09-05 11:30:00','2026-07-14 14:17:17'),(3,1,10,'报告详细，代码规范，测试覆盖全面。','/reports/report3.docx',90.00,1,'2023-09-05 12:00:00','2026-07-14 14:17:17'),(4,2,2,'栈和队列实验完成，十进制转二进制功能正确，表达式求值部分未实现。','/reports/report4.docx',65.00,1,'2023-09-12 10:30:00','2026-07-14 14:17:17'),(5,2,3,'实现了顺序栈和循环队列，实验报告完整。','/reports/report5.docx',78.00,1,'2023-09-12 11:00:00','2026-07-14 14:17:17'),(6,4,2,'进程调度模拟程序完成，界面友好，三种调度算法均实现。','/reports/report6.docx',92.50,1,'2023-09-07 15:00:00','2026-07-14 14:17:17'),(7,4,3,'FCFS和SJF实现正确，RR算法时间片设置有问题。','/reports/report7.docx',68.00,1,'2023-09-07 16:00:00','2026-07-14 14:17:17'),(8,6,4,'静态路由配置成功，网络互通。','/reports/report8.docx',80.00,1,'2023-09-21 11:00:00','2026-07-14 14:17:17'),(9,6,5,'配置过程中出现错误，后经排查解决，报告已记录。','/reports/report9.docx',70.00,1,'2023-09-21 11:30:00','2026-07-14 14:17:17'),(10,7,4,'Wireshark抓包实验，成功捕获HTTP数据包并分析。','/reports/report10.docx',NULL,0,'2023-09-28 10:00:00','2026-07-14 14:17:17'),(11,7,5,'TCP三次握手过程捕获清晰，分析准确。','/reports/report11.docx',NULL,0,'2023-09-28 10:30:00','2026-07-14 14:17:17'),(12,3,2,'二叉树遍历实验完成，前中后序输出正确，但哈夫曼树未实现。','/reports/report12.docx',NULL,0,'2023-09-19 09:30:00','2026-07-14 14:17:17'),(13,11,2,'SQL查询实验完成，各类查询语句运行正确。','/reports/report13.docx',88.00,1,'2023-09-08 11:00:00','2026-07-14 14:17:17'),(14,14,3,'需求分析文档编写完整，用例图绘制规范。','/reports/report14.docx',NULL,0,'2024-02-20 10:00:00','2026-07-14 14:17:17'),(15,14,4,'需求分析报告提交，部分内容需补充。','/reports/report15.docx',NULL,0,'2024-02-20 10:30:00','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_lab_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_plan_detail`
--

DROP TABLE IF EXISTS `tc_plan_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_plan_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '排课主键ID',
  `plan_id` bigint NOT NULL COMMENT '所属计划ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `experiment_id` bigint NOT NULL COMMENT '实验ID',
  `schedule_date` date NOT NULL COMMENT '上课日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_experiment_id` (`experiment_id`),
  CONSTRAINT `fk_detail_course` FOREIGN KEY (`course_id`) REFERENCES `tc_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_detail_experiment` FOREIGN KEY (`experiment_id`) REFERENCES `tc_experiment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_detail_plan` FOREIGN KEY (`plan_id`) REFERENCES `tc_teaching_plan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教学计划排课明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_plan_detail`
--

LOCK TABLES `tc_plan_detail` WRITE;
/*!40000 ALTER TABLE `tc_plan_detail` DISABLE KEYS */;
INSERT INTO `tc_plan_detail` VALUES (1,1,1,1,'2023-09-05','2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,1,1,2,'2023-09-12','2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,1,1,3,'2023-09-19','2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,2,2,4,'2023-09-07','2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,2,2,5,'2023-09-14','2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,2,3,6,'2023-09-21','2026-07-14 14:17:17','2026-07-14 14:17:17'),(7,2,3,7,'2023-09-28','2026-07-14 14:17:17','2026-07-14 14:17:17'),(8,3,1,1,'2023-09-05','2026-07-14 14:17:17','2026-07-14 14:17:17'),(9,3,1,2,'2023-09-12','2026-07-14 14:17:17','2026-07-14 14:17:17'),(10,3,1,8,'2023-09-19','2026-07-14 14:17:17','2026-07-14 14:17:17'),(11,4,4,9,'2023-09-08','2026-07-14 14:17:17','2026-07-14 14:17:17'),(12,4,4,10,'2023-09-15','2026-07-14 14:17:17','2026-07-14 14:17:17'),(13,4,6,12,'2023-10-06','2026-07-14 14:17:17','2026-07-14 14:17:17'),(14,5,5,11,'2024-02-20','2026-07-14 14:17:17','2026-07-14 14:17:17'),(15,5,5,12,'2024-02-27','2026-07-14 14:17:17','2026-07-14 14:17:17'),(16,6,3,6,'2024-03-05','2026-07-14 14:17:17','2026-07-14 14:17:17'),(17,6,3,7,'2024-03-12','2026-07-14 14:17:17','2026-07-14 14:17:17'),(18,6,7,10,'2024-03-19','2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_plan_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_teacher`
--

DROP TABLE IF EXISTS `tc_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '师资主键ID',
  `teacher_id` varchar(30) DEFAULT NULL COMMENT '教师ID（业务编号）',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `type` varchar(30) DEFAULT NULL COMMENT '师资类型：0-实训老师，1-非实训老师',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `company` varchar(100) DEFAULT NULL COMMENT '所在单位',
  `on_job` tinyint(1) NOT NULL DEFAULT '1' COMMENT '在职状态：0-离职，1-在职',
  `user_id` bigint DEFAULT NULL COMMENT '关联用户ID（sm_user.id），便于快速查询',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_teacher_id` (`teacher_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='师资管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_teacher`
--

LOCK TABLES `tc_teacher` WRITE;
/*!40000 ALTER TABLE `tc_teacher` DISABLE KEYS */;
INSERT INTO `tc_teacher` VALUES (1,'T001','陈教授','0','13800000006','teacher01@vtp.edu.cn','虚拟仿真实训中心',1,6,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'T002','刘副教授','1','13800000007','teacher02@vtp.edu.cn','计算机学院',1,7,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'T003','张副教授','0','13800000011','teacher03@vtp.edu.cn','计算机科学与技术学院',1,11,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'T004','李教授','1','13800000012','teacher04@vtp.edu.cn','软件工程学院',1,12,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'T005','王讲师','0','13800000013','teacher05@vtp.edu.cn','自动化学院',1,13,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,'T006','赵高级工程师','1','13800000014','teacher06@vtp.edu.cn','网络空间安全学院',1,14,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_teaching_plan`
--

DROP TABLE IF EXISTS `tc_teaching_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_teaching_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划主键ID',
  `name` varchar(255) NOT NULL COMMENT '计划名称',
  `semester` varchar(50) NOT NULL COMMENT '学期',
  `class_id` bigint NOT NULL COMMENT '班级ID',
  `create_id` bigint NOT NULL COMMENT '创建人ID（sm_user.id，教师创建）',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计划状态：0-草稿，1-已发布',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_create_id` (`create_id`),
  CONSTRAINT `fk_plan_class` FOREIGN KEY (`class_id`) REFERENCES `tc_class` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_plan_creator` FOREIGN KEY (`create_id`) REFERENCES `sm_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教学计划表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_teaching_plan`
--

LOCK TABLES `tc_teaching_plan` WRITE;
/*!40000 ALTER TABLE `tc_teaching_plan` DISABLE KEYS */;
INSERT INTO `tc_teaching_plan` VALUES (1,'2023-2024-1 计科1班 数据结构教学计划','2023-2024-1',1,6,'2023-09-01','2023-12-31',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(2,'2023-2024-1 计科1班 操作系统与网络教学计划','2023-2024-1',1,6,'2023-09-01','2023-12-31',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(3,'2023-2024-1 软件2班 数据结构教学计划','2023-2024-1',2,7,'2023-09-01','2023-12-31',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(4,'2023-2024-1 自动化3班 数据库与算法教学计划','2023-2024-1',3,11,'2023-09-01','2023-12-31',1,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(5,'2023-2024-2 软件2班 软件工程教学计划','2023-2024-2',2,12,'2024-02-15','2024-06-30',0,'2026-07-14 14:17:17','2026-07-14 14:17:17'),(6,'2023-2024-2 计科1班 网络与安全教学计划','2023-2024-2',1,13,'2024-02-15','2024-06-30',0,'2026-07-14 14:17:17','2026-07-14 14:17:17');
/*!40000 ALTER TABLE `tc_teaching_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'vtp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-14 14:25:39
