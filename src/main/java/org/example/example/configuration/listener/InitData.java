package org.example.example.configuration.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
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
import org.example.example.persistance.repository.RecipeRepository;

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
@NoArgsConstructor
public class InitData {

    @Inject
    private ServletContext context;

    @Inject
    private UserRepository repository;
    @Inject
    private CategoryRepository category;
    @Inject
    private RecipeRepository recipeRepository;

//    @Resource(name = "avatarDir")
//    private String avatarDir;

    @Resource(name = "dir")
    private String avatarInitDir;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @PostConstruct
    @PermitAll
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

        Recipe r1 = Recipe.builder().title(recipe1.getTitle()).description(recipe1.getDescription()).preparationTime(recipe1.getPreparationTime()).author(user1).category(category1).build();
        Recipe r2 = Recipe.builder().title(recipe2.getTitle()).description(recipe2.getDescription()).preparationTime(recipe2.getPreparationTime()).author(user2).category(category1).build();
        Recipe r3 = Recipe.builder().title(recipe3.getTitle()).description(recipe3.getDescription()).preparationTime(recipe3.getPreparationTime()).author(user1).category(category1).build();
        Recipe r4 = Recipe.builder().title(recipe4.getTitle()).description(recipe4.getDescription()).preparationTime(recipe4.getPreparationTime()).author(user1).category(category2).build();
        Recipe r5 = Recipe.builder().title(recipe5.getTitle()).description(recipe5.getDescription()).preparationTime(recipe5.getPreparationTime()).author(user3).category(category2).build();
        Recipe r6 = Recipe.builder().title(recipe6.getTitle()).description(recipe6.getDescription()).preparationTime(recipe6.getPreparationTime()).author(user1).category(category2).build();
        Recipe r7 = Recipe.builder().title(recipe7.getTitle()).description(recipe7.getDescription()).preparationTime(recipe7.getPreparationTime()).author(user2).category(category1).build();

        recipeRepository.create(r1);
        recipeRepository.create(r2);
        recipeRepository.create(r3);
        recipeRepository.create(r4);
        recipeRepository.create(r5);
        recipeRepository.create(r6);
        recipeRepository.create(r7);
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
