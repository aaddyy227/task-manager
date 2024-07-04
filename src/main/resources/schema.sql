CREATE DATABASE IF NOT EXISTS task_manager;

USE task_manager;

CREATE TABLE IF NOT EXISTS Task (
                                    id VARCHAR(36) NOT NULL PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    dueDate DATE,
                                    responsible VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS SubTask (
                                       id VARCHAR(36) NOT NULL PRIMARY KEY,
                                       parent_task_id VARCHAR(36),
                                       FOREIGN KEY (parent_task_id) REFERENCES Task(id),
                                       FOREIGN KEY (id) REFERENCES Task(id)
);

CREATE TABLE IF NOT EXISTS Comment (
                                       id VARCHAR(36) NOT NULL PRIMARY KEY,
                                       content TEXT NOT NULL,
                                       createdDate DATETIME,
                                       updatedDate DATETIME,
                                       task_id VARCHAR(36),
                                       FOREIGN KEY (task_id) REFERENCES Task(id)
);

CREATE TABLE IF NOT EXISTS TaskHistory (
                                           id VARCHAR(36) NOT NULL PRIMARY KEY,
                                           modifiedDate DATETIME,
                                           task_id VARCHAR(36),
                                           FOREIGN KEY (task_id) REFERENCES Task(id)
);
