package com.taskmanager.service;

import com.taskmanager.dto.SubTaskRequest;
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

    /**
     * Get all subtasks.
     */
    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskRepository.findAll().stream()
                .map(subTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a subtask by its ID.
     */
    public SubtaskDTO getSubTaskById(String id) {
        return subTaskRepository.findById(id)
                .map(subTaskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }

    /**
     * Update an existing subtask by ID.
     */
    public Optional<SubtaskDTO> updateSubTask(String id, SubtaskDTO subTaskDTO) {
        return Optional.ofNullable(subTaskRepository.findById(id).map(existingSubTask -> {
            // Map the DTO to an entity and update its ID to the existing subtask's ID
            SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
            subTask.setId(existingSubTask.getId());
            subTask.setParentTask(existingSubTask.getParentTask());
            return subTaskMapper.toDto(subTaskRepository.save(subTask));
        }).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id)));
    }

    /**
     * Delete a subtask by its ID.
     */
    public String deleteSubTask(String id) {
        return subTaskRepository.findById(id).map(subTask -> {
            subTaskRepository.delete(subTask);
            return "Deleted subtask with id: " + id;
        }).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }
}
