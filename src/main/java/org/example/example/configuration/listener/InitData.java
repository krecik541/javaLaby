package org.example.example.configuration.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.servlet.ServletContext;
import lombok.NoArgsConstructor;
import org.example.example.persistance.domain.*;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.repository.CategoryRepository;
import org.example.example.persistance.repository.RecipeRepository;
import org.example.example.persistance.repository.UserRepository;
import org.example.example.service.CategoryService;
import org.example.example.service.RecipeService;
import org.example.example.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@DependsOn("InitializeAdminService")
@DeclareRoles({Role.ADMIN, Role.USER})
@RunAs(Role.ADMIN)
@NoArgsConstructor
public class InitData {

    @Inject
    private ServletContext context;

    @Inject
    private UserRepository repository;
    @Inject
    private CategoryRepository category;
    @EJB
    private RecipeService recipe;

//    @Resource(name = "avatarDir")
//    private String avatarDir;

    @Resource(name = "dir")
    private String avatarInitDir;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @PostConstruct
    private void initData() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .login("admin")
                .name("Admin ADMIN")
                .email("admin@gmail.com")
                .password(passwordHash.generate("admin".toCharArray()))
                .recipes(new ArrayList<>())
                .avatar(Path.of("/" + avatarInitDir + "/gordon.png"))
                .roles(List.of(Role.USER))
                .build();

        User user2 = User.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-000000000001"))
                .login("user")
                .name("User USER")
                .email("user@gmail.com")
                .password(passwordHash.generate("user".toCharArray()))
                .recipes(new ArrayList<>())
                .roles(List.of(Role.USER))
                .build();

        User user3 = User.builder()
                .id(UUID.randomUUID())
                .login("user1")
                .name("User USER")
                .email("user@gmail.com")
                .password(passwordHash.generate("user1".toCharArray()))
                .recipes(new ArrayList<>())
                .roles(List.of(Role.USER))
                .build();

        repository.create(user1);
        repository.create(user2);
        repository.create(user3);

        Category category1 = Category.builder()
                .id(UUID.fromString("22222222-2222-2222-2222-000000000001"))
                .name("Dania słone")
                .type(CategoryType.DINNER)
                .recipes(new ArrayList<>())
                .build();

        Category category2 = Category.builder()
                .id(UUID.randomUUID())
                .name("Dania słodki")
                .type(CategoryType.DESSERT)
                .recipes(new ArrayList<>())
                .build();

        category.create(category1);
        category.create(category2);

        RecipeRequestDTO recipe1 = RecipeRequestDTO.builder()
                .title("Makaron")
                .description("AABBCC")
                .preparationTime(10)
                .author(user1.getId())
                .category(category1.getId())
                .build();

        RecipeRequestDTO recipe2 = RecipeRequestDTO.builder()
                .title("Chleb")
                .description("AABBCC")
                .preparationTime(16)
                .author(user2.getId())
                .category(category1.getId())
                .build();

        RecipeRequestDTO recipe3 = RecipeRequestDTO.builder()
                .title("Ser")
                .description("AABBCC")
                .preparationTime(60)
                .author(user1.getId())
                .category(category1.getId())
                .build();

        RecipeRequestDTO recipe4 = RecipeRequestDTO.builder()
                .title("Lody")
                .description("AABBCC")
                .preparationTime(1)
                .author(user1.getId())
                .category(category2.getId())
                .build();

        RecipeRequestDTO recipe5 = RecipeRequestDTO.builder()
                .title("Ciasto")
                .description("AABBCC")
                .preparationTime(10)
                .author(user3.getId())
                .category(category2.getId())
                .build();

        RecipeRequestDTO recipe6 = RecipeRequestDTO.builder()
                .title("Ciastka")
                .description("AABBCC")
                .preparationTime(55)
                .author(user1.getId())
                .category(category2.getId())
                .build();

        RecipeRequestDTO recipe7 = RecipeRequestDTO.builder()
                .title("Mięso")
                .description("AABBCC")
                .preparationTime(35)
                .author(user2.getId())
                .category(category1.getId())
                .build();

        recipe.create(recipe1);
        recipe.create(recipe2);
        recipe.create(recipe3);
        recipe.create(recipe4);
        recipe.create(recipe5);
        recipe.create(recipe6);
        recipe.create(recipe7);
    }

//    private byte[] readAvatar(String fileName) {
//        try {
//            Path avatarPath = avatarDir.resolve(fileName);
//            if (Files.exists(avatarPath)) {
//                return Files.readAllBytes(avatarPath);
//            } else {
//                System.err.println("[WARN] Avatar file not found: " + avatarPath);
//                return null;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading avatar " + fileName, e);
//        }
//    }
}
