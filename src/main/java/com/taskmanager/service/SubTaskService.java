package com.taskmanager.service;

import com.taskmanager.dto.SubtaskDTO;
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
    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubTaskMapper subTaskMapper;

    public List<SubtaskDTO> getAllSubTasks() {
        return subTaskRepository.findAll().stream()
                .map(subTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<SubtaskDTO> getSubTaskById(Long id) {
        return subTaskRepository.findById(id).map(subTaskMapper::toDto);
    }

    public Optional<SubtaskDTO> createSubTask(Long taskId, SubtaskDTO subTaskDTO) {
        return taskRepository.findById(taskId).map(task -> {
            SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
            subTask.setParentTask(task);
            return subTaskMapper.toDto(subTaskRepository.save(subTask));
        });
    }

    public Optional<SubtaskDTO> updateSubTask(Long id, SubtaskDTO subTaskDTO) {
        return subTaskRepository.findById(id).map(existingSubTask -> {
            SubTask subTask = subTaskMapper.toEntity(subTaskDTO);
            subTask.setId(existingSubTask.getId());
            subTask.setParentTask(existingSubTask.getParentTask());
            return subTaskMapper.toDto(subTaskRepository.save(subTask));
        });
    }

    public boolean deleteSubTask(Long id) {
        return subTaskRepository.findById(id).map(subTask -> {
            subTaskRepository.delete(subTask);
            return true;
        }).orElse(false);
    }
}
