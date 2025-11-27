package org.example.example.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.domain.CategoryType;

import java.io.Serializable;
import java.util.UUID;

@Named("categoryFormView")
@ViewScoped
@Getter
@Setter
public class CategoryFormView implements Serializable {

    @Inject
    private CategoryController categoryController;

    private String id;
    private String name;
    private CategoryType type;

    public void init() {
        if (id == null || id.isBlank()) return;
        try {
            UUID uuid = UUID.fromString(id);
            CategoryResponseDTO dto = categoryController.findById(uuid);
            if (dto != null) {
                this.name = dto.getName();
                this.type = dto.getType();
            }
        } catch (Exception ignored) {}
    }

    public String save() {
        try {
            CategoryRequestDTO req = new CategoryRequestDTO();
            req.setName(name);
            req.setType(type);

            if (id != null && !id.isBlank()) {
                UUID uuid = UUID.fromString(id);
                categoryController.update(uuid, req);
            } else {
                categoryController.create(req);
            }
            return "/categories/categories.xhtml?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public CategoryType[] getTypeOptions() {
        return CategoryType.values();
    }
}
