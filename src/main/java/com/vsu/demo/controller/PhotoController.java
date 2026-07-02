package com.vsu.demo.controller;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.PhotoFile;
import com.vsu.demo.request.CreatePhotoRequest;
import com.vsu.demo.request.UpdatePhotoRequest;
import com.vsu.demo.service.PhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
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

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> file(@PathVariable UUID id) {
        PhotoFile f = photoService.getFile(id);
        byte[] bytes = Base64.getDecoder().decode(f.getFileContent());
        return ResponseEntity.ok().contentType(mediaType(f.getFileExtension())).body(bytes);
    }

    private MediaType mediaType(String extension) {
        if (extension == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        return switch (extension.toLowerCase()) {
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
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
