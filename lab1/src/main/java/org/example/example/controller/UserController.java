package org.example.example.controller;

import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserCreatedRequest;
import org.example.example.service.UserService;

import java.util.List;
import java.util.UUID;

public class UserController {

    private static UserController instance;
    private final UserService userService;

    private UserController() {
        userService = UserService.getInstance();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public User findById(UUID id) {
        return userService.findById(id).
                orElseThrow();
    }

    public List<User> findAll() {
        return userService.findAll();
    }

    public UUID create(UserCreatedRequest user) {
        return userService.create(user);
    }

    public UUID delete(UUID id) {
        return userService.delete(id);
    }

    public UUID update(UUID id, UserCreatedRequest user) {
        return userService.update(id, user);
    }
}
