package org.example.example.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private static Path path;
    private UserRepository userRepository;

    public UserService() {
    }

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void setPath(Path path) {
        UserService.path = Path.of(path.toString() + "/");
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UUID create(UserRequestDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .recipes(new ArrayList<>())
                .build();
        validate(userRepository.create(user));
        return user.getId();
    }

    public UUID delete(UUID id) {
        validate(userRepository.delete(id));
        return id;
    }

    public UUID update(UUID uuid, UserRequestDTO dto) {
        Optional<User> existingUserOpt = userRepository.findById(uuid);
        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User existingUser = existingUserOpt.get();
        User user = User.builder()
                .id(uuid)
                .name(dto.getName())
                .email(dto.getEmail())
                .recipes(existingUser.getRecipes())
                .avatar(existingUser.getAvatar())
                .build();
        validate(userRepository.update(uuid, user));
        return uuid;
    }


    public Path getAvatar(UUID id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(IOException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        Path path = Paths.get(UserService.path.toString() + id.toString() + ".png");
        userRepository.setAvatar(id, path);
        Files.copy(inputStream, path);
        return id;
    }

    public UUID deleteAvatar(UUID id) {
        return userRepository.deleteAvatar(id);
    }

    private void validate(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Operation failed");
        }
    }
}
