package TimeManager.ui.beans;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Listener for logging successful login attempts.
 */
@Component
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger logger = Logger.getLogger(LoginListener.class);

    /**
     * Listens to Spring Event "AuthenticationSuccessEvent" and logs username to audit log.
     *
     * @param event the successful login event
     */
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        logger.info("SUCCESSFUL LOGIN from User: " + event.getAuthentication().getName());
    }
}
