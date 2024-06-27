package com.taskmanager.controller;

import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.service.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtasks")
public class SubTaskController {
    private final SubTaskService subTaskService;

    @Autowired
    public SubTaskController(SubTaskService subTaskService) {
        this.subTaskService = subTaskService;
    }

    @GetMapping
    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskService.getAllSubTasks();
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<SubtaskDTO> createSubTask(@PathVariable Long taskId, @RequestBody SubtaskDTO subtaskDTO) {
        try {
            SubtaskDTO createdSubTask = subTaskService.createSubTask(taskId, subtaskDTO).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
            return ResponseEntity.ok(createdSubTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubtaskDTO> getSubTaskById(@PathVariable Long id) {
        try {
            SubtaskDTO subtaskDTO = subTaskService.getSubTaskById(id);
            return ResponseEntity.ok(subtaskDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubtaskDTO> updateSubTask(@PathVariable Long id, @RequestBody SubtaskDTO subtaskDTO) {
        try {
            SubtaskDTO updatedSubTask = subTaskService.updateSubTask(id, subtaskDTO).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
            return ResponseEntity.ok(updatedSubTask);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubTask(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subTaskService.deleteSubTask(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
