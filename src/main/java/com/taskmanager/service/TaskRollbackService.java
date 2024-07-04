package com.taskmanager.service;

import com.taskmanager.dto.TaskRollbackRequest;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskHistory;
import com.taskmanager.repository.TaskHistoryRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskRollbackService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    @Autowired
    public TaskRollbackService(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    @Transactional
    public Task rollbackTask(TaskRollbackRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + request.getTaskId()));

        TaskHistory taskHistory = taskHistoryRepository.findById(request.getHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Task history not found with id " + request.getHistoryId()));

        task.setTitle(taskHistory.getTitle());
        task.setDescription(taskHistory.getDescription());
        task.setDueDate(taskHistory.getDueDate());
        task.setResponsible(taskHistory.getResponsible());

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<TaskHistory> getAllTaskHistory(String taskId) {
        return taskHistoryRepository.findAllByTaskId(taskId);
    }
}
