-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: medilabo
-- ------------------------------------------------------
-- Server version	8.0.39

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;

CREATE TABLE `patients` (
  `date_naissance` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `genre` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;

INSERT INTO `patients` VALUES ('1966-12-31 01:00:00.000000',1,'F','TestNone','Test','1 Brookside St','100-222-3333'),('1945-06-24 00:00:00.000000',2,'M','TestBorderline','Test','2 High St','200-333-4444'),('2004-06-18 01:00:00.000000',3,'M','TestInDanger','Test','3 Club Road','300-444-5555'),('2002-06-28 01:00:00.000000',4,'F','TestEarlyOnset','Test','4 Valley Dr','400-555-6666');

UNLOCK TABLES;

-- Dump completed on 2025-06-23  9:42:35
