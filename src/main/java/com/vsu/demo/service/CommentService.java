package com.vsu.demo.service;

import com.vsu.demo.entity.Comment;
import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.CommentRepository;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreateCommentRequest;
import com.vsu.demo.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PhotoRepository photoRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    public List<Comment> findByPhoto(UUID photoId) {
        return commentRepository.findByPhoto(photoId);
    }

    @Transactional
    public Comment create(UUID photoId, CreateCommentRequest request, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
        if (photoRepository.findById(photoId).isEmpty()) {
            throw new ValidationException(ErrorCode.PHOTO_NOT_FOUND);
        }
        Comment comment = new Comment(null, request.text(), LocalDate.now(), user.getId(), photoId);
        return commentRepository.create(comment);
    }

    @Transactional
    public void delete(Integer id, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Comment not found"));
        if (!comment.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ValidationException(ErrorCode.NOT_OWNER);
        }
        commentRepository.deleteById(id);
    }
}
