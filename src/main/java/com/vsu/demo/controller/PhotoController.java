package com.vsu.demo.controller;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.request.CreatePhotoRequest;
import com.vsu.demo.request.UpdatePhotoRequest;
import com.vsu.demo.service.PhotoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping
    public List<Photo> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return photoService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public Photo findById(@PathVariable UUID id) {
        return photoService.findById(id);
    }

    @GetMapping("/search")
    public List<Photo> findByTitle(@RequestParam String title) {
        return photoService.findByTitle(title);
    }

    @GetMapping("/search_by_user")
    public List<Photo> findByUserId(@RequestParam UUID userId) {
        return photoService.findByUserId(userId);
    }

    @PostMapping
    public Photo create(@RequestBody CreatePhotoRequest request, Authentication auth) {
        return photoService.create(request, auth);
    }

    @PutMapping("/{id}")
    public Photo update(@PathVariable UUID id, @RequestBody UpdatePhotoRequest request, Authentication auth) {
        return photoService.update(id, request, auth);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, Authentication auth) {
        photoService.deleteById(id, auth);
    }
}
