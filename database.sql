DROP DATABASE IF EXISTS test;

CREATE DATABASE `test` ;

USE test;

CREATE TABLE `users` (
  `mail` varchar(90),
  `password` varchar(45) DEFAULT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prenom` varchar(45) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `sexe` varchar(1) DEFAULT NULL,
  `budget` int DEFAULT NULL,
  `temps` int DEFAULT NULL,
  PRIMARY KEY (`mail`)
);

CREATE TABLE `recette` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(45) DEFAULT NULL,
  `temps` int DEFAULT NULL,
  `budget` int DEFAULT NULL,
  `image` MEDIUMBLOB DEFAULT NULL,
  `ingredients` varchar(500) DEFAULT NULL,
  `prepa` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


INSERT INTO `test`.`recette` (`nom`,`temps`,`budget`, `ingredients`)VALUES("Lasagnes",180,30, "pate\nlait");
INSERT INTO `test`.`recette` (`nom`,`temps`,`budget`)VALUES("Pates",10,2);
INSERT INTO `test`.`recette` (`nom`,`temps`,`budget`)VALUES("Gateau au Chocolat",45,10);
