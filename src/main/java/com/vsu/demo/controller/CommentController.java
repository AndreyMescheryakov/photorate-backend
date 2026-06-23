package com.vsu.demo.controller;

import com.vsu.demo.entity.Comment;
import com.vsu.demo.request.CreateCommentRequest;
import com.vsu.demo.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/photos/{photoId}/comments")
    public List<Comment> findByPhoto(@PathVariable UUID photoId) {
        return commentService.findByPhoto(photoId);
    }

    @PostMapping("/photos/{photoId}/comments")
    public Comment create(@PathVariable UUID photoId, @RequestBody CreateCommentRequest request, Authentication auth) {
        return commentService.create(photoId, request, auth);
    }

    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable Integer id, Authentication auth) {
        commentService.delete(id, auth);
    }
}
