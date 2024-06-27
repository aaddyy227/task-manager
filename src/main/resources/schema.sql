CREATE DATABASE IF NOT EXISTS task_manager;

USE task_manager;

CREATE TABLE IF NOT EXISTS Task (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    dueDate DATE,
                                    responsible VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS SubTask (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       dueDate DATE,
                                       responsible VARCHAR(255),
                                       parent_task_id BIGINT,
                                       FOREIGN KEY (parent_task_id) REFERENCES Task(id)
);

CREATE TABLE IF NOT EXISTS Comment (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       content TEXT NOT NULL,
                                       createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       task_id BIGINT,
                                       FOREIGN KEY (task_id) REFERENCES Task(id)
);
