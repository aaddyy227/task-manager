-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.4.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for task_manager
CREATE DATABASE IF NOT EXISTS `task_manager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `task_manager`;

-- Dumping structure for table task_manager.comment
CREATE TABLE IF NOT EXISTS `comment` (
  `id` varchar(36) NOT NULL,
  `content` text NOT NULL,
  `createdDate` datetime DEFAULT NULL,
  `updatedDate` datetime DEFAULT NULL,
  `task_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.comment: ~2 rows (approximately)
INSERT INTO `comment` (`id`, `content`, `createdDate`, `updatedDate`, `task_id`) VALUES
	('33c60eba-cf26-4625-8096-5267c1fdad53', 'This is a comment.TEST#3', '2024-06-28 03:17:36', '2024-06-28 03:17:36', 'b04d19cc-4ec7-4c75-8ab0-b376e39acbc3');
INSERT INTO `comment` (`id`, `content`, `createdDate`, `updatedDate`, `task_id`) VALUES
	('64bb36f4-3fbf-464b-9913-6d537e357819', 'This is a comment.TEST#2', '2024-06-28 03:17:32', '2024-06-28 03:17:32', 'b04d19cc-4ec7-4c75-8ab0-b376e39acbc3');

-- Dumping structure for table task_manager.subtask
CREATE TABLE IF NOT EXISTS `subtask` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `responsible` varchar(255) DEFAULT NULL,
  `parent_task_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_task_id` (`parent_task_id`),
  CONSTRAINT `subtask_ibfk_1` FOREIGN KEY (`parent_task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.subtask: ~3 rows (approximately)
INSERT INTO `subtask` (`id`, `title`, `description`, `dueDate`, `responsible`, `parent_task_id`) VALUES
	('1cfd3df9-321f-4787-97c8-23274c2d9112', 'New Subtask 12', 'Subtask Description', '2024-06-28', 'Jane Doe', '3b563914-9dd5-4ff5-90e2-827f6cf032d4');
INSERT INTO `subtask` (`id`, `title`, `description`, `dueDate`, `responsible`, `parent_task_id`) VALUES
	('38ce2532-4c76-4d02-bbc8-d5a7e603aa13', 'SubTask Title1', 'SubTask Description', '2024-06-26', 'Jane Doe', 'b04d19cc-4ec7-4c75-8ab0-b376e39acbc3');
INSERT INTO `subtask` (`id`, `title`, `description`, `dueDate`, `responsible`, `parent_task_id`) VALUES
	('9aab58c0-6a68-498f-862f-6cb65052a135', 'New Subtask', 'Subtask Description', '2024-06-28', 'Jane Doe', '3b563914-9dd5-4ff5-90e2-827f6cf032d4');

-- Dumping structure for table task_manager.task
CREATE TABLE IF NOT EXISTS `task` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `responsible` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.task: ~5 rows (approximately)
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
	('3b563914-9dd5-4ff5-90e2-827f6cf032d4', 'New Task', 'Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
	('4146fcf2-8388-43bb-a61b-4f3f1de8f622', 'New Task 123', 'Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
	('9eb70367-0858-4c4d-a11f-989b1826049f', 'Task Title 12', 'Task Description', '2024-06-26', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
	('b04d19cc-4ec7-4c75-8ab0-b376e39acbc3', 'Updated Task Title', 'Updated Task Description', '2024-06-26', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
	('d45d0650-b5d0-4d4f-81c2-d91d9299f7f2', 'Task Title 1234', 'Task Description', '2024-06-26', 'John Doe');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
