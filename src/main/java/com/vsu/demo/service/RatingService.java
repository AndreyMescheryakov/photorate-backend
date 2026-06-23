package com.vsu.demo.service;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.Rating;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.RatingRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreateRatingRequest;
import com.vsu.demo.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, PhotoRepository photoRepository,
                         UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Rating rate(UUID photoId, CreateRatingRequest request, Authentication auth) {
        if (request.score() == null || request.score() < 1 || request.score() > 10) {
            throw new ValidationException("Score must be between 1 and 10");
        }
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ValidationException(ErrorCode.PHOTO_NOT_FOUND));

        if (photo.getUserId().equals(user.getId())) {
            throw new ValidationException(ErrorCode.SELF_RATING);
        }
        if (ratingRepository.existsByUserAndPhoto(user.getId(), photoId)) {
            throw new ValidationException(ErrorCode.ALREADY_RATED);
        }

        Rating rating = new Rating(UUID.randomUUID(), request.score(), LocalDate.now(), user.getId(), photoId);
        ratingRepository.create(rating);
        return rating;
    }

    public Double averageScore(UUID photoId) {
        if (photoRepository.findById(photoId).isEmpty()) {
            throw new ValidationException(ErrorCode.PHOTO_NOT_FOUND);
        }
        Double avg = ratingRepository.averageByPhoto(photoId);
        return avg == null ? 0.0 : avg;
    }
}
