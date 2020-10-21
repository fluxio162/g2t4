package TimeManager.tests.ControllerTests;

import TimeManager.model.Badge;
import TimeManager.model.BadgeTitles;
import TimeManager.services.BadgeService;
import TimeManager.ui.controllers.BadgeDetailController;
import TimeManager.model.User;
import TimeManager.services.UserService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BadgeDetailControllerTest {
    @Autowired
    BadgeService badgeService;

    @Autowired
    UserService userService;

    BadgeDetailController badgeDetailController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializeBadgeDetailController() {
        this.badgeDetailController = new BadgeDetailController();
        this.badgeDetailController = new BadgeDetailController(badgeService);
        Assert.assertNotNull("BadgeDetailController could not be initialized", badgeDetailController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateBadge() {
        this.badgeDetailController = new BadgeDetailController(badgeService);
        User user = userService.loadUser("user64");
        Assert.assertNotNull("user64 could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Badge toBeCreatedBadge = new Badge();
        toBeCreatedBadge.setBadgeTitle(BadgeTitles.CUSTOMER_MEETING.toString());
        toBeCreatedBadge.setImg(new File("/images/CustomerMeeting.png"));
        toBeCreatedBadge.setUser(user);
        Date today = new Date();
        toBeCreatedBadge.setCreateDate(today);
        toBeCreatedBadge.setEnabled(true);
        int id = 123;
        toBeCreatedBadge.setBadgeId(id);
        badgeDetailController.save(toBeCreatedBadge);
        badgeDetailController.setBadge(toBeCreatedBadge);
        badgeDetailController.doSaveBadge();
        badgeDetailController.doReloadBadge();

        Badge freshlyCreatedBadge = badgeDetailController.getBadge();
        Assert.assertNotNull("New badge could not be loaded from test data source after being saved", freshlyCreatedBadge);
        Assert.assertEquals("Badge does not have the correct title attribute stored after being saved", BadgeTitles.CUSTOMER_MEETING.toString(), freshlyCreatedBadge.getBadgeTitle());
        Assert.assertEquals("Badge does not have the correct user stored after being saved", user, freshlyCreatedBadge.getUser());
        Assert.assertTrue("Badge does not have the correct value for enabling stored after being saved", freshlyCreatedBadge.isEnabled());
        Assert.assertEquals("Badge does not have the correct id attribute stored after being saved", id, freshlyCreatedBadge.getBadgeId());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteBadge() {
        this.badgeDetailController = new BadgeDetailController(badgeService);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        int actualNumberOfBadges = badgeService.getAllBadges().size();
        User user = userService.loadUser("user64");

        Badge badge = new Badge();
        badge.setBadgeTitle(BadgeTitles.CUSTOMER_MEETING.toString());
        badge.setImg(new File("/images/CustomerMeeting.png"));
        badge.setUser(user);
        Date today = new Date();
        badge.setCreateDate(today);
        badge.setEnabled(true);
        int id = 123;
        badge.setBadgeId(id);
        badgeDetailController.save(badge);
        badgeDetailController.setBadge(badge);
        badgeDetailController.doSaveBadge();
        badgeDetailController.doReloadBadge();

        Badge toBeDeletedBadge = badgeDetailController.getBadge();

       badgeDetailController.doDeleteBadge();

        Assert.assertEquals("The badge has not been deleted after calling BadgeService.deleteBadge", actualNumberOfBadges, badgeService.getAllBadges().size());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedBadge.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedBadge.getDeleteDate());
        Assert.assertFalse("user50 is still enabled", toBeDeletedBadge.isEnabled());
    }

}
