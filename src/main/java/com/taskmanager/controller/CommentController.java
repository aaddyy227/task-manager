package com.taskmanager.controller;

import com.taskmanager.dto.CommentDTO;
import com.taskmanager.dto.CommentRequest;
import com.taskmanager.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Add a comment to a specific task by task ID.
     */
    @PostMapping("/task/add/{taskId}")
    public ResponseEntity<String> addCommentToTask(@PathVariable String taskId, @RequestBody CommentRequest commentRequest) {
        String response = commentService.addCommentToTask(taskId, commentRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing comment by comment ID.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable String commentId, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * Delete a comment by comment ID.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId) {
        String message = commentService.deleteComment(commentId);
        return ResponseEntity.ok(message);
    }
}
