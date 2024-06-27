package com.taskmanager.service;

import com.taskmanager.dto.CommentDTO;
import com.taskmanager.dto.CommentRequest;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.CommentMapper;
import com.taskmanager.model.Comment;
import com.taskmanager.model.Task;
import com.taskmanager.repository.CommentRepository;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public List<CommentDTO> getCommentsByTaskId(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        return commentRepository.findByTaskId(taskId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String addCommentToTask(String taskId, CommentRequest commentRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(commentRequest.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(comment);
        task.getComments().add(comment);
        taskRepository.save(task);
        return "Comment added!";
    }

    public Optional<CommentDTO> updateComment(String commentId, CommentDTO commentDTO) {
        return Optional.ofNullable(commentRepository.findById(commentId).map(existingComment -> {
            existingComment.setContent(commentDTO.getContent());
            existingComment.setUpdatedDate(LocalDateTime.now());
            return commentMapper.toDto(commentRepository.save(existingComment));
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId)));
    }

    public String deleteComment(String commentId) {
        return commentRepository.findById(commentId).map(comment -> {
            commentRepository.delete(comment);
            return "Deleted comment with id: " + commentId;
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
    }
}
