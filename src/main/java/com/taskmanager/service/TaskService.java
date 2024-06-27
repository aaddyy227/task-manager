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

    public List<TaskDTO> getAllTasks(String title, String responsible) {
        List<Task> tasks;
        if (title != null && responsible != null) {
            tasks = taskRepository.findByTitleAndResponsible(title, responsible)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with title " + title + " and responsible " + responsible));
        } else if (title != null) {
            tasks = taskRepository.findAllByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with title " + title));
        } else if (responsible != null) {
            tasks = taskRepository.findByResponsible(responsible)
                    .orElseThrow(() -> new ResourceNotFoundException("Tasks not found with responsible " + responsible));
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<Task> existingTask = taskRepository.findByTitle(taskDTO.getTitle());
        if (existingTask.isPresent()) {
            throw new IllegalArgumentException("Task with title " + taskDTO.getTitle() + " already exists.");
        }

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

            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setResponsible(task.getResponsible());
            existingTask.setSubTasks(task.getSubTasks());

            return taskMapper.toDto(taskRepository.save(existingTask));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id)));
    }

    public String deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.delete(task);
            return "Deleted task with id: " + id;
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }
}
