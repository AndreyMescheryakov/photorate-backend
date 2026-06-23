package com.vsu.demo.service;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.PhotoFileRepository;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreatePhotoRequest;
import com.vsu.demo.request.UpdatePhotoRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private PhotoFileRepository photoFileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication auth;

    @InjectMocks
    private PhotoService photoService;

    private User owner;
    private UUID photoId;
    private Photo photo;

    @BeforeEach
    void setUp() {
        owner = new User(UUID.randomUUID(), "owner@mail.ru", "owner", "hash", Role.USER, LocalDate.now());
        photoId = UUID.randomUUID();
        photo = new Photo(photoId, "Sunset", "beach", "nature", LocalDate.now(), owner.getId(), null, 0);
    }

    @Test
    void create_shouldSavePhoto_whenRequestIsValid() {
        when(auth.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(photoRepository.create(any())).thenReturn(true);

        CreatePhotoRequest request = new CreatePhotoRequest("Sunset", "beach", "nature", null, null, null, null);
        Photo result = photoService.create(request, auth);

        assertEquals("Sunset", result.getTitle());
        assertEquals(owner.getId(), result.getUserId());
        verify(photoRepository).create(any());
    }

    @Test
    void create_shouldThrow_whenTitleIsBlank() {
        when(auth.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));

        CreatePhotoRequest request = new CreatePhotoRequest("  ", "beach", "nature", null, null, null, null);
        assertThrows(ValidationException.class, () -> photoService.create(request, auth));
    }

    @Test
    void findById_shouldThrow_whenPhotoNotFound() {
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> photoService.findById(photoId));
    }

    @Test
    void update_shouldThrow_whenUserIsNotOwner() {
        User stranger = new User(UUID.randomUUID(), "other@mail.ru", "other", "hash", Role.USER, LocalDate.now());
        when(auth.getName()).thenReturn(stranger.getEmail());
        when(userRepository.findByEmail(stranger.getEmail())).thenReturn(Optional.of(stranger));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        UpdatePhotoRequest request = new UpdatePhotoRequest("new", "new", "art", 0);
        assertThrows(ValidationException.class, () -> photoService.update(photoId, request, auth));
    }

    @Test
    void update_shouldThrow_whenVersionConflict() {
        when(auth.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.update(any())).thenReturn(false);

        UpdatePhotoRequest request = new UpdatePhotoRequest("new", "new", "art", 0);
        assertThrows(ValidationException.class, () -> photoService.update(photoId, request, auth));
    }

    @Test
    void delete_shouldRemovePhoto_whenUserIsOwner() {
        when(auth.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        photoService.deleteById(photoId, auth);

        verify(photoRepository).deleteById(photoId);
    }

    @Test
    void delete_shouldThrow_whenUserIsNotOwner() {
        User stranger = new User(UUID.randomUUID(), "other@mail.ru", "other", "hash", Role.USER, LocalDate.now());
        when(auth.getName()).thenReturn(stranger.getEmail());
        when(userRepository.findByEmail(stranger.getEmail())).thenReturn(Optional.of(stranger));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        assertThrows(ValidationException.class, () -> photoService.deleteById(photoId, auth));
    }
}
