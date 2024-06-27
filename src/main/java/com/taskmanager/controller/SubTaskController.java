package com.taskmanager.controller;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.dto.TaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.SubTaskService;
import com.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtasks")
public class SubTaskController {
    private final SubTaskService subTaskService;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public SubTaskController(SubTaskService subTaskService, TaskRepository taskRepository, TaskService taskService) {
        this.subTaskService = subTaskService;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    /**
     * Get all subtasks.
     */
    @GetMapping("/all")
    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskService.getAllSubTasks();
    }

    /**
     * Add a subtask to a specific task.
     */
    @PostMapping("/{taskId}")
    public ResponseEntity<TaskDTO> addSubTask(@PathVariable String taskId, @RequestBody SubTaskRequest subTaskRequest) {
        try {
            TaskDTO createdSubTask = taskService.addSubtask(taskId, subTaskRequest);
            return ResponseEntity.ok(createdSubTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get details of a specific subtask by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubtaskDTO> getSubTaskById(@PathVariable String id) {
        try {
            SubtaskDTO subtaskDTO = subTaskService.getSubTaskById(id);
            return ResponseEntity.ok(subtaskDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing subtask by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubtaskDTO> updateSubTask(@PathVariable String id, @RequestBody SubtaskDTO subtaskDTO) {
        try {
            SubtaskDTO updatedSubTask = subTaskService.updateSubTask(id, subtaskDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
            return ResponseEntity.ok(updatedSubTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a subtask by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubTask(@PathVariable String id) {
        try {
            return ResponseEntity.ok(subTaskService.deleteSubTask(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
