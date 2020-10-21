package TimeManager.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Bean for displaying messages using Primefaces "growl" or "messages".
 */
@Component
@Scope("request")
public class MessageBean {

    /**
     * Sends message on invalid login.
     */
    public void showInvalidLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Der Benutzername oder das Kennwort ist falsch.", ""));
    }

    /**
     * Sends message on successful logout.
     */
    public void showLogout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sie wurden erfolgreich ausgeloggt.", ""));
    }
}
