package org.example.example.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.repository.UserRepository;

import java.util.ArrayList;
import java.util.UUID;

public class InitData implements ServletContextListener {

    private UserRepository repository;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        repository = UserRepository.getInstance();

        initData();
    }

    private void initData() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .name("Adam Smith")
                .email("adamsmith@gmail.com")
                .recipes(new ArrayList<>())
                .build();
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .email("janedoe@gmail.com")
                .recipes(new ArrayList<>())
                .build();
        User user3 = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("johndoe@gmail.com")
                .recipes(new ArrayList<>())
                .build();
        User user4 = User.builder()
                .id(UUID.randomUUID())
                .name("ABC DEF")
                .email("abcdef@gmail.com")
                .recipes(new ArrayList<>())
                .build();

        repository.create(user1);
        repository.create(user2);
        repository.create(user3);
        repository.create(user4);
    }
}
