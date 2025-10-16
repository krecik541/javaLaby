package org.example.example.service;

import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserCreatedRequest;
import org.example.example.persistance.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private static UserService instance;
    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UUID create(UserCreatedRequest dto) {
        User user = User.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .build();
        validate(userRepository.create(user));
        System.out.println(user.getId());
        return user.getId();
    }

    public UUID delete(UUID id) {
        validate(userRepository.delete(id));
        return id;
    }

    // TODO: potencjanie usuwanie reciept
    public UUID update(UUID uuid, UserCreatedRequest dto) {
        User user = User.builder()
                .id(uuid)
                .name(dto.getName())
                .email(dto.getEmail())
                .recipes(null)
                .build();
        validate(userRepository.update(uuid, user));
        return uuid;
    }

    private void validate(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
    }
}
