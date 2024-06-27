package com.taskmanager.controller;

import com.taskmanager.dto.CommentDTO;
import com.taskmanager.dto.CommentRequest;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Get all comments for a specific task by task ID.
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable String taskId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
            return ResponseEntity.ok(comments);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add a comment to a specific task by task ID.
     */
    @PostMapping("/task/add/{taskId}")
    public ResponseEntity<String> addCommentToTask(@PathVariable String taskId, @RequestBody CommentRequest commentRequest) {
        try {
            return ResponseEntity.ok(commentService.addCommentToTask(taskId, commentRequest));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an existing comment by comment ID.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable String commentId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
            return ResponseEntity.ok(updatedComment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a comment by comment ID.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId) {
        try {
            String message = commentService.deleteComment(commentId);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
