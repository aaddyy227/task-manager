package com.taskmanager.service;

import com.taskmanager.dto.SubTaskRequest;
import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.exception.GlobalExceptionHandler;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubTaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
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
     * Retrieves all subtasks.
     *
     * @return A list of SubtaskDTOs representing all subtasks.
     */
    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskRepository.findAll().stream()
                .map(subTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a subtask by its ID.
     *
     * @param id The ID of the subtask to retrieve.
     * @return The SubtaskDTO corresponding to the given ID.
     * @throws ResourceNotFoundException if the subtask is not found.
     */
    public SubtaskDTO getSubTaskById(String id) {
        return subTaskRepository.findById(id)
                .map(subTaskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }





    /**
     * Updates an existing subtask.
     *
     * @param id          The ID of the subtask to update.
     * @param subTaskDTO  The data transfer object representing the updated subtask.
     * @return An Optional containing the updated SubtaskDTO if the update was successful.
     * @throws ResourceNotFoundException if the subtask is not found.
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
     * Deletes a subtask by its ID.
     *
     * @param id The ID of the subtask to delete.
     * @return A message indicating the subtask was deleted.
     * @throws ResourceNotFoundException if the subtask is not found.
     */
    public String deleteSubTask(String id) {
        return subTaskRepository.findById(id).map(subTask -> {
            subTaskRepository.delete(subTask);
            return "Deleted subtask with id: " + id;
        }).orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));
    }
}
