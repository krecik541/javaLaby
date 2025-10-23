package org.example.example.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private UserRepository userRepository;

    public UserService() {}

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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


    public byte[] getAvatar(UUID id) {
        return userRepository.getAvatar(id);
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        return userRepository.setAvatar(id, inputStream.readAllBytes());
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
