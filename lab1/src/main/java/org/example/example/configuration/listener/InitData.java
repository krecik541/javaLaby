package org.example.example.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

@WebListener
public class InitData implements ServletContextListener {

    private UserRepository repository;
    private Path avatarDir;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        repository = UserRepository.getInstance();
        String avatarParam = event.getServletContext().getInitParameter("avatarDir");
        avatarDir = Path.of(event.getServletContext().getRealPath("/"), avatarParam);

        initData();
    }

    private void initData() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .name("Adam Smith")
                .email("adamsmith@gmail.com")
                .recipes(new ArrayList<>())
                .avatar(readAvatar("guest.png"))
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

    private byte[] readAvatar(String fileName) {
        try {
            Path avatarPath = avatarDir.resolve(fileName);
            if (Files.exists(avatarPath)) {
                return Files.readAllBytes(avatarPath);
            } else {
                System.err.println("[WARN] Avatar file not found: " + avatarPath);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading avatar " + fileName, e);
        }
    }
}
