package com.taskmanager.controller;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Get all tasks, optionally filtered by title and/or responsible.
     */
    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String responsible) {
        List<TaskDTO> tasks = taskService.getAllTasks(title, responsible);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Create a new task.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
        try {
            TaskDTO createdTask = taskService.createTask(taskRequest);
            return ResponseEntity.ok(createdTask);
        } catch (DuplicateTaskException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Get details of a specific task by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskDetails(@PathVariable String id) {
        try {
            TaskDTO taskDTO = taskService.getTaskById(id);
            return ResponseEntity.ok(taskDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing task by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
        try {
            TaskDTO updatedTask = taskService.updateTask(id, taskDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
            return ResponseEntity.ok(updatedTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a task by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        try {
            return ResponseEntity.ok(taskService.deleteTask(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add a subtask to an existing task.
     */
    @PostMapping("/{taskId}/subtasks")
    public ResponseEntity<TaskDTO> addSubTask(@PathVariable String taskId, @RequestBody SubTaskRequest subTaskRequest) {
        try {
            TaskDTO subTask = taskService.addSubtask(taskId, subTaskRequest);
            return ResponseEntity.ok(subTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
