--
-- Server version	8.0.17
--

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alert_log`
--

DROP TABLE IF EXISTS `alert_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert_log` (
  `alert_log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  `prisoner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`alert_log_id`),
  KEY `FK96g0xchhbwjwo4bq1p09lrnsf` (`prisoner_id`),
  CONSTRAINT `FK96g0xchhbwjwo4bq1p09lrnsf` FOREIGN KEY (`prisoner_id`) REFERENCES `prisoner` (`prisoner_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_log`
--

LOCK TABLES `alert_log` WRITE;
/*!40000 ALTER TABLE `alert_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `annotation`
--

DROP TABLE IF EXISTS `annotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `annotation` (
  `annotation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_updated_timestamp` datetime(6) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `annotation_dest_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `prison_dest_id` bigint(20) DEFAULT NULL,
  `prisoner_dest_id` bigint(20) DEFAULT NULL,
  `user_dest_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`annotation_id`),
  KEY `FK5nl6kotk0i47cvovfrurghfoo` (`user_dest_id`),
  KEY `FKi6co4hgy8462mtr1syt5ac863` (`prison_dest_id`),
  KEY `FKn3h6xmlu5u55p4u7em9p1fi7t` (`annotation_dest_id`),
  KEY `FKphs49kcylp7rru7nemr4c4rss` (`prisoner_dest_id`),
  KEY `FKrkjh4n3cqld60jrvfioevt3j6` (`created_by`),
  CONSTRAINT `FK5nl6kotk0i47cvovfrurghfoo` FOREIGN KEY (`user_dest_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKi6co4hgy8462mtr1syt5ac863` FOREIGN KEY (`prison_dest_id`) REFERENCES `prison` (`prison_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKn3h6xmlu5u55p4u7em9p1fi7t` FOREIGN KEY (`annotation_dest_id`) REFERENCES `annotation` (`annotation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKphs49kcylp7rru7nemr4c4rss` FOREIGN KEY (`prisoner_dest_id`) REFERENCES `prisoner` (`prisoner_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKrkjh4n3cqld60jrvfioevt3j6` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `annotation`
--

LOCK TABLES `annotation` WRITE;
/*!40000 ALTER TABLE `annotation` DISABLE KEYS */;
/*!40000 ALTER TABLE `annotation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criminal_record`
--

DROP TABLE IF EXISTS `criminal_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `criminal_record` (
  `criminal_record_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_timestamp` datetime(6) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `emission_date` date NOT NULL,
  `last_updated_timestamp` datetime(6) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `prisoner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`criminal_record_id`),
  KEY `FKh4dx6ab1vflps2erbej4t3r9k` (`prisoner_id`),
  CONSTRAINT `FKh4dx6ab1vflps2erbej4t3r9k` FOREIGN KEY (`prisoner_id`) REFERENCES `prisoner` (`prisoner_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criminal_record`
--

LOCK TABLES `criminal_record` WRITE;
/*!40000 ALTER TABLE `criminal_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `criminal_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facial_recognition`
--

DROP TABLE IF EXISTS `facial_recognition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facial_recognition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `secret` varchar(255) DEFAULT NULL,
  `pic_byte` longblob,
  `type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fr_user_id_idx` (`user_id`),
  CONSTRAINT `fr_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facial_recognition`
--

LOCK TABLES `facial_recognition` WRITE;
/*!40000 ALTER TABLE `facial_recognition` DISABLE KEYS */;
/*!40000 ALTER TABLE `facial_recognition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_table`
--

DROP TABLE IF EXISTS `image_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `pic_byte` longblob,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_table`
--

LOCK TABLES `image_table` WRITE;
/*!40000 ALTER TABLE `image_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_prescription`
--

DROP TABLE IF EXISTS `medical_prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_prescription` (
  `prescription_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_timestamp` datetime(6) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_updated_timestamp` datetime(6) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `prisoner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`prescription_id`),
  KEY `FKs8xwpni6gkvmc8fk0bemndaxi` (`prisoner_id`),
  CONSTRAINT `FKs8xwpni6gkvmc8fk0bemndaxi` FOREIGN KEY (`prisoner_id`) REFERENCES `prisoner` (`prisoner_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_prescription`
--

LOCK TABLES `medical_prescription` WRITE;
/*!40000 ALTER TABLE `medical_prescription` DISABLE KEYS */;
/*!40000 ALTER TABLE `medical_prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prison`
--

DROP TABLE IF EXISTS `prison`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prison` (
  `prison_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `contact` varchar(9) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `photo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`prison_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prison`
--

LOCK TABLES `prison` WRITE;
/*!40000 ALTER TABLE `prison` DISABLE KEYS */;
/*!40000 ALTER TABLE `prison` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisoner`
--

DROP TABLE IF EXISTS `prisoner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisoner` (
  `prisoner_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alert_off` tinyint(1) DEFAULT '0',
  `alternative_contact` varchar(9) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `birth_date` date NOT NULL,
  `bracelet_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `cell` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `contact` varchar(9) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `created_timestamp` datetime(6) DEFAULT NULL,
  `identifier_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `maxhb` int(11) NOT NULL DEFAULT '120',
  `minhb` int(11) NOT NULL DEFAULT '30',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `nationality` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `photo_id` bigint(20) DEFAULT NULL,
  `threat_level` int(11) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `prison_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`prisoner_id`),
  UNIQUE KEY `bracelet_id_UNIQUE` (`bracelet_id`),
  UNIQUE KEY `identifier_id_UNIQUE` (`identifier_id`),
  KEY `FKce0j0rbo93xg4nm9ejk8u053d` (`prison_id`),
  KEY `FKokmde1lsxyc3lrsoowi5sovow` (`created_by`),
  CONSTRAINT `FKce0j0rbo93xg4nm9ejk8u053d` FOREIGN KEY (`prison_id`) REFERENCES `prison` (`prison_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FKokmde1lsxyc3lrsoowi5sovow` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisoner`
--

LOCK TABLES `prisoner` WRITE;
/*!40000 ALTER TABLE `prisoner` DISABLE KEYS */;
/*!40000 ALTER TABLE `prisoner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisoner_log`
--

DROP TABLE IF EXISTS `prisoner_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisoner_log` (
  `prisoner_log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `log_timestamp` datetime(6) DEFAULT NULL,
  `by_user` bigint(20) DEFAULT NULL,
  `prisoner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`prisoner_log_id`),
  KEY `FK13sfgtxufu7rgnfvwk2txiuj` (`prisoner_id`),
  KEY `FKi12mskgj1mk2q4wsqbhft8yuc` (`by_user`),
  CONSTRAINT `FK13sfgtxufu7rgnfvwk2txiuj` FOREIGN KEY (`prisoner_id`) REFERENCES `prisoner` (`prisoner_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FKi12mskgj1mk2q4wsqbhft8yuc` FOREIGN KEY (`by_user`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=408 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisoner_log`
--

LOCK TABLES `prisoner_log` WRITE;
/*!40000 ALTER TABLE `prisoner_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `prisoner_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (0,'ROLE_GUARD'),(1,'ROLE_MANAGER'),(2,'ROLE_NETWORKMAN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `FKa50n59y1j4a6qwa42p8jiguds` (`user_id`),
  CONSTRAINT `FKa50n59y1j4a6qwa42p8jiguds` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `birth_date` date NOT NULL,
  `contact` varchar(9) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_timestamp` datetime(6) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `nationality` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `photo_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `prison_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  KEY `FK7nr6nqh84tsorn8iogwfobc73` (`prison_id`),
  CONSTRAINT `FK7nr6nqh84tsorn8iogwfobc73` FOREIGN KEY (`prison_id`) REFERENCES `prison` (`prison_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_log`
--

DROP TABLE IF EXISTS `user_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_log` (
  `user_log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `log_timestamp` datetime(6) DEFAULT NULL,
  `by_user` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_log_id`),
  KEY `FKmd6gmr2tvduf9qvif1nchhqfm` (`user_id`),
  KEY `FKpl92sgaciyd14tajut3owys36` (`by_user`),
  CONSTRAINT `FKmd6gmr2tvduf9qvif1nchhqfm` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FKpl92sgaciyd14tajut3owys36` FOREIGN KEY (`by_user`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_log`
--

LOCK TABLES `user_log` WRITE;
/*!40000 ALTER TABLE `user_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
