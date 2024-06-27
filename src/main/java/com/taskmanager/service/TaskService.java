package com.taskmanager.service;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

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

            for (SubTask subTask : task.getSubTasks()) {
                subTask.setParentTask(task);
            }

            List<Long> subTaskIds = task.getSubTasks().stream()
                    .map(SubTask::getId)
                    .toList();
            existingTask.getSubTasks().removeIf(subTask -> !subTaskIds.contains(subTask.getId()));

            return taskMapper.toDto(taskRepository.save(task));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id)));
    }

    public String deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.delete(task);
            return "Deleted task with id:" + id;
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }
}
