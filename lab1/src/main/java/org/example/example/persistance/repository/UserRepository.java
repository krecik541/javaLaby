package org.example.example.persistance.repository;

import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserCreatedRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        System.out.println(user);
        return users.put(id, user) != null ? id : null;
    }

    @Override
    public UUID delete(UUID id) {
        return users.remove(id) != null ? id : null;
    }

    @Override
    public UUID update(UUID id, User user) {
        return users.put(id, user) != null ? id : null;
    }



    public byte[] getAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getAvatar();
    }

    public UUID setAvatar(UUID id, InputStream inputStream) throws IOException {
        System.out.println(0);
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        System.out.println(1);
        user.setAvatar(inputStream.readAllBytes());
        System.out.println(2);
        return id;
    }

    public UUID deleteAvatar(UUID id) {
        User user = findById(id).orElseThrow(IllegalArgumentException::new);
        user.setAvatar(null);
        return id;
    }

}
