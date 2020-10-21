package TimeManager.tests.ControllerTests;

import TimeManager.model.Badge;
import TimeManager.model.BadgeTitles;
import TimeManager.services.BadgeService;
import TimeManager.ui.controllers.BadgeListController;
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
import java.util.Collection;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BadgeListControllerTest {
    @Autowired
    BadgeService badgeService;

    @Autowired
    UserService userService;

    BadgeListController badgeListController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializeBadgeListController() {
        this.badgeListController = new BadgeListController();
        this.badgeListController = new BadgeListController(badgeService);
        Assert.assertNotNull("BadgeListController could not be initialized", badgeListController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getBadgesFromUser() {
        this.badgeListController = new BadgeListController(badgeService);
        User user = new User();
        user.setUsername("user000000");
        userService.saveUser(user);

        Badge badge = new Badge();
        badge.setBadgeTitle(BadgeTitles.CUSTOMER_MEETING.toString());
        badge.setImg(new File("/images/CustomerMeeting.png"));
        badge.setUser(user);
        Date today = new Date();
        badge.setCreateDate(today);
        badge.setEnabled(true);
        int id = 123;
        badge.setBadgeId(id);
        badge.setTimeSpentOnTask((float) 1.50);

        badgeService.saveBadge(badge);

        Collection<Badge> toBeReturnedBadge = badgeListController.getAllBadgesFromUser(user);

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user033", authorities = {"ADMIN"})
    public void testGetEmployeeBadgesTeam() {
        this.badgeListController = new BadgeListController(badgeService);
        User employeeUser = userService.loadUser("user50");
        Assert.assertNotNull("user50 could not be loaded from test data source", employeeUser);

        Badge badge = new Badge();
        badge.setBadgeTitle(BadgeTitles.CUSTOMER_MEETING.toString());
        badge.setImg(new File("/images/CustomerMeeting.png"));
        badge.setUser(employeeUser);
        Date today = new Date();
        badge.setCreateDate(today);
        badge.setEnabled(true);
        int id = 123;
        badge.setBadgeId(id);
        badge.setTimeSpentOnTask((float) 1.50);

        badgeService.saveBadge(badge);

        Collection<Badge> toBeReturnedBadge = badgeListController.getEmployeeBadges();

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }


    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user006", authorities = {"ADMIN"})
    public void testGetEmployeeBadgesDepartment() {
        this.badgeListController = new BadgeListController(badgeService);
        User employeeUser = userService.loadUser("user50");
        Assert.assertNotNull("user50 could not be loaded from test data source", employeeUser);

        Badge badge = new Badge();
        badge.setBadgeTitle(BadgeTitles.CUSTOMER_MEETING.toString());
        badge.setImg(new File("/images/CustomerMeeting.png"));
        badge.setUser(employeeUser);
        Date today = new Date();
        badge.setCreateDate(today);
        badge.setEnabled(true);
        int id = 123;
        badge.setBadgeId(id);
        badge.setTimeSpentOnTask((float) 1.50);

        badgeService.saveBadge(badge);

        Collection<Badge> toBeReturnedBadge = badgeListController.getEmployeeBadges();

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }


    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetAllNewBadges() {
        this.badgeListController = new BadgeListController(badgeService);

        Collection<Badge> allBadges = badgeListController.getBadges();
        Collection<Badge> allNewBadges = badgeListController.getLatestBadges();

        Assert.assertEquals("All badges are not the same amount as newest badges", allBadges.size(), allNewBadges.size());

        for (Badge b : allNewBadges){
            Assert.assertTrue("All badges are not the same as newest badges", allBadges.contains(b));
        }

    }









}
