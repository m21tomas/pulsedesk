package com.example.pulsedesk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsedesk.dto.CommentRequest;
import com.example.pulsedesk.dto.CommentResponse;
import com.example.pulsedesk.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
	private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
    	return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commentService.submitComment(request.getText()));
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public CommentResponse getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        
        return ResponseEntity.noContent().build();
    }
}
