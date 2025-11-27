package org.example.example.jsf;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Named;
import java.util.Locale;

@Named("localeBean")
@RequestScoped
public class LocaleBean {

    public void init(ComponentSystemEvent event) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        UIViewRoot vr = ctx.getViewRoot();
        if (vr == null) return;

        // Determine preferred locale from request headers
        try {
            Locale req = ctx.getExternalContext().getRequestLocale();
            if (req != null) {
                String lang = req.getLanguage();
                if (lang != null && lang.startsWith("pl")) {
                    vr.setLocale(new Locale("pl"));
                } else {
                    vr.setLocale(new Locale("en"));
                }
            }
        } catch (Exception ignored) {
        }
    }

    public String getLogoName() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return "logo.png";
        UIViewRoot vr = ctx.getViewRoot();
        if (vr == null || vr.getLocale() == null) return "logo.png";
        String lang = vr.getLocale().getLanguage();
        if (lang != null && lang.startsWith("pl")) return "logo-pl.png";
        return "logo-en.png";
    }
}
