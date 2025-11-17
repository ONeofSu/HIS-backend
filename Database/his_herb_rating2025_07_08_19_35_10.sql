-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: herb_rating
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `evaluation_application`
--

DROP TABLE IF EXISTS `evaluation_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_application` (
  `application_id` bigint NOT NULL AUTO_INCREMENT,
  `evaluation_id` bigint NOT NULL,
  `application_state` varchar(50) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_application`
--

LOCK TABLES `evaluation_application` WRITE;
/*!40000 ALTER TABLE `evaluation_application` DISABLE KEYS */;
INSERT INTO `evaluation_application` VALUES (1,1,'审核通过',3),(2,2,'审核通过',3),(3,3,'审核中',3);
/*!40000 ALTER TABLE `evaluation_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_detail`
--

DROP TABLE IF EXISTS `evaluation_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_detail` (
  `evaluation_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `evaluation_id` bigint NOT NULL,
  `indicator_id` bigint NOT NULL,
  `indicator_score` decimal(5,2) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `material` varchar(255) NOT NULL,
  PRIMARY KEY (`evaluation_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_detail`
--

LOCK TABLES `evaluation_detail` WRITE;
/*!40000 ALTER TABLE `evaluation_detail` DISABLE KEYS */;
INSERT INTO `evaluation_detail` VALUES (1,1,1,80.00,'一般','material.jpg'),(2,1,2,80.00,'一般','material.jpg'),(3,1,3,80.00,'一般','material.jpg'),(4,1,4,80.00,'一般','material.jpg'),(5,1,5,80.00,'一般','material.jpg'),(6,1,6,80.00,'一般','material.jpg'),(7,2,1,90.00,'不错','material.png'),(8,2,2,90.00,'不错','material.png'),(9,2,3,90.00,'不错','material.png'),(10,2,4,90.00,'不错','material.png'),(11,2,5,90.00,'不错','material.png'),(12,2,6,90.00,'不错','material.png'),(13,3,1,60.00,'草料','material.txt'),(14,3,2,60.00,'草料','material.txt'),(15,3,3,60.00,'草料','material.txt'),(16,3,4,60.00,'草料','material.txt'),(17,3,5,60.00,'草料','material.txt'),(18,3,6,60.00,'草料','material.txt');
/*!40000 ALTER TABLE `evaluation_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_indicator`
--

DROP TABLE IF EXISTS `evaluation_indicator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_indicator` (
  `indicator_id` bigint NOT NULL AUTO_INCREMENT,
  `indicator_name` varchar(50) NOT NULL,
  `weight` decimal(5,2) NOT NULL,
  PRIMARY KEY (`indicator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_indicator`
--

LOCK TABLES `evaluation_indicator` WRITE;
/*!40000 ALTER TABLE `evaluation_indicator` DISABLE KEYS */;
INSERT INTO `evaluation_indicator` VALUES (1,'药用价值',0.20),(2,'外观价值',0.10),(3,'经济价值',0.15),(4,'药效表现',0.20),(5,'药物安全',0.20),(6,'来源渠道',0.15);
/*!40000 ALTER TABLE `evaluation_indicator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `herb_evaluation`
--

DROP TABLE IF EXISTS `herb_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `herb_evaluation` (
  `evaluation_id` bigint NOT NULL AUTO_INCREMENT,
  `herb_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `evaluation_state` varchar(50) NOT NULL,
  `auditor_user_id` bigint NOT NULL,
  `total_score` decimal(5,2) NOT NULL,
  `evaluate_time` datetime NOT NULL,
  PRIMARY KEY (`evaluation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_evaluation`
--

LOCK TABLES `herb_evaluation` WRITE;
/*!40000 ALTER TABLE `herb_evaluation` DISABLE KEYS */;
INSERT INTO `herb_evaluation` VALUES (1,1,1,'审核通过',3,80.00,'2025-06-25 22:50:54'),(2,2,2,'审核通过',3,90.00,'2025-06-28 22:50:54'),(3,1,2,'审核中',3,60.00,'2025-06-30 15:37:54');
/*!40000 ALTER TABLE `herb_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `herb_rating`
--

DROP TABLE IF EXISTS `herb_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `herb_rating` (
  `herb_evaluation_id` bigint NOT NULL AUTO_INCREMENT,
  `herb_id` bigint NOT NULL,
  `total_score` decimal(5,2) NOT NULL,
  PRIMARY KEY (`herb_evaluation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_rating`
--

LOCK TABLES `herb_rating` WRITE;
/*!40000 ALTER TABLE `herb_rating` DISABLE KEYS */;
INSERT INTO `herb_rating` VALUES (1,1,80.00),(2,2,90.00);
/*!40000 ALTER TABLE `herb_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `herb_rating_detail`
--

DROP TABLE IF EXISTS `herb_rating_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `herb_rating_detail` (
  `herb_evaluation_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `herb_evaluation_id` bigint NOT NULL,
  `herb_id` bigint NOT NULL,
  `indicator_id` bigint NOT NULL,
  `avg_score` decimal(5,2) NOT NULL,
  PRIMARY KEY (`herb_evaluation_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_rating_detail`
--

LOCK TABLES `herb_rating_detail` WRITE;
/*!40000 ALTER TABLE `herb_rating_detail` DISABLE KEYS */;
INSERT INTO `herb_rating_detail` VALUES (1,1,1,1,80.00),(2,1,1,2,80.00),(3,1,1,3,80.00),(4,1,1,4,80.00),(5,1,1,5,80.00),(6,1,1,6,80.00),(7,2,2,1,90.00),(8,2,2,2,90.00),(9,2,2,3,90.00),(10,2,2,4,90.00),(11,2,2,5,90.00),(12,2,2,6,90.00);
/*!40000 ALTER TABLE `herb_rating_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-08 19:35:10
