package com.vsu.demo.service;

import com.vsu.demo.entity.Role;
import com.vsu.demo.entity.User;
import com.vsu.demo.exception.ValidationException;
import com.vsu.demo.repository.UserRepository;
import com.vsu.demo.request.CreateUserRequest;
import com.vsu.demo.response.ErrorCode;
import com.vsu.demo.response.UserMapper;
import com.vsu.demo.response.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        userRepository.lockOnValue(request.email());
        Optional<User> maybeUser = userRepository.findByEmail(request.email());
        if (maybeUser.isPresent()) {
            throw new ValidationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User newUser = new User(
                UUID.randomUUID(),
                request.email(),
                request.nickname(),
                passwordEncoder.encode(request.password()),
                Role.USER,
                LocalDate.now()
        );
        if (!userRepository.create(newUser)) {
            throw new ValidationException("User creation failed");
        }
        return UserMapper.toResponse(newUser);
    }

    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
