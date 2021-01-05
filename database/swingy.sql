-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 05 jan. 2021 à 17:31
-- Version du serveur :  8.0.19
-- Version de PHP : 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `swingy`
--

-- --------------------------------------------------------

--
-- Structure de la table `artefacts`
--

CREATE TABLE `artefacts` (
  `A_Name` text NOT NULL,
  `A_Type` text NOT NULL,
  `A_Amount` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `artefacts`
--

INSERT INTO `artefacts` (`A_Name`, `A_Type`, `A_Amount`) VALUES
('Wood sword', 'Weapon', 5),
('Iron sword', 'Weapon', 15),
('Diamond sword', 'Weapon', 30),
('Wood chestplate', 'Armor', 5),
('Iron chestplate', 'Armor', 15),
('Diamond chestplate', 'Armor', 30),
('Wood helm', 'Helm', 5),
('Iron helm', 'Helm', 15),
('Diamond helm', 'Helm', 30);

-- --------------------------------------------------------

--
-- Structure de la table `heroes`
--

CREATE TABLE `heroes` (
  `H_Name` text NOT NULL,
  `H_Class` text NOT NULL,
  `H_Level` int NOT NULL,
  `H_Experience` int NOT NULL,
  `H_Attack` int NOT NULL,
  `H_Defense` int NOT NULL,
  `H_HitPoints` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `heroes`
--

INSERT INTO `heroes` (`H_Name`, `H_Class`, `H_Level`, `H_Experience`, `H_Attack`, `H_Defense`, `H_HitPoints`) VALUES
('Achill', 'Guerrier', 3, 3050, 130, 60, 475),
('Bob', 'Forgeron', 1, 0, 30, 10, 100),
('Hector', 'Guerrier', 1, 0, 30, 10, 100),
('test', 'test', 1, 0, 30, 10, 100),
('Morgane', 'Valkirie', 1, 0, 30, 10, 100),
('Kassandra', 'Queen', 1, 0, 30, 10, 100),
('Aatrox', 'Demon', 6, 5500, 430, 210, 1600),
('ggg', 'ggg', 1, 500, 30, 10, 100),
('hhhh', 'hhhh', 2, 500, 70, 30, 250);

-- --------------------------------------------------------

--
-- Structure de la table `heroesartefacts`
--

CREATE TABLE `heroesartefacts` (
  `H_Name` text NOT NULL,
  `A_Name` text NOT NULL,
  `A_Type` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `heroesartefacts`
--

INSERT INTO `heroesartefacts` (`H_Name`, `A_Name`, `A_Type`) VALUES
('Achill', 'Diamond sword', 'Weapon'),
('hhhh', 'Diamond sword', 'Weapon');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
