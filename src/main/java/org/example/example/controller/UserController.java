package org.example.example.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.dtos.UserResponseDTO;
import org.example.example.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserController {

    private UserService userService;

    public UserController() {
    }

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserResponseDTO findById(UUID id) {
        return userService.findById(id)
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .hasAvatar(user.getAvatar() != null)
                        .recipes(user.getRecipes())
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
                        .recipes(user.getRecipes())
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


    public Path getAvatar(UUID id) throws IOException {
        return userService.getAvatar(id);
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        return userService.setAvatar(id, inputStream);
    }

    public UUID deleteAvatar(UUID id) {
        return userService.deleteAvatar(id);
    }
}
