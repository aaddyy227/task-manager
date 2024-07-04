package com.taskmanager.controller;

import com.taskmanager.dto.TaskRollbackRequest;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskHistory;
import com.taskmanager.service.TaskRollbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskRollbackController {

    private final TaskRollbackService taskRollbackService;

    @Autowired
    public TaskRollbackController(TaskRollbackService taskRollbackService) {
        this.taskRollbackService = taskRollbackService;
    }

    @PostMapping("/rollback")
    public ResponseEntity<Task> rollbackTask(@RequestBody TaskRollbackRequest request) {
        Task rolledBackTask = taskRollbackService.rollbackTask(request);
        return ResponseEntity.ok(rolledBackTask);
    }

    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskHistory>> getAllTaskHistory(@PathVariable String taskId) {
        List<TaskHistory> taskHistories = taskRollbackService.getAllTaskHistory(taskId);
        return ResponseEntity.ok(taskHistories);
    }
}
