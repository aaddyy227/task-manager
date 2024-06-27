package com.taskmanager.service;

import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubTaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.repository.SubTaskRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final SubTaskMapper subTaskMapper;

    @Autowired
    public SubTaskService(SubTaskRepository subTaskRepository, TaskRepository taskRepository, SubTaskMapper subTaskMapper) {
        this.subTaskRepository = subTaskRepository;
        this.taskRepository = taskRepository;
        this.subTaskMapper = subTaskMapper;
    }

    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskRepository.findAll().stream()
                .map(subTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubtaskDTO getSubTaskById(Long id) {
        return subTaskRepository.findById(id)
                .map(subTaskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }

    public Optional<SubtaskDTO> createSubTask(Long taskId, SubtaskDTO subTaskDTO) {
        return Optional.ofNullable(taskRepository.findById(taskId).map(task -> {
            SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
            subTask.setParentTask(task);
            return subTaskMapper.toDto(subTaskRepository.save(subTask));
        }).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId)));
    }

    public Optional<SubtaskDTO> updateSubTask(Long id, SubtaskDTO subTaskDTO) {
        return Optional.ofNullable(subTaskRepository.findById(id).map(existingSubTask -> {
            SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
            subTask.setId(existingSubTask.getId());
            subTask.setParentTask(existingSubTask.getParentTask());
            return subTaskMapper.toDto(subTaskRepository.save(subTask));
        }).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id)));
    }

    public String deleteSubTask(Long id) {
        return subTaskRepository.findById(id).map(subTask -> {
            subTaskRepository.delete(subTask);
            return "Deleted subtask with id:" +id;
        }).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }
}
