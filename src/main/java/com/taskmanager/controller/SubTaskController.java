package com.taskmanager.controller;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.dto.TaskDTO;
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
    private final TaskService taskService;

    @Autowired
    public SubTaskController(SubTaskService subTaskService, TaskService taskService) {
        this.subTaskService = subTaskService;
        this.taskService = taskService;
    }

    /**
     * Get all subtasks.
     */
    @GetMapping("/all")
    public ResponseEntity<List<SubtaskDTO>> getAllSubTasks() {
        List<SubtaskDTO> subtasks = subTaskService.getAllSubTasks();
        return ResponseEntity.ok(subtasks);
    }

    /**
     * Add a subtask to a specific task.
     */
    @PostMapping("/{taskId}")
    public ResponseEntity<TaskDTO> addSubTask(@PathVariable String taskId, @RequestBody SubTaskRequest subTaskRequest) {
        TaskDTO createdSubTask = taskService.addSubtask(taskId, subTaskRequest);
        return ResponseEntity.ok(createdSubTask);
    }

    /**
     * Get details of a specific subtask by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubtaskDTO> getSubTaskById(@PathVariable String id) {
        SubtaskDTO subtaskDTO = subTaskService.getSubTaskById(id);
        return ResponseEntity.ok(subtaskDTO);
    }

    /**
     * Update an existing subtask by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubtaskDTO> updateSubTask(@PathVariable String id, @RequestBody SubtaskDTO subtaskDTO) {
        SubtaskDTO updatedSubTask = subTaskService.updateSubTask(id, subtaskDTO);
        return ResponseEntity.ok(updatedSubTask);
    }

    /**
     * Delete a subtask by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubTask(@PathVariable String id) {
        String response = subTaskService.deleteSubTask(id);
        return ResponseEntity.ok(response);
    }
}
