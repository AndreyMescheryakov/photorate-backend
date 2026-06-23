package com.vsu.demo.controller;

import com.vsu.demo.request.CreateUserRequest;
import com.vsu.demo.response.UserResponse;
import com.vsu.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
