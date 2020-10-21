package TimeManager.ui.beans;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Listener for logging successful logout attempts.
 */
@Component
public class LogoutListener implements ApplicationListener<LogoutSuccessEvent> {

    private static final Logger logger = Logger.getLogger(LogoutListener.class);

    /**
     * Listens to Spring Event "LogoutSuccessEvent" and logs username to audit log.
     *
     * @param event the successful logout event
     */
    public void onApplicationEvent(LogoutSuccessEvent event) {
        logger.info("LOGOUT from User: " + event.getAuthentication().getName());
    }
}
