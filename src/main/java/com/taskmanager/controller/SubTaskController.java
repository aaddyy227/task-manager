package com.taskmanager.controller;


import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.service.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtasks")
public class SubTaskController {
    @Autowired
    private SubTaskService subTaskService;

    @GetMapping
    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskService.getAllSubTasks();
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<SubtaskDTO> createSubTask(@PathVariable Long taskId, @RequestBody SubtaskDTO subTaskDTO) {
        return subTaskService.createSubTask(taskId, subTaskDTO).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubtaskDTO> getSubTaskById(@PathVariable Long id) {
        return subTaskService.getSubTaskById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubtaskDTO> updateSubTask(@PathVariable Long id, @RequestBody SubtaskDTO subTaskDTO) {
        return subTaskService.updateSubTask(id, subTaskDTO).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id) {
        return subTaskService.deleteSubTask(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
