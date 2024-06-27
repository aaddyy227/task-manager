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

    /**
     * Get all comments for a specific task by task ID.
     */
    public List<CommentDTO> getCommentsByTaskId(String taskId) {
        Optional<List<Comment>> comment = commentRepository.findByTaskId(taskId);
        if(comment.isPresent()){
        return comment.get().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());}
        else throw new ResourceNotFoundException("No comments for task with id: "+taskId);
    }

    /**
     * Add a comment to a specific task.
     */
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

    /**
     * Update an existing comment by comment ID.
     */
    public Optional<CommentDTO> updateComment(String commentId, CommentDTO commentDTO) {
        return Optional.ofNullable(commentRepository.findById(commentId).map(existingComment -> {
            existingComment.setContent(commentDTO.getContent());
            existingComment.setUpdatedDate(LocalDateTime.now());
            return commentMapper.toDto(commentRepository.save(existingComment));
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId)));
    }

    /**
     * Delete a comment by comment ID.
     */
    public String deleteComment(String commentId) {
        return commentRepository.findById(commentId).map(comment -> {
            commentRepository.delete(comment);
            return "Deleted comment with id: " + commentId;
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
    }
}
