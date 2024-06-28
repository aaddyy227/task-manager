package com.taskmanager.controller;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskUpdateRequest;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Get all tasks, optionally filtered by title and responsible person
     */
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String responsible) {
        List<TaskDTO> tasks = taskService.getAllTasks(title, responsible);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Create a new task
     */
    @PostMapping("/add")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskRequest taskRequest) {
        TaskDTO createdTask = taskService.createTask(taskRequest);
        return ResponseEntity.ok(createdTask);
    }

    /**
     * Get details of a specific task by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskDetails(@PathVariable String id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    /**
     * Update an existing task by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String id, @RequestBody TaskUpdateRequest taskUpdateRequest) {
        TaskDTO updatedTask = taskService.updateTask(id, taskUpdateRequest);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }


}
