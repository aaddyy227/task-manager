package com.taskmanager.repository;


import com.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    Optional<List<Task>> findByTitleAndResponsible(String title, String responsible);

    Optional<Task> findByTitle(String title);

    Optional<List<Task>> findAllByTitle(String title);

    Optional<List<Task>> findByResponsible(String responsible);
}
