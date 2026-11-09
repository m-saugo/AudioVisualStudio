CREATE DATABASE  IF NOT EXISTS `audiovisual_studio` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `audiovisual_studio`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: audiovisual_studio
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `ator`
--

DROP TABLE IF EXISTS `ator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ator` (
  `cache` float NOT NULL,
  `papel` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo_personagem` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKmtfu6tddl54uqf49ahv6nd6aq` FOREIGN KEY (`id`) REFERENCES `pessoa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ator`
--

LOCK TABLES `ator` WRITE;
/*!40000 ALTER TABLE `ator` DISABLE KEYS */;
INSERT INTO `ator` VALUES (50000,'Protagonista','Principal',1),(25000,'Protagonista','Principal',4);
/*!40000 ALTER TABLE `ator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diretor`
--

DROP TABLE IF EXISTS `diretor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diretor` (
  `estilo_direcao` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salario` float NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKswfguln2w24i0u2wnmo3spkvs` FOREIGN KEY (`id`) REFERENCES `pessoa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diretor`
--

LOCK TABLES `diretor` WRITE;
/*!40000 ALTER TABLE `diretor` DISABLE KEYS */;
INSERT INTO `diretor` VALUES ('drama',70000,2);
/*!40000 ALTER TABLE `diretor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filme`
--

DROP TABLE IF EXISTS `filme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filme` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `duracao` int NOT NULL,
  `genero` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filme`
--

LOCK TABLES `filme` WRITE;
/*!40000 ALTER TABLE `filme` DISABLE KEYS */;
INSERT INTO `filme` VALUES (2,140,'Romance','Além das Estrelas');
/*!40000 ALTER TABLE `filme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filme_ator`
--

DROP TABLE IF EXISTS `filme_ator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filme_ator` (
  `filme_codigo` bigint NOT NULL,
  `ator_id` bigint NOT NULL,
  PRIMARY KEY (`filme_codigo`,`ator_id`),
  KEY `FKqb4g9o04pcwdbsf2rt5vnt1sw` (`ator_id`),
  CONSTRAINT `FKhsb1tkxry6rr61nxdsr9v568r` FOREIGN KEY (`filme_codigo`) REFERENCES `filme` (`codigo`),
  CONSTRAINT `FKqb4g9o04pcwdbsf2rt5vnt1sw` FOREIGN KEY (`ator_id`) REFERENCES `ator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filme_ator`
--

LOCK TABLES `filme_ator` WRITE;
/*!40000 ALTER TABLE `filme_ator` DISABLE KEYS */;
INSERT INTO `filme_ator` VALUES (2,1),(2,4);
/*!40000 ALTER TABLE `filme_ator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filme_diretor`
--

DROP TABLE IF EXISTS `filme_diretor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filme_diretor` (
  `filme_codigo` bigint NOT NULL,
  `diretor_id` bigint NOT NULL,
  PRIMARY KEY (`filme_codigo`,`diretor_id`),
  KEY `FK713eimhe3kmwautqoyj70w3wf` (`diretor_id`),
  CONSTRAINT `FK4i4fu4di3i68mqfpjquspoj4k` FOREIGN KEY (`filme_codigo`) REFERENCES `filme` (`codigo`),
  CONSTRAINT `FK713eimhe3kmwautqoyj70w3wf` FOREIGN KEY (`diretor_id`) REFERENCES `diretor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filme_diretor`
--

LOCK TABLES `filme_diretor` WRITE;
/*!40000 ALTER TABLE `filme_diretor` DISABLE KEYS */;
INSERT INTO `filme_diretor` VALUES (2,2);
/*!40000 ALTER TABLE `filme_diretor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filme_roteirista`
--

DROP TABLE IF EXISTS `filme_roteirista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filme_roteirista` (
  `filme_codigo` bigint NOT NULL,
  `roteirista_id` bigint NOT NULL,
  PRIMARY KEY (`filme_codigo`,`roteirista_id`),
  KEY `FKt1vfjn71d5qwlot3h56oqhpwg` (`roteirista_id`),
  CONSTRAINT `FKmraifsvxoq7rxy6cb26m9mtv` FOREIGN KEY (`filme_codigo`) REFERENCES `filme` (`codigo`),
  CONSTRAINT `FKt1vfjn71d5qwlot3h56oqhpwg` FOREIGN KEY (`roteirista_id`) REFERENCES `roteirista` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filme_roteirista`
--

LOCK TABLES `filme_roteirista` WRITE;
/*!40000 ALTER TABLE `filme_roteirista` DISABLE KEYS */;
INSERT INTO `filme_roteirista` VALUES (2,3);
/*!40000 ALTER TABLE `filme_roteirista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pessoa`
--

DROP TABLE IF EXISTS `pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pessoa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cpf` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `idade` int NOT NULL,
  `nacionalidade` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pessoa`
--

LOCK TABLES `pessoa` WRITE;
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` VALUES (1,'010.010.010-00',25,'Brasileira','Maria Silva'),(2,'037.079.627-27',55,'Brasileira','Joao Pereira'),(3,'12694765998',25,'Brasileira','Pedro Santos'),(4,'12694765998',19,'Brasileira','MARIA EDUARDA FELICIANO LUNELLI');
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roteirista`
--

DROP TABLE IF EXISTS `roteirista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roteirista` (
  `especialidade` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salario` float NOT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKn3e8mc1ogdcqw6u9c2rncbw36` FOREIGN KEY (`id`) REFERENCES `pessoa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roteirista`
--

LOCK TABLES `roteirista` WRITE;
/*!40000 ALTER TABLE `roteirista` DISABLE KEYS */;
INSERT INTO `roteirista` VALUES ('Ficção',90000,3);
/*!40000 ALTER TABLE `roteirista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `senha_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_usuario` enum('ADMINISTRADOR','ATOR','DIRETOR','ROTEIRISTA') COLLATE utf8mb4_unicode_ci NOT NULL,
  `pessoa_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`email`),
  UNIQUE KEY `UKr3paqktjbbb5iuok1mvu0s8xg` (`pessoa_id`),
  CONSTRAINT `FK9kjbckbnh9ryosephjpbojisa` FOREIGN KEY (`pessoa_id`) REFERENCES `pessoa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'mariaeduardafelicianolunelli@gmail.com','$2a$10$Yt5ObUhJUua1BHVO7l8xduK7r/b2vrNQg4uZfaMKzd5LYpY1Oy506','ADMINISTRADOR',NULL),(2,'marialunelli@alunos.utfpr.edu.br','$2a$10$0Bbi.vh9bBz3xQb6DD81W.zGGqL9in7Q1MFff4Y/8t6sM6Am1xAGu','ATOR',4);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-08 13:11:02
