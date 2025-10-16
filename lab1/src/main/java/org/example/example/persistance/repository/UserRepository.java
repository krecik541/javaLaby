package org.example.example.persistance.repository;

import org.example.example.persistance.domain.User;
import java.io.IOException;
import java.util.*;

public class UserRepository implements Repository<User, UUID> {

    private static UserRepository instance;
    private final Map<UUID, User> users;

    private UserRepository() {
        users = new HashMap<>();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
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



    public byte[] getAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, byte[] file) throws IOException {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(file);
        return id;
    }

    public UUID deleteAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(null);
        return id;
    }

}
