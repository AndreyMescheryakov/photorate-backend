package com.vsu.demo.controller;

import com.vsu.demo.entity.Rating;
import com.vsu.demo.request.CreateRatingRequest;
import com.vsu.demo.response.RatingAverage;
import com.vsu.demo.service.RatingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/photos/{photoId}/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public Rating rate(@PathVariable UUID photoId, @RequestBody CreateRatingRequest request, Authentication auth) {
        return ratingService.rate(photoId, request, auth);
    }

    @GetMapping("/average")
    public RatingAverage average(@PathVariable UUID photoId) {
        return ratingService.averageByPhoto(photoId);
    }
}
