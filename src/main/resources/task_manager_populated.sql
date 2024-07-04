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

-- Dumping data for table task_manager.comment: ~0 rows (approximately)
INSERT INTO `comment` (`id`, `content`, `createdDate`, `updatedDate`, `task_id`) VALUES
    ('d6559de4-a507-415a-81d3-e47604347c53', 'New Comment', '2024-07-04 17:44:03', '2024-07-04 17:44:03', '858cd548-2437-4677-ae87-d4b27a16b0a8');

-- Dumping structure for table task_manager.subtask
CREATE TABLE IF NOT EXISTS `subtask` (
                                         `id` varchar(36) NOT NULL,
                                         `parent_task_id` varchar(36) DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `parent_task_id` (`parent_task_id`),
                                         CONSTRAINT `subtask_ibfk_1` FOREIGN KEY (`parent_task_id`) REFERENCES `task` (`id`),
                                         CONSTRAINT `subtask_ibfk_2` FOREIGN KEY (`id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.subtask: ~0 rows (approximately)
INSERT INTO `subtask` (`id`, `parent_task_id`) VALUES
    ('1e2d0f33-8731-4daf-acc0-31b1462f39ea', '858cd548-2437-4677-ae87-d4b27a16b0a8');

-- Dumping structure for table task_manager.task
CREATE TABLE IF NOT EXISTS `task` (
                                      `id` varchar(36) NOT NULL,
                                      `title` varchar(255) NOT NULL,
                                      `description` text DEFAULT NULL,
                                      `dueDate` date DEFAULT NULL,
                                      `responsible` varchar(255) DEFAULT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.task: ~0 rows (approximately)
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('1e2d0f33-8731-4daf-acc0-31b1462f39ea', 'New Subtask 12', 'Subtask Description', '2024-06-28', 'Jane Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('28520418-f92c-4717-8665-ffe75b9ffa43', 'New Task 123', 'Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('858cd548-2437-4677-ae87-d4b27a16b0a8', 'Updated Task Title123', 'Updated Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('922ca550-f9e6-406e-ae81-828d76172dca', 'New Task 1245s3', 'Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('d113e489-2843-4c20-841e-00b5d69f0170', 'Updated Task Title123', 'Updated Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('d22518ef-931a-4319-a5ef-1f585a8432cc', 'New Task 1245s3', 'Task Description', '2024-06-28', 'John Doe');
INSERT INTO `task` (`id`, `title`, `description`, `dueDate`, `responsible`) VALUES
    ('ec199e08-72c5-4dd4-82c0-3d0dfbb37d98', 'New Task 123', 'Task Description', '2024-06-28', 'John Doe');

-- Dumping structure for table task_manager.taskhistory
CREATE TABLE IF NOT EXISTS `taskhistory` (
                                             `id` varchar(36) NOT NULL,
                                             `modifiedDate` datetime DEFAULT NULL,
                                             `task_id` varchar(36) DEFAULT NULL,
                                             PRIMARY KEY (`id`),
                                             KEY `task_id` (`task_id`),
                                             CONSTRAINT `taskhistory_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table task_manager.taskhistory: ~0 rows (approximately)
INSERT INTO `taskhistory` (`id`, `modifiedDate`, `task_id`) VALUES
    ('28520418-f92c-4717-8665-ffe75b9ffa43', '2024-07-04 18:09:06', 'ec199e08-72c5-4dd4-82c0-3d0dfbb37d98');
INSERT INTO `taskhistory` (`id`, `modifiedDate`, `task_id`) VALUES
    ('922ca550-f9e6-406e-ae81-828d76172dca', '2024-07-04 17:43:18', '858cd548-2437-4677-ae87-d4b27a16b0a8');
INSERT INTO `taskhistory` (`id`, `modifiedDate`, `task_id`) VALUES
    ('d113e489-2843-4c20-841e-00b5d69f0170', '2024-07-04 17:44:15', '858cd548-2437-4677-ae87-d4b27a16b0a8');
INSERT INTO `taskhistory` (`id`, `modifiedDate`, `task_id`) VALUES
    ('d22518ef-931a-4319-a5ef-1f585a8432cc', '2024-07-04 17:42:09', '858cd548-2437-4677-ae87-d4b27a16b0a8');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
