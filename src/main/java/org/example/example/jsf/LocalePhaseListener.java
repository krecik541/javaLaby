package org.example.example.jsf;

import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import java.io.Serializable;
import java.util.Locale;

public class LocalePhaseListener implements PhaseListener, Serializable {

    @Override
    public void beforePhase(PhaseEvent event) {
        // no-op
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext ctx = event.getFacesContext();
        if (ctx == null) return;
        UIViewRoot vr = ctx.getViewRoot();
        if (vr == null) return;

        try {
            Locale req = ctx.getExternalContext().getRequestLocale();
            if (req != null && req.getLanguage() != null && req.getLanguage().startsWith("pl")) {
                vr.setLocale(new Locale("pl"));
            } else {
                vr.setLocale(Locale.ENGLISH);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
