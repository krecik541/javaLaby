package org.example.example.jsf;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import org.example.example.service.UserService;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Named("authView")
public class AuthView {

    @EJB
    private UserService userService;

    public void checkAuth() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return;
        if (fc.getExternalContext().getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/authentication/login.xhtml");
        }
    }

    // Overloaded listener method for <f:event type="preRenderView" listener="#{authView.checkAuth}" />
    public void checkAuth(ComponentSystemEvent event) throws IOException {
        checkAuth();
    }

    public boolean isAdmin() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return false;
        return fc.getExternalContext().isUserInRole(Role.ADMIN);
    }

    public UUID getCurrentUserId() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return null;
        Principal p = fc.getExternalContext().getUserPrincipal();
        if (p == null) return null;
        java.util.Optional<User> u = userService.findByLogin(p.getName());
        if (u.isEmpty()) return null;
        return u.get().getId();
    }

    public String getCurrentUserName() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return null;
        Principal p = fc.getExternalContext().getUserPrincipal();
        if (p == null) return null;
        return p.getName();
    }

    public boolean isOwner(Object ownerId) {
        if (ownerId == null) return false;
        UUID owner;
        try {
            if (ownerId instanceof UUID) owner = (UUID) ownerId;
            else owner = UUID.fromString(ownerId.toString());
        } catch (Exception e) {
            return false;
        }
        UUID cur = getCurrentUserId();
        return cur != null && cur.equals(owner);
    }

    public void checkAdminAuth() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) return;
        if (fc.getExternalContext().getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/authentication/login.xhtml");
            return;
        }
        if (!isAdmin()) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/403.xhtml");
        }
    }
}

