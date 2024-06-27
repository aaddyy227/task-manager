package com.taskmanager.controller;

import com.taskmanager.dto.CommentDTO;
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

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    @PostMapping("/task/{taskId}")
    public ResponseEntity<CommentDTO> addCommentToTask(@PathVariable Long taskId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.addCommentToTask(taskId, commentDTO);
            return ResponseEntity.ok(createdComment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
