package TimeManager.ui.controllers;

import TimeManager.model.NotificationCategories;
import TimeManager.configs.WebSecurityConfig;
import TimeManager.model.User;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Controller for the user settings view.
 */
@Component
@Scope("view")
public class UserSettingsController {
    @Autowired
    private UserService userService;

    private Collection<String> notificationCategories = new ArrayList<>();

    /**
     * Instantiates a new controller.
     */
    public UserSettingsController(){}

    /**
     * Instantiates a new controller.
     *
     * @param userService the user service
     */
    public UserSettingsController(UserService userService){
        this.userService = userService;
    }

    /**
     * Initializes the notification categories collection.
     */
    @PostConstruct
    public void init(){
        notificationCategories.add(NotificationCategories.NEVER.toString());
        notificationCategories.add(NotificationCategories.WEEKLY.toString());
        notificationCategories.add(NotificationCategories.MONTHLY.toString());
        notificationCategories.add(NotificationCategories.YEARLY.toString());
    }

    /**
     * Get notification categories.
     *
     * @return the notification categories
     */
    public Collection<String> getNotificationCategories() {
        return notificationCategories;
    }

    /**
     * Set notification categories.
     *
     * @param notificationCategories the notification categories
     */
    public void setNotificationCategories(Collection<String> notificationCategories) {
        this.notificationCategories = notificationCategories;
    }

    /**
     * Saves user.
     *
     * @param user the user
     */
    public void saveUser(User user){
        this.userService.settingsSaveUser(user);
    }

    /**
     * Saves user with new password and encodes the password before saving it in the database.
     *
     * @param user the user
     */
    public void saveNewPassword (User user) {
        FacesContext context = FacesContext.getCurrentInstance();
        user.setPassword(WebSecurityConfig.passwordEncoder().encode(user.getPassword()));
        this.userService.settingsSaveUser(user);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg: Passwort wurde geändert.", null));
    }
}
