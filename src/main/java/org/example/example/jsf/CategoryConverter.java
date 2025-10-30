package org.example.example.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.example.example.controller.CategoryController;

import java.util.UUID;

@FacesConverter(value = "categoryConverter", managed = true)
public class CategoryConverter implements Converter {

    @Inject
    private CategoryController categoryController;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isBlank()) return null;
        try {
            UUID uuid = UUID.fromString(s);
            return categoryController.findById(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        if (o instanceof UUID uuid) {
            return uuid.toString();
        }
        return o.toString();
    }
}