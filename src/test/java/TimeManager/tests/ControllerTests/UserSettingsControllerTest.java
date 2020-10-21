package TimeManager.tests.ControllerTests;

import TimeManager.model.NotificationCategories;
import TimeManager.model.User;
import TimeManager.services.UserService;
import TimeManager.ui.controllers.UserSettingsController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserSettingsControllerTest {

    @Autowired
    UserService userService;

    UserSettingsController userSettingsController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializeAllActivitiesController() {
        this.userSettingsController = new UserSettingsController();
        this.userSettingsController = new UserSettingsController(userService);
        Assert.assertNotNull("AllActivitiesController could not be initialized",userSettingsController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetNotificationsCategories() {
        this.userSettingsController = new UserSettingsController(userService);
        userSettingsController.init();
        Collection<String> toBeTestedNotificationCategories = userSettingsController.getNotificationCategories();
        Collection<String> alreadyExistingNotificationCategories = new ArrayList<>();
        alreadyExistingNotificationCategories.add(NotificationCategories.MONTHLY.toString());
        alreadyExistingNotificationCategories.add(NotificationCategories.WEEKLY.toString());
        alreadyExistingNotificationCategories.add(NotificationCategories.YEARLY.toString());
        alreadyExistingNotificationCategories.add(NotificationCategories.NEVER.toString());

        for (String category : toBeTestedNotificationCategories) {
            Assert.assertTrue("Notification categories in Controller are no the same as in Enum", alreadyExistingNotificationCategories.contains(category));
        }
    }

        @Test
        @DirtiesContext
        @WithMockUser(username = "user64", authorities = {"EMPLOYEE"})
        public void testSaveUser(){
            this.userSettingsController = new UserSettingsController(userService);
            User toBeChangedUser = userService.loadUser("user64");
            Assert.assertNotNull("user64 could not be loaded from test data source", toBeChangedUser);

            String oldMail = toBeChangedUser.getEmail();
            String newMail = "mail123@test.at";
            toBeChangedUser.setEmail(newMail);

            userSettingsController.saveUser(toBeChangedUser);
            User changedUser = userService.loadUser("user64");

            Assert.assertEquals("User was not saved correctly after changing mail address", changedUser.getEmail(), newMail);
            Assert.assertNotEquals("User was not saved correctly after changing mail address", changedUser.getEmail(), oldMail);
        }
    }


