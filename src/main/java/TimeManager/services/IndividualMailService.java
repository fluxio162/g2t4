package TimeManager.services;

import TimeManager.model.Badge;
import TimeManager.model.NotificationCategories;
import TimeManager.model.User;
import TimeManager.repositories.BadgeRepository;
import TimeManager.repositories.TaskRepository;
import TimeManager.repositories.UserRepository;
import TimeManager.ui.activitycontrollers.ActivityController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Service for sending individual mails in a given interval.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class IndividualMailService extends ActivityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    private MailService mailService = new MailService();
    private static final Logger logger = Logger.getLogger(IndividualMailService.class);

    /**
     * Generates and sends weekly mail notifications.
     */
    @Scheduled(cron="0 0 0 * * SUN")
    public void weeklyIndividualMailNotification() {
        List<User> weeklyUsers = userRepository.findAllWithNotificationInterval(NotificationCategories.WEEKLY.toString());
        Date today = new Date();
        Date aWeekAgo = new Date(today.getTime() - 7 * (1000 * 60 * 60 * 24));
        for (User user : weeklyUsers) {
            this.currentActivities = taskRepository.allTasksBetweenDatesFromUser(aWeekAgo, today, user);
            if (this.currentActivities.size() != 0) {
                String message = "Liebe/r Benutzer/in!\n\nNachstehend finden Sie Ihre Aktivitäten der letzten Woche:\n";
                this.generateHashMapPercent(currentActivities);
                for (HashMap.Entry<String, Float> entry : this.currentActivitiesPercentage.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();
                    message += "Aktivität \"" + key + "\": " + value + "%\n";
                }
                Collection<Badge> badges = badgeRepository.allBadgesBetweenDatesFromUser(aWeekAgo, today, user);
                if (badges.size() != 0) {
                    message += "\nNachstehend finden Sie die Badges, die Sie letzte Woche erhalten haben:\n";
                    for (Badge badge : badges) {
                        message += "Badge \"" + badge.getBadgeTitle() + "\": " + badge.getTimeSpentOnTask() + " Stunden\n";
                    }
                }
                message += "\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            else {
                String message = "Liebe/r Benutzer/in!\n\nEs konnten leider keine Aktivitäten für die letzte Woche gefunden werden. \n\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            logger.info("Weekly e-mail notifications sent.");
        }
    }

    /**
     * Generates and sends monthly mail notifications.
     */
    @Scheduled(cron="0 0 0 1 * *")
    public void monthlyIndividualMailNotification() {
        List<User> weeklyUsers = userRepository.findAllWithNotificationInterval(NotificationCategories.MONTHLY.toString());
        Date today = new Date();
        Date aMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        for (User user : weeklyUsers) {
            this.currentActivities = taskRepository.allTasksBetweenDatesFromUser(aMonthAgo, today, user);
            if (this.currentActivities.size() != 0) {
                String message = "Liebe/r Benutzer/in!\n\nNachstehend finden Sie Ihre Aktivitäten des vergangenen Monats:\n";
                this.generateHashMapPercent(currentActivities);
                for (HashMap.Entry<String, Float> entry : this.currentActivitiesPercentage.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();
                    message += "Aktivität \"" + key + "\": " + value + "%\n";
                }
                Collection<Badge> badges = badgeRepository.allBadgesBetweenDatesFromUser(aMonthAgo, today, user);
                if (badges.size() != 0) {
                    message += "\nNachstehend finden Sie alle Badges, die Sie letztes Monat erhalten haben:\n";
                    for (Badge badge : badges) {
                        message += "Badge \"" + badge.getBadgeTitle() + "\": " + badge.getTimeSpentOnTask() + " Stunden\n";
                    }
                }
                message += "\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            else {
                String message = "Liebe/r Benutzer/in!\n\nEs konnten leider keine Aktivitäten für das letzte Monat gefunden werden.\n\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            logger.info("Monthly e-mail notifications sent.");
        }
    }

    /**
     * Generates and sends yearly mail notifications.
     */
    @Scheduled(cron="0 0 0 1 1 *")
    public void yearlyIndividualMailNotification() {
        List<User> weeklyUsers = userRepository.findAllWithNotificationInterval(NotificationCategories.YEARLY.toString());
        Date today = new Date();
        Date aYearAgo = java.sql.Date.valueOf(LocalDate.now().minusYears(1));
        for (User user : weeklyUsers) {
            this.currentActivities = taskRepository.allTasksBetweenDatesFromUser(aYearAgo, today, user);
            if (this.currentActivities.size() != 0) {
                String message = "Liebe/r Benutzer/in!\n\nNachstehend finden Sie Ihre Aktivitäten des letzten Jahres:\n";
                this.generateHashMapPercent(currentActivities);
                for (HashMap.Entry<String, Float> entry : this.currentActivitiesPercentage.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();
                    message += "Aktivität \"" + key + "\": " + value + "%\n";
                }
                Collection<Badge> badges = badgeRepository.allBadgesBetweenDatesFromUser(aYearAgo, today, user);
                if (badges.size() != 0) {
                    message += "\nNachstehend finden Sie alle Badges, die Sie im letzten Jahr erhalten haben:\n";
                    for (Badge badge : badges) {
                        message += "Badge \"" + badge.getBadgeTitle() + "\": " + badge.getTimeSpentOnTask() + " Stunden\n";
                    }
                }
                message += "\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            else {
                String message = "Liebe/r Benutzer/in!\n\nEs konnten leider keine Aktivitäten für das letzte Jahr gefunden werden.\n\n Mit freundlichen Grüßen,\n Ihr TimeManager Team";
                mailService.parallelMessaging(user, "Ihre personalisierte Produktivitätsauswertung", message);
            }
            logger.info("Yearly e-Mail notifications sent.");
        }
    }
}
