package TimeManager.ui.beans;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * Listener for logging failed login attempts.
 */
@Component
public class FailedLoginListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger logger = Logger.getLogger(FailedLoginListener.class);

    /**
     * Listens to Spring Event "AuthenticationFailureBadCredentialsEvent" and logs username to audit log.
     *
     * @param event the failed login event
     */
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        logger.info("FAILED LOGIN from User: " + event.getAuthentication().getName());
    }
}
