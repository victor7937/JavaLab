-- MySQL dump 10.13  Distrib 8.0.25, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: certificate_db
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `gift_certificate`
--

DROP TABLE IF EXISTS `gift_certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_certificate` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(70) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `price` decimal(6,2) NOT NULL,
  `duration` int NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_certificate`
--

LOCK TABLES `gift_certificate` WRITE;
/*!40000 ALTER TABLE `gift_certificate` DISABLE KEYS */;
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (1,'lessons certificate','lessons certificate for 2 lessons',20.00,2,'2021-06-08 14:28:20','2021-06-08 15:11:52');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (2,'Programming lessons certificate','lessons certificate for 2 lessons',25.70,3,'2021-06-08 14:28:20','2021-06-11 01:38:16');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (3,'science-sertificate','lessons certificate for 2 lessons',20.00,2,'2021-06-08 21:44:22','2021-06-08 21:44:22');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (11,'physics science','lessons certificate for 3 math lessons',25.00,3,'2021-06-08 23:31:57','2021-06-08 23:31:57');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (15,'Java learning','Java programming learning online course',15.50,9,'2021-06-11 15:49:41','2021-06-11 15:54:42');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (16,'Java learning advanced','Java programming advanced lavel learning online course',15.00,5,'2021-06-11 15:56:22','2021-06-11 15:56:22');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (17,'Java Web Learning','Java web development advanced lavel learning online course',22.70,10,'2021-06-12 05:13:06','2021-06-12 05:24:03');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (18,'Perfume certificate','20% discount for all perfume in the Kravt shop',1.00,10,'2021-06-12 08:43:08','2021-06-12 23:58:13');
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`, `create_date`, `last_update_date`) VALUES (19,'Gucci perfume certificate','15% discount for all Gucci perfume in the Kravt shop',1.00,10,'2021-06-13 01:11:38','2021-06-13 01:18:52');
/*!40000 ALTER TABLE `gift_certificate` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS update_trigger */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_trigger` BEFORE UPDATE ON `gift_certificate` FOR EACH ROW BEGIN
    SET NEW.last_update_date=CURRENT_TIMESTAMP;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `m2m_certificate_tag`
--

DROP TABLE IF EXISTS `m2m_certificate_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m2m_certificate_tag` (
  `cert_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`cert_id`,`tag_id`),
  KEY `m2m_certificate_tag_tag_id_fk` (`tag_id`),
  CONSTRAINT `m2m_certificate_tag_gift_certificate_id_fk` FOREIGN KEY (`cert_id`) REFERENCES `gift_certificate` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `m2m_certificate_tag_tag_id_fk` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m2m_certificate_tag`
--

LOCK TABLES `m2m_certificate_tag` WRITE;
/*!40000 ALTER TABLE `m2m_certificate_tag` DISABLE KEYS */;
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (1,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (2,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (3,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (15,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (16,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,1);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (1,2);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (15,2);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (16,2);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,2);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (1,3);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (2,4);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (18,4);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (15,11);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (16,11);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,11);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (16,12);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,12);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,13);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (17,14);
INSERT INTO `m2m_certificate_tag` (`cert_id`, `tag_id`) VALUES (18,16);
/*!40000 ALTER TABLE `m2m_certificate_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_name_uindex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` (`id`, `name`) VALUES (16,'discount');
INSERT INTO `tag` (`id`, `name`) VALUES (17,'gucci');
INSERT INTO `tag` (`id`, `name`) VALUES (14,'hibernate');
INSERT INTO `tag` (`id`, `name`) VALUES (7,'HR');
INSERT INTO `tag` (`id`, `name`) VALUES (8,'IT');
INSERT INTO `tag` (`id`, `name`) VALUES (11,'java');
INSERT INTO `tag` (`id`, `name`) VALUES (13,'javaee');
INSERT INTO `tag` (`id`, `name`) VALUES (1,'online');
INSERT INTO `tag` (`id`, `name`) VALUES (3,'online study');
INSERT INTO `tag` (`id`, `name`) VALUES (4,'perfume');
INSERT INTO `tag` (`id`, `name`) VALUES (12,'spring');
INSERT INTO `tag` (`id`, `name`) VALUES (2,'study');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-14 21:11:55
