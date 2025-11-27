package org.example.example.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.ServletException;
import lombok.Getter;

import java.io.Serializable;

@Named("securityView")
@ViewScoped
@Getter
public class SecurityView implements Serializable {

    @Inject
    private SecurityContext securityContext;

    // No direct injection of HttpServletRequest here — use FacesContext when needed

    public String getCurrentUserName() {
        SecurityContext sc = getSecurityContext();
        if (sc != null && sc.getCallerPrincipal() != null) {
            return sc.getCallerPrincipal().getName();
        }
        return null;
    }

    public boolean isUserLoggedIn() {
        SecurityContext sc = getSecurityContext();
        return sc != null && sc.getCallerPrincipal() != null;
    }

    public boolean isUserInRole(String role) {
        SecurityContext sc = getSecurityContext();
        return sc != null && sc.isCallerInRole(role);
    }

    public String logout() {
        try {
            var req = (jakarta.servlet.http.HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            req.logout();
            return "/index.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            throw new RuntimeException("Logout failed", e);
        }
    }

    private SecurityContext getSecurityContext() {
        if (securityContext != null) {
            return securityContext;
        }
        // Fallback to programmatic lookup
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null && ctx.getExternalContext() != null) {
                // nothing else to do here - injection should provide the securityContext
                return securityContext;
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public boolean isAdmin() {
        return isUserInRole("ADMIN");
    }
}
