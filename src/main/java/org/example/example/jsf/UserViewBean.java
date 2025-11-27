package org.example.example.jsf;


import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import org.example.example.controller.UserController;
import org.example.example.persistance.dtos.UserResponseDTO;

@Named("userView")
@ViewScoped
@Getter
@Setter
public class UserViewBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UserController userService;

    private String id;
    private UserResponseDTO userDto;
    private boolean notFound = false;

    public void init() {
        if (id == null) return;
        UUID uuid = UUID.fromString(id);
        try {
            userDto = userService.findById(uuid);
        } catch (Exception e) {
            userDto = null;
        }
        notFound = (userDto == null);
        if (notFound) {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc != null) {
                try {
                    fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/404.xhtml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public UserResponseDTO getUserDto() {
        return userDto;
    }
}
