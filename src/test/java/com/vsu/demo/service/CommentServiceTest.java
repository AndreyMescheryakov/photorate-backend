package com.vsu.demo.service;

import com.vsu.demo.entity.Comment;
import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.CommentRepository;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreateCommentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication auth;

    @InjectMocks
    private CommentService commentService;

    private User author;
    private UUID photoId;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = new User(UUID.randomUUID(), "author@mail.ru", "author", "hash", Role.USER, LocalDate.now());
        photoId = UUID.randomUUID();
        comment = new Comment(1, "nice", LocalDate.now(), author.getId(), photoId);
    }

    @Test
    void create_shouldThrow_whenPhotoNotFound() {
        when(auth.getName()).thenReturn(author.getEmail());
        when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> commentService.create(photoId, new CreateCommentRequest("nice"), auth));
    }

    @Test
    void delete_shouldThrow_whenUserIsNotAuthor() {
        User stranger = new User(UUID.randomUUID(), "other@mail.ru", "other", "hash", Role.USER, LocalDate.now());
        when(auth.getName()).thenReturn(stranger.getEmail());
        when(userRepository.findByEmail(stranger.getEmail())).thenReturn(Optional.of(stranger));
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        assertThrows(ValidationException.class, () -> commentService.delete(1, auth));
    }

    @Test
    void delete_shouldRemoveComment_whenUserIsAdmin() {
        User admin = new User(UUID.randomUUID(), "admin@mail.ru", "admin", "hash", Role.ADMIN, LocalDate.now());
        when(auth.getName()).thenReturn(admin.getEmail());
        when(userRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        commentService.delete(1, auth);

        verify(commentRepository).deleteById(1);
    }

    @Test
    void delete_shouldRemoveComment_whenUserIsAuthor() {
        when(auth.getName()).thenReturn(author.getEmail());
        when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        commentService.delete(1, auth);

        verify(commentRepository).deleteById(1);
    }
}
