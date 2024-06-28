package com.taskmanager.repository;

import com.taskmanager.model.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, String> {
    Optional<SubTask> findByTitleAndParentTaskId(String title, String parentTaskId);

}