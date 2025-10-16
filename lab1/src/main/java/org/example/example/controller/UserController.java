package org.example.example.controller;

import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.dtos.UserResponseDTO;
import org.example.example.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public UserResponseDTO findById(UUID id) {
        return userService.findById(id)
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .hasAvatar(user.getAvatar() != null)
                        .build())
                .orElseThrow();
    }

    public List<UserResponseDTO> findAll() {
        return userService.findAll()
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .hasAvatar(user.getAvatar() != null)
                        .build())
                .collect(Collectors.toList());
    }

    public UUID create(UserRequestDTO user) {
        return userService.create(user);
    }

    public UUID delete(UUID id) {
        return userService.delete(id);
    }

    public UUID update(UUID id, UserRequestDTO user) {
        return userService.update(id, user);
    }



    public byte[] getAvatar(UUID id) {
        return userService.getAvatar(id);
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        return userService.setAvatar(id, inputStream);
    }

    public UUID deleteAvatar(UUID id) {
        return userService.deleteAvatar(id);
    }
}
