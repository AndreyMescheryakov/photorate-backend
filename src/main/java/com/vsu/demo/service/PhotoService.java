package com.vsu.demo.service;

import com.vsu.demo.entity.Photo;
import com.vsu.demo.entity.PhotoFile;
import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.PhotoFileRepository;
import com.vsu.demo.repository.PhotoRepository;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreatePhotoRequest;
import com.vsu.demo.request.UpdatePhotoRequest;
import com.vsu.demo.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoFileRepository photoFileRepository;
    private final UserRepository userRepository;

    public PhotoService(PhotoRepository photoRepository, PhotoFileRepository photoFileRepository,
                        UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.photoFileRepository = photoFileRepository;
        this.userRepository = userRepository;
    }

    public List<Photo> findAll(int page, int size) {
        return photoRepository.findAll(page, size);
    }

    public Photo findById(UUID id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new ValidationException(ErrorCode.PHOTO_NOT_FOUND));
    }

    public List<Photo> findByTitle(String title) {
        return photoRepository.findByTitle(title);
    }

    public List<Photo> findByUserId(UUID userId) {
        return photoRepository.findByUserId(userId);
    }

    @Transactional
    public Photo create(CreatePhotoRequest request, Authentication auth) {
        User user = currentUser(auth);
        if (request.title() == null || request.title().isBlank()) {
            throw new ValidationException("Title must not be empty");
        }

        UUID photoFileId = null;
        if (request.fileName() != null) {
            PhotoFile file = new PhotoFile(
                    UUID.randomUUID(),
                    request.fileName(),
                    request.fileContent(),
                    request.fileExtension(),
                    request.fileSizeKb(),
                    LocalDate.now()
            );
            photoFileRepository.create(file);
            photoFileId = file.getId();
        }

        Photo photo = new Photo(
                UUID.randomUUID(),
                request.title(),
                request.description(),
                request.category(),
                LocalDate.now(),
                user.getId(),
                photoFileId,
                0
        );
        if (!photoRepository.create(photo)) {
            throw new ValidationException("Photo creation failed");
        }
        return photo;
    }

    @Transactional
    public Photo update(UUID id, UpdatePhotoRequest request, Authentication auth) {
        User user = currentUser(auth);
        Photo photo = findById(id);
        if (!photo.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ValidationException(ErrorCode.NOT_OWNER);
        }

        photo.setTitle(request.title());
        photo.setDescription(request.description());
        photo.setCategory(request.category());
        photo.setVersion(request.version());

        if (!photoRepository.update(photo)) {
            throw new ValidationException("Photo was modified by another user");
        }
        return findById(id);
    }

    @Transactional
    public void deleteById(UUID id, Authentication auth) {
        User user = currentUser(auth);
        Photo photo = findById(id);
        if (!photo.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ValidationException(ErrorCode.NOT_OWNER);
        }
        photoRepository.deleteById(id);
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
    }
}
