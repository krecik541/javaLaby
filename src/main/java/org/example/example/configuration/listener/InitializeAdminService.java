package org.example.example.configuration.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.repository.UserRepository;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * EJB singleton can be forced to start automatically when application starts. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@NoArgsConstructor(force = true)
public class InitializeAdminService {

    private final UserRepository userRepository;

    private final Pbkdf2PasswordHash passwordHash;

    @Resource(name = "avatarDir")
    private String avatarDir;

    @Resource(name = "avatarInitDir")
    private String avatarInitDir;

    @Inject
    public InitializeAdminService(
            UserRepository userRepository,
            @SuppressWarnings("CdiInjectionPointsInspection") Pbkdf2PasswordHash passwordHash
    ) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @PostConstruct
    @SneakyThrows
    private void init() {
        System.out.println(passwordHash.generate("admin".toCharArray()));
        System.out.println(passwordHash.generate("user".toCharArray()));
        if (userRepository.findByLogin("admin-service").isEmpty()) {
            System.out.println(passwordHash);
            System.out.println(2);
            User admin = User.builder()
                    .id(UUID.fromString("11111111-1111-1111-1111-000000000000"))
                    .login("admin-service")
                    .name("Admin")
                    .email("admin@gmail.com")
                    .password(passwordHash.generate("admin".toCharArray()))
                    .roles(List.of(Role.ADMIN, Role.USER))
                    .build();

            userRepository.create(admin);
        }
    }

}
