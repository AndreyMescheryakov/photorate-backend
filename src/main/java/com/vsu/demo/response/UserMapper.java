package com.vsu.demo.response;

import com.vsu.demo.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
