package org.example.example.configuration.listener;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.CategoryType;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class InitData {

    @Inject
    private ServletContext context;

    @Inject
    private UserRepository repository;
    @Inject
    private CategoryRepository category;
    @Inject
    private RecipeService recipe;

    private Path dir;

    @Inject
    private RequestContextController requestContextController;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        boolean activated = requestContextController.activate();
        try {
            String avatarParam = context.getInitParameter("dir");
            dir = Path.of(context.getRealPath("/"), avatarParam);
            UserService.setPath(Path.of(dir + "/avatars/"));
            initData();
        } finally {
            if (activated) requestContextController.deactivate();
        }
    }

    private void initData() {
        Category category1 = Category.builder()
                .id(UUID.randomUUID())
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

        User user1 = User.builder()
                .id(UUID.randomUUID())
                .name("Adam Smith")
                .email("adamsmith@gmail.com")
                .recipes(new ArrayList<>())
                .avatar(Path.of(dir + "/gordon.png"))
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
                .author(user4.getId())
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
