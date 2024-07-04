package com.taskmanager.service;

import com.taskmanager.dto.SubtaskDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubTaskMapper;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.TaskHistory;
import com.taskmanager.repository.SubTaskRepository;
import com.taskmanager.repository.TaskHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper subTaskMapper;
    private final TaskHistoryRepository taskHistoryRepository;

    @Autowired
    public SubTaskService(SubTaskRepository subTaskRepository, SubTaskMapper subTaskMapper, TaskHistoryRepository taskHistoryRepository) {
        this.subTaskRepository = subTaskRepository;
        this.subTaskMapper = subTaskMapper;
        this.taskHistoryRepository = taskHistoryRepository;
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
     * Updates an existing subtask by ID.
     *
     * @param id         The ID of the subtask to update.
     * @param subTaskDTO The data transfer object representing the updated subtask.
     * @return The updated SubtaskDTO.
     * @throws ResourceNotFoundException if the subtask is not found.
     */
    @Transactional
    public SubtaskDTO updateSubTask(String id, SubtaskDTO subTaskDTO) {
        SubTask existingSubTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));

        // Save current state to history before making changes
        saveSubTaskHistory(existingSubTask);

        SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
        subTask.setId(existingSubTask.getId());
        subTask.setParentTask(existingSubTask.getParentTask());

        return subTaskMapper.toDto(subTaskRepository.save(subTask));
    }

    /**
     * Deletes a subtask by its ID.
     *
     * @param id The ID of the subtask to delete.
     * @return A message indicating the subtask was deleted.
     * @throws ResourceNotFoundException if the subtask is not found.
     */
    @Transactional
    public String deleteSubTask(String id) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id " + id));

        // Save current state to history before deletion
        saveSubTaskHistory(subTask);

        subTaskRepository.delete(subTask);
        return "Deleted subtask with id: " + id;
    }

    /**
     * Saves the current state of the given subtask to the history within a transactional context.
     *
     * @param subTask The subtask whose state is to be saved.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSubTaskHistory(SubTask subTask) {
        TaskHistory subTaskHistory = new TaskHistory();
        subTaskHistory.setTask(subTask);
        subTaskHistory.setTitle(subTask.getTitle());
        subTaskHistory.setDescription(subTask.getDescription());
        subTaskHistory.setDueDate(subTask.getDueDate());
        subTaskHistory.setResponsible(subTask.getResponsible());
        subTaskHistory.setModifiedDate(LocalDateTime.now());
        taskHistoryRepository.save(subTaskHistory);
    }
}
