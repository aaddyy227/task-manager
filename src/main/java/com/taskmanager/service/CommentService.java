package com.taskmanager.service;

import com.taskmanager.dto.CommentDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.CommentMapper;
import com.taskmanager.repository.CommentRepository;
import com.taskmanager.model.Comment;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDTO addCommentToTask(Long taskId, CommentDTO commentDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setTask(task);
        comment.setCreatedDate(new java.util.Date());
        return commentMapper.toDto(commentRepository.save(comment));
    }
}
