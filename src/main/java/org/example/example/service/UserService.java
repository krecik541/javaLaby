package org.example.example.service;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class UserService {

    private UserRepository userRepository;

    @Resource(name = "avatarDir")
    private String avatarDir;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

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

    public UUID create(UserRequestDTO dto, String role) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .login(dto.getLogin())
                .email(dto.getEmail())
                .password(passwordHash.generate(dto.getPassword().toCharArray()))
                .recipes(new ArrayList<>())
                .roles(List.of(role))
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
                .name(dto.getName() != null ? dto.getName() : existingUser.getName())
                .login(dto.getLogin() != null ? dto.getLogin() : existingUser.getLogin())
                .email(dto.getEmail() != null ? dto.getEmail() : existingUser.getEmail())
                .password(existingUser.getPassword())
                .recipes(existingUser.getRecipes())
                .avatar(existingUser.getAvatar())
                .roles(existingUser.getRoles())
                .build();
        validate(userRepository.update(uuid, user));
        return uuid;
    }


    public Path getAvatar(UUID id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(IOException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        Path path = Paths.get(avatarDir + id.toString() + ".png");
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

    public Optional<User> findByLogin(String s) {
        return userRepository.findByLogin(s);
    }
}
