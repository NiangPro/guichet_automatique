-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 24 nov. 2021 à 10:56
-- Version du serveur : 10.4.21-MariaDB
-- Version de PHP : 8.0.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `guichet_auto`
--

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `code` varchar(50) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `tel` varchar(30) NOT NULL,
  `courriel` varchar(250) NOT NULL,
  `sexe` char(2) NOT NULL,
  `nip` int(11) NOT NULL,
  `estAdmin` int(11) NOT NULL DEFAULT 0,
  `acces` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`code`, `prenom`, `nom`, `tel`, `courriel`, `sexe`, `nip`, `estAdmin`, `acces`) VALUES
('admin', 'admin', 'admin', '773243432', 'admin@gmail.com', 'M', 1234, 1, 1),
('ass', 'Ass', 'Fall', '778463212', 'malick@gmail.com', 'M', 1234, 0, 1),
('fallpro', 'Alioune', 'Fall', '778479876', 'fallpro@gmail.com', 'M', 1234, 0, 1),
('mamyta', 'mareme', 'sylla', '778676541', 'mamy@gmail.com', 'F', 1234, 0, 1),
('niangpro', 'Bassirou', 'Niang', '776453563', 'niangpro@gmail.com', 'M', 1234, 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `compte`
--

CREATE TABLE `compte` (
  `solde` float NOT NULL DEFAULT 0,
  `etat` int(11) NOT NULL DEFAULT 0,
  `type` varchar(20) NOT NULL,
  `code_client` varchar(30) NOT NULL,
  `dateOuverture` date NOT NULL DEFAULT current_timestamp(),
  `numCpt` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `compte`
--

INSERT INTO `compte` (`solde`, `etat`, `type`, `code_client`, `dateOuverture`, `numCpt`) VALUES
(1049.22, 1, 'Compte Cheque', 'mamyta', '2021-11-16', 'CCH001'),
(351.484, 1, 'Compte Cheque', 'ass', '2021-11-19', 'CCH002'),
(19.25, 1, 'Compte Cheque', 'niangpro', '2021-11-20', 'CCH003'),
(200, 1, 'Compte Epargne', 'ass', '2021-11-24', 'CEP002'),
(23, 1, 'Compte Epargne', 'ass', '2021-11-23', 'CEPm001'),
(45, 1, 'Compte Hypothecaire', 'mamyta', '2021-11-16', 'CHT001'),
(10.243, 1, 'Marge Credit', 'mamyta', '2021-11-17', 'CMC001');

-- --------------------------------------------------------

--
-- Structure de la table `guichet`
--

CREATE TABLE `guichet` (
  `id` int(11) NOT NULL,
  `balance` float NOT NULL DEFAULT 0,
  `nbreBillet` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `guichet`
--

INSERT INTO `guichet` (`id`, `balance`, `nbreBillet`) VALUES
(1, 1430, 931);

-- --------------------------------------------------------

--
-- Structure de la table `historique`
--

CREATE TABLE `historique` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `numOpt` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `historique`
--

INSERT INTO `historique` (`id`, `date`, `numOpt`) VALUES
(1, '2021-11-18', 'Opt0004'),
(2, '2021-11-19', 'Opt0005'),
(3, '2021-11-19', 'Opt0006'),
(4, '2021-11-19', 'Opt0007'),
(5, '2021-11-19', 'Opt0008'),
(6, '2021-11-19', 'Opt0009'),
(7, '2021-11-19', 'Opt00010'),
(8, '2021-11-19', 'Opt00011'),
(9, '2021-11-19', 'Opt12'),
(10, '2021-11-19', 'Opt13'),
(11, '2021-11-19', 'Opt14'),
(12, '2021-11-19', 'Opt15'),
(13, '2021-11-19', 'Opt16'),
(14, '2021-11-19', 'Opt17'),
(15, '2021-11-19', 'Opt18'),
(16, '2021-11-19', 'Opt19'),
(17, '2021-11-19', 'Opt20'),
(18, '2021-11-19', 'Opt21'),
(19, '2021-11-19', 'Opt22'),
(20, '2021-11-19', 'Opt23'),
(21, '2021-11-20', 'Opt24'),
(22, '2021-11-20', 'Opt25'),
(23, '2021-11-20', 'Opt26'),
(24, '2021-11-20', 'Opt27'),
(25, '2021-11-20', 'Opt28'),
(26, '2021-11-20', 'Opt29'),
(27, '2021-11-20', 'Opt30'),
(28, '2021-11-20', 'Opt31'),
(29, '2021-11-20', 'Opt32'),
(30, '2021-11-20', 'Opt33'),
(31, '2021-11-20', 'Opt34'),
(32, '2021-11-20', 'Opt35'),
(33, '2021-11-20', 'Opt36'),
(34, '2021-11-20', 'Opt37'),
(35, '2021-11-20', 'Opt38'),
(36, '2021-11-20', 'Opt39'),
(37, '2021-11-24', 'Opt40'),
(38, '2021-11-24', 'Opt41');

-- --------------------------------------------------------

--
-- Structure de la table `operation`
--

CREATE TABLE `operation` (
  `montant` float NOT NULL,
  `typeOpt` varchar(100) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `numCpt` varchar(100) NOT NULL,
  `nbreBillet` int(11) DEFAULT NULL,
  `numOpt` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `operation`
--

INSERT INTO `operation` (`montant`, `typeOpt`, `date`, `numCpt`, `nbreBillet`, `numOpt`) VALUES
(5, 'Depot', '2021-11-17', 'CCH001', 0, 'Opt0001'),
(20, 'Retrait', '2021-11-19', 'CCH001', 0, 'Opt00010'),
(20, 'Retrait', '2021-11-19', 'CCH001', 0, 'Opt00011'),
(10, 'Depot', '2021-11-17', 'CHT001', 0, 'Opt0002'),
(5, 'Depot', '2021-11-18', 'CHT001', 0, 'Opt0003'),
(5, 'Depot', '2021-11-18', 'CHT001', 0, 'Opt0004'),
(90, 'Retrait', '2021-11-19', 'CCH001', 0, 'Opt0005'),
(15, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt0006'),
(5, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt0007'),
(5, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt0008'),
(5, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt0009'),
(10, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt12'),
(20, 'Retrait', '2021-11-19', 'CCH001', 0, 'Opt13'),
(10, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt14'),
(5, 'Transfert', '2021-11-19', 'CCH001', 0, 'Opt15'),
(2, 'Paiement Facture', '2021-11-19', 'CCH001', 0, 'Opt16'),
(10, 'Depot', '2021-11-19', 'CCH001', 0, 'Opt17'),
(2.25, 'Paiement Facture', '2021-11-19', 'CCH001', 0, 'Opt18'),
(3.25, 'Paiement Facture', '2021-11-19', 'CCH001', 0, 'Opt19'),
(3.25, 'Paiement Facture', '2021-11-19', 'CCH001', 0, 'Opt20'),
(2.25, 'Paiement Facture', '2021-11-19', 'CCH001', 0, 'Opt21'),
(25, 'Depot', '2021-11-19', 'CCH002', 0, 'Opt22'),
(30, 'Retrait', '2021-11-19', 'CCH002', 0, 'Opt23'),
(100, 'Depot', '2021-11-20', 'CCH001', 0, 'Opt24'),
(50, 'Retrait', '2021-11-20', 'CCH001', 5, 'Opt25'),
(60, 'Retrait', '2021-11-20', 'CCH001', 6, 'Opt26'),
(20, 'Depot', '2021-11-20', 'CCH001', 0, 'Opt27'),
(14, 'Transfert', '2021-11-20', 'CCH001', 0, 'Opt28'),
(2000, 'Depot', '2021-11-20', 'CCH001', 0, 'Opt29'),
(1000, 'Retrait', '2021-11-20', 'CCH001', 100, 'Opt30'),
(1000, 'Retrait', '2021-11-20', 'CCH001', 100, 'Opt31'),
(2000, 'Depot', '2021-11-20', 'CCH001', 0, 'Opt32'),
(1000, 'Retrait', '2021-11-20', 'CCH001', 100, 'Opt33'),
(300, 'Depot', '2021-11-20', 'CCH002', 0, 'Opt34'),
(50, 'Depot', '2021-11-20', 'CCH003', 0, 'Opt35'),
(70, 'Retrait', '2021-11-20', 'CCH003', 7, 'Opt36'),
(30, 'Transfert', '2021-11-20', 'CCH003', 0, 'Opt37'),
(30, 'Depot', '2021-11-20', 'CCH003', 0, 'Opt38'),
(11.25, 'Paiement Facture', '2021-11-20', 'CCH003', 0, 'Opt39'),
(23, 'Depot', '2021-11-24', 'CCH001', 0, 'Opt40'),
(77, 'Depot', '2021-11-24', 'CEP002', 0, 'Opt41');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`code`);

--
-- Index pour la table `compte`
--
ALTER TABLE `compte`
  ADD PRIMARY KEY (`numCpt`),
  ADD KEY  (`code_client`);

--
-- Index pour la table `guichet`
--
ALTER TABLE `guichet`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `historique`
--
ALTER TABLE `historique`
  ADD PRIMARY KEY (`id`),
  ADD KEY  (`numOpt`);

--
-- Index pour la table `operation`
--
ALTER TABLE `operation`
  ADD PRIMARY KEY (`numOpt`),
  ADD KEY  (`numCpt`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `guichet`
--
ALTER TABLE `guichet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `historique`
--
ALTER TABLE `historique`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `compte`
--
ALTER TABLE `compte`
  ADD CONSTRAINT `compte_ibfk_1` FOREIGN KEY (`code_client`) REFERENCES `client` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `historique`
--
ALTER TABLE `historique`
  ADD CONSTRAINT `historique_ibfk_1` FOREIGN KEY (`numOpt`) REFERENCES `operation` (`numOpt`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `operation`
--
ALTER TABLE `operation`
  ADD CONSTRAINT `operation_ibfk_1` FOREIGN KEY (`numCpt`) REFERENCES `compte` (`numCpt`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
