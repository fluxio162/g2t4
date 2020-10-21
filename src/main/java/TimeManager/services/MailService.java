package TimeManager.services;

import TimeManager.model.User;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service for sending mails.
 * Source: https://www.baeldung.com/java-email
 */
@Component
@Scope("application")
public class MailService {

    private String host = "smtp.gmail.com";
    private String userName = "timemanager.system@gmail.com";
    private String password = "timeManager2020";
    private String port = "587";

    private static final Logger logger = Logger.getLogger(MailService.class);

    /**
     * Sends mail.
     *
     * @param recipient   the recipient
     * @param subject     the subject
     * @param messageText the message text
     */
    public void sendMail(User recipient, String subject, String messageText) {

        String from = "TimeManagerSystem";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.user", userName);
        properties.put("mai.smtp.password", password);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mai.smtp.debug", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail()));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);

            logger.info("Message with subject " + subject + " sent successfully to user " + recipient.getUsername() + ".");

        } catch (MessagingException e) {
            logger.trace(e);
        }
    }

    /**
     * Starts a separate thread for each mail.
     *
     * @param recipient the recipient
     * @param subject   the subject
     * @param message   the message
     */
    public void parallelMessaging(User recipient, String subject, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMail(recipient, subject, message);
            }
        }).start();
    }


}
