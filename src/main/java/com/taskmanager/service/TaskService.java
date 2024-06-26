package com.taskmanager.service;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        task.getSubTasks().forEach(subTask -> subTask.setParentTask(task));
        return taskMapper.toDto(taskRepository.save(task));
    }

    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        return Optional.ofNullable(taskRepository.findById(id).map(existingTask -> {
            Task task = taskMapper.toEntity(taskDTO);
            task.setId(existingTask.getId());
            task.getSubTasks().forEach(subTask -> subTask.setParentTask(task));
            return taskMapper.toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id)));
    }

    public boolean deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.delete(task);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }
}
