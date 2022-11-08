DROP DATABASE IF EXISTS test;

CREATE DATABASE `test` ;

USE test;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mail` varchar(90) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prénom` varchar(45) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `budget` int DEFAULT NULL,
  `temps` int DEFAULT NULL,
  PRIMARY KEY (`mail`)
);

CREATE TABLE `recette` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(45) DEFAULT NULL,
  `temps` int DEFAULT NULL,
  `budget` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `test`.`recette` (`nom`,`temps`,`budget`)VALUES("Pates",10,2);
INSERT INTO `test`.`recette` (`nom`,`temps`,`budget`)VALUES("Gateau au Chocolat",45,10);
