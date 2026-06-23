package com.vsu.demo.service;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.Rating;
import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.RatingRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreateRatingRequest;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication auth;

    @InjectMocks
    private RatingService ratingService;

    private User owner;
    private User rater;
    private UUID photoId;
    private Photo photo;

    @BeforeEach
    void setUp() {
        owner = new User(UUID.randomUUID(), "owner@mail.ru", "owner", "hash", Role.USER, LocalDate.now());
        rater = new User(UUID.randomUUID(), "rater@mail.ru", "rater", "hash", Role.USER, LocalDate.now());
        photoId = UUID.randomUUID();
        photo = new Photo(photoId, "Sunset", "beach", "nature", LocalDate.now(), owner.getId(), null, 0);
    }

    @Test
    void rate_shouldThrow_whenScoreOutOfRange() {
        CreateRatingRequest request = new CreateRatingRequest(11);
        assertThrows(ValidationException.class, () -> ratingService.rate(photoId, request, auth));
    }

    @Test
    void rate_shouldThrow_whenRatingOwnPhoto() {
        when(auth.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        CreateRatingRequest request = new CreateRatingRequest(8);
        assertThrows(ValidationException.class, () -> ratingService.rate(photoId, request, auth));
    }

    @Test
    void rate_shouldThrow_whenAlreadyRated() {
        when(auth.getName()).thenReturn(rater.getEmail());
        when(userRepository.findByEmail(rater.getEmail())).thenReturn(Optional.of(rater));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(ratingRepository.existsByUserAndPhoto(rater.getId(), photoId)).thenReturn(true);

        CreateRatingRequest request = new CreateRatingRequest(8);
        assertThrows(ValidationException.class, () -> ratingService.rate(photoId, request, auth));
    }

    @Test
    void rate_shouldSaveRating_whenValid() {
        when(auth.getName()).thenReturn(rater.getEmail());
        when(userRepository.findByEmail(rater.getEmail())).thenReturn(Optional.of(rater));
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(ratingRepository.existsByUserAndPhoto(rater.getId(), photoId)).thenReturn(false);
        when(ratingRepository.create(any())).thenReturn(true);

        CreateRatingRequest request = new CreateRatingRequest(8);
        Rating result = ratingService.rate(photoId, request, auth);

        assertEquals(8, result.getScore());
        assertEquals(rater.getId(), result.getUserId());
    }
}
