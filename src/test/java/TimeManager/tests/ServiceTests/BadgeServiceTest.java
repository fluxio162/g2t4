package TimeManager.tests.ServiceTests;

import TimeManager.model.Badge;
import TimeManager.model.BadgeTitles;
import TimeManager.model.User;
import TimeManager.services.BadgeService;
import TimeManager.services.DepartmentService;
import TimeManager.services.TeamService;
import TimeManager.services.UserService;

import java.util.Collection;

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

/**
 * Some very basic tests for {@link BadgeService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BadgeServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    BadgeService badgeService;

    @Autowired
    TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateBadge() {
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
        badgeService.saveBadge(toBeCreatedBadge);

        Badge freshlyCreatedBadge = badgeService.loadBadge(id);
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
        badgeService.saveBadge(badge);

        Badge toBeDeletedBadge = badgeService.loadBadge(id);

        badgeService.deleteBadge(toBeDeletedBadge);

        Assert.assertEquals("The badge has not been deleted after calling BadgeService.deleteBadge", actualNumberOfBadges, badgeService.getAllBadges().size());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedBadge.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedBadge.getDeleteDate());
        Assert.assertFalse("user50 is still enabled", toBeDeletedBadge.isEnabled());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetBadgesFromUser() {
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

        Collection<Badge> toBeReturnedBadge = badgeService.getAllFromUser(user);

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user033", authorities = {"ADMIN"})
    public void testGetEmployeeBadgesTeam() {
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

        Collection<Badge> toBeReturnedBadge = badgeService.getEmployeeBadges();

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }


    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user006", authorities = {"ADMIN"})
    public void testGetEmployeeBadgesDepartment() {
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

        Collection<Badge> toBeReturnedBadge = badgeService.getEmployeeBadges();

        Assert.assertEquals("The badge has not been assigned to the user", 1, toBeReturnedBadge.size());

        for (Badge b : toBeReturnedBadge) {
            Assert.assertEquals("The badge has not been assigned to the user", b.getBadgeId(), badge.getBadgeId());
        }


    }


}
