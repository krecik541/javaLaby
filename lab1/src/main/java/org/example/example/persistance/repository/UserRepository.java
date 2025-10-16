package org.example.example.persistance.repository;

import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserCreatedRequest;

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
        System.out.println(users);
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
}
