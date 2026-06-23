package com.vsu.demo.response;

import com.vsu.demo.entity.Role;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(UUID id, String email, String nickname, Role role, LocalDate createdAt) {
}
