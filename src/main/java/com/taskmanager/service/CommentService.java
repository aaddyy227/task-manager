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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    /**
     * Retrieves all comments for a specific task by task ID.
     *
     * @param taskId The ID of the task to retrieve comments for.
     * @return A list of CommentDTOs representing all comments for the task.
     * @throws ResourceNotFoundException if no comments are found for the given task ID.
     */
    public List<CommentDTO> getCommentsByTaskId(String taskId) {
        return commentRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No comments for task with id: " + taskId))
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds a comment to a specific task.
     *
     * @param taskId          The ID of the task to add the comment to.
     * @param commentRequest  The data transfer object representing the new comment.
     * @return A message indicating the comment was added.
     * @throws ResourceNotFoundException if the task is not found.
     */
    @Transactional
    public String addCommentToTask(String taskId, CommentRequest commentRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(commentRequest.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(comment);
        return "Comment added!";
    }

    /**
     * Updates an existing comment by comment ID.
     *
     * @param commentId  The ID of the comment to update.
     * @param commentDTO The data transfer object representing the updated comment.
     * @return The updated CommentDTO.
     * @throws ResourceNotFoundException if the comment is not found.
     */
    @Transactional
    public CommentDTO updateComment(String commentId, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
        existingComment.setContent(commentDTO.getContent());
        existingComment.setUpdatedDate(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(existingComment));
    }

    /**
     * Deletes a comment by comment ID.
     *
     * @param commentId The ID of the comment to delete.
     * @return A message indicating the comment was deleted.
     * @throws ResourceNotFoundException if the comment is not found.
     */
    @Transactional
    public String deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
        commentRepository.delete(comment);
        return "Deleted comment with id: " + commentId;
    }
}
