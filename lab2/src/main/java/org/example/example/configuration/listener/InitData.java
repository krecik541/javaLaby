package org.example.example.configuration.listener;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

@ApplicationScoped
public class InitData {

    @Inject
    private ServletContext context;

    @Inject
    private UserRepository repository;

    private Path dir;

    @Inject
    private RequestContextController requestContextController;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        boolean activated = requestContextController.activate();
        try {
            String avatarParam = context.getInitParameter("dir");
            dir = Path.of(context.getRealPath("/"), avatarParam);
            initData();
        } finally {
            if (activated) requestContextController.deactivate();
        }
    }

    private void initData() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .name("Adam Smith")
                .email("adamsmith@gmail.com")
                .recipes(new ArrayList<>())
                .avatar(readAvatar("gordon.png"))
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
            Path avatarPath = dir.resolve(fileName);
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
