package org.example.example.persistance.repository;

import jakarta.inject.Singleton;
import org.example.example.persistance.domain.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class UserRepository implements Repository<User, UUID> {

    private final Map<UUID, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }

    @Override
    public Optional<User> findById(UUID id) {
        User user = users.get(id);
        if (user != null)
            return Optional.of(user);
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    @Override
    public UUID create(User user) {
        UUID id = UUID.randomUUID();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public UUID delete(UUID id) {
        return users.remove(id) != null ? id : null;
    }

    @Override
    public UUID update(UUID id, User user) {
        if (users.containsKey(id)) {
            users.put(id, user);
            return id;
        }
        return null;
    }


    public Path getAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, Path path) throws IOException {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(path);
        return id;
    }

    public UUID deleteAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(null);
        return id;
    }

}
