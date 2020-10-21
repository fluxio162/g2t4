package TimeManager.tests.ServiceTests;

import TimeManager.model.DefaultTimeFlipConfiguration;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.services.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Some very basic tests for {@link TimeFlipService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TimeFlipServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TimeFlipService timeFlipService;

    @Autowired
    MiniserverService miniserverService;


    @Test
    @WithMockUser(username = "testUser", authorities = {"ADMIN"})
    public void testDataInitialization() {
        Assert.assertEquals("Insufficient amount of timeflips initialized for test data source", 20, timeFlipService.getAllTimeFlips().size());
        for (TimeFlip timeFlip : timeFlipService.getAllTimeFlips()) {
            if (timeFlip.getTimeFlipId() == 1) {
                Assert.assertEquals("TimeFlip \"1\" does not have user user45", "user45", timeFlip.getUser().getUsername());
                Assert.assertNotNull("TimeFlip \"1\" does not have a createUser defined", timeFlip.getCreateUser());
                Assert.assertNotNull("TimeFlip \"1\" does not have a createDate defined", timeFlip.getCreateDate());
                Assert.assertNull("TimeFlip \"1\" has a updateUser defined", timeFlip.getUpdateUser());
                Assert.assertNull("TimeFlip \"1\" has a updateDate defined", timeFlip.getUpdateDate());
                Assert.assertEquals("TimeFlip \"1\" does not have default configuration defined", timeFlip.getTimeFlipConfiguration(), DefaultTimeFlipConfiguration.getDefaultTimeFlipConfiguration());
            }
            else if (timeFlip.getTimeFlipId() == 6) {
                Assert.assertEquals("TimeFlip \"6\" does not have user user50", "user50", timeFlip.getUser().getUsername());
                Assert.assertNotNull("TimeFlip \"6\" does not have a createUser defined", timeFlip.getCreateUser());
                Assert.assertNotNull("TimeFlip \"6\" does not have a createDate defined", timeFlip.getCreateDate());
                Assert.assertNull("TimeFlip \"6\" has a updateUser defined", timeFlip.getUpdateUser());
                Assert.assertNull("TimeFlip \"6\" has a updateDate defined", timeFlip.getUpdateDate());
                Assert.assertEquals("TimeFlip \"6\" does not have default configuration defined", timeFlip.getTimeFlipConfiguration(), DefaultTimeFlipConfiguration.getDefaultTimeFlipConfiguration());
            }
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testLoadFlip() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        TimeFlip toBeLoadedTimeFlip = timeFlipService.loadTimeFlip(1);
        Assert.assertNotNull("TimeFlip 1 could not be loaded from test data source", toBeLoadedTimeFlip);

        Assert.assertEquals("TimeFlip \"1\" has wrong MAC address stored", "5E-4A-8A-A5-F4-E8", toBeLoadedTimeFlip.getMacAddress());
        Assert.assertEquals("TimeFlip \"1\" has wrong user stored", userService.loadUser("user45"), toBeLoadedTimeFlip.getUser());
        Assert.assertNotNull("TimeFlip \"1\" does not have configuration stored", toBeLoadedTimeFlip.getTimeFlipConfiguration());
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Pause/Dienstfrei",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(0));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Konzeption",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(1));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Design",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(2));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Implementierung",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(3));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Testen",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(4));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Dokumentation",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(5));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Fehleranalyse und -korrektur",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(6));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Meeting (intern)",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(7));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Kundenbesprechung",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(8));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Fortbildung",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(9));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Projektmanagement",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(10));
        Assert.assertEquals("TimeFlip \"1\" has wrong configuration for side 0 stored", "Sonstiges",toBeLoadedTimeFlip.getTimeFlipConfiguration().get(11));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteTimeFlip() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        TimeFlip toBeDeletedTimeFlip = timeFlipService.loadTimeFlip(20);
        Assert.assertNotNull("TimeFlip 20 could not be loaded from test data source", toBeDeletedTimeFlip);

        timeFlipService.deleteTimeFlip(toBeDeletedTimeFlip);

        Assert.assertEquals("No TimeFlip has been deleted after calling TimeFlipService.deleteTimeFlip", 19, timeFlipService.getAllTimeFlips().size());
        Assert.assertNull("TimeFlip 20 is still assigned to user64", userService.loadUser("user64").getTimeFlip());
        Assert.assertFalse("TimeFlip 20 is still in Room 5", roomService.loadRoom(5).getMiniserver().getTimeFlip().contains(toBeDeletedTimeFlip));
        Assert.assertFalse("TimeFlip 20 is still assigned to Miniserver 5", miniserverService.loadMiniserver(5).getTimeFlip().contains(toBeDeletedTimeFlip));

        for (TimeFlip remainingTimeFlip : timeFlipService.getAllTimeFlips()) {
            Assert.assertNotEquals("Deleted TimeFlip 20  could still be loaded from test data source via TimeFlipService.getAllTimeFlips()", toBeDeletedTimeFlip.getTimeFlipId(), remainingTimeFlip.getTimeFlipId());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateTimeFlip() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        TimeFlip toBeUpdatedTimeFlip = timeFlipService.loadTimeFlip(4);
        Assert.assertNotNull("TimeFlip 4 could not be loaded from test data source", toBeUpdatedTimeFlip);

        Assert.assertNull("TimeFlip \"4\" has a updateUser defined", toBeUpdatedTimeFlip.getUpdateUser());
        Assert.assertNull("TimeFlip \"4\" has a updateDate defined", toBeUpdatedTimeFlip.getUpdateDate());


        timeFlipService.updateTimeFlip(toBeUpdatedTimeFlip, "user49", "user48");

        TimeFlip freshlyLoadedTimeFlip = timeFlipService.loadTimeFlip(4);
        Assert.assertNotNull("TimeFlip 4 could not be loaded from test data source after being saved", freshlyLoadedTimeFlip);
        Assert.assertNotNull("TimeFlip \"4\" does not have a updateUser defined after being saved", freshlyLoadedTimeFlip.getUpdateUser());
        Assert.assertEquals("TimeFlip \"4\" has wrong updateUser set", adminUser, freshlyLoadedTimeFlip.getUpdateUser());
        Assert.assertNotNull("TimeFlip \"4\" does not have a updateDate defined after being saved", freshlyLoadedTimeFlip.getUpdateDate());
        Assert.assertEquals("TimeFlip \"4\" does not have the correct user attribute stored being saved", "user49", freshlyLoadedTimeFlip.getUser().getUsername());
        Assert.assertNull("TimeFlip 4 is still assigned to old User", userService.loadUser("user48").getTimeFlip());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateTimeFlipWithNoCurrentUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        TimeFlip toBeUpdatedTimeFlip = timeFlipService.createTimeFlip(new TimeFlip(), 999, "noUser");
        Assert.assertNotNull("TimeFlip 4 could not be loaded from test data source", toBeUpdatedTimeFlip);

        Assert.assertNull("TimeFlip \"4\" has a updateUser defined", toBeUpdatedTimeFlip.getUpdateUser());
        Assert.assertNull("TimeFlip \"4\" has a updateDate defined", toBeUpdatedTimeFlip.getUpdateDate());


        timeFlipService.updateTimeFlip(toBeUpdatedTimeFlip, "user50", null);

        TimeFlip freshlyLoadedTimeFlip = timeFlipService.loadTimeFlip(999);
        Assert.assertNotNull("TimeFlip 999 could not be loaded from test data source after being saved", freshlyLoadedTimeFlip);
        Assert.assertNotNull("TimeFlip \"999\" does not have a updateUser defined after being saved", freshlyLoadedTimeFlip.getUpdateUser());
        Assert.assertEquals("TimeFlip \"999\" has wrong updateUser set", adminUser, freshlyLoadedTimeFlip.getUpdateUser());
        Assert.assertNotNull("TimeFlip \"999\" does not have a updateDate defined after being saved", freshlyLoadedTimeFlip.getUpdateDate());
        Assert.assertEquals("TimeFlip \"999\" does not have the correct user attribute stored being saved", "user50", freshlyLoadedTimeFlip.getUser().getUsername());
        Assert.assertEquals("TimeFlip 999 is not assigned to new Miniserver", 4, miniserverService.findMiniserverWithTimeFlip(freshlyLoadedTimeFlip).getMiniserverId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateTimeFlip() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);



        TimeFlip toBeCreatedTimeFlip = new TimeFlip();

        timeFlipService.createTimeFlip(toBeCreatedTimeFlip, 99, "user034");

        TimeFlip freshlyCreatedTimeFlip = timeFlipService.loadTimeFlip(99);
        Assert.assertNotNull("New TimeFlip could not be loaded from test data source after being saved", freshlyCreatedTimeFlip);
        Assert.assertEquals("TimeFlip \"99\" does not have a the correct timeFlipId attribute stored being saved", 99, freshlyCreatedTimeFlip.getTimeFlipId());
        Assert.assertEquals("TimeFlip \"99\" does not have a the correct user attribute stored being saved", "user034", freshlyCreatedTimeFlip.getUser().getUsername());
        Assert.assertEquals("TimeFlip \"99\" is not assigned to Miniserver 5", 5, miniserverService.findMiniserverWithTimeFlip(freshlyCreatedTimeFlip).getMiniserverId());
        Assert.assertNotNull("TimeFlip \"99\" does not have a createUser defined after being saved", freshlyCreatedTimeFlip.getCreateUser());
        Assert.assertEquals("TimeFlip \"99\" has wrong createUser set", adminUser, freshlyCreatedTimeFlip.getCreateUser());
        Assert.assertNotNull("TimeFlip \"99\" does not have a createDate defined after being saved", freshlyCreatedTimeFlip.getCreateDate());

        timeFlipService.deleteTimeFlip(freshlyCreatedTimeFlip);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingTimeFlipWithNullTimeFlipId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        TimeFlip toBeCreatedTimeFlip = new TimeFlip();
        timeFlipService.createTimeFlip(toBeCreatedTimeFlip, 0, null);
        Assert.assertNull("TimeFlip \"toBeCreatedTimeFlip\" could be loaded from database", timeFlipService.loadTimeFlip(toBeCreatedTimeFlip.getTimeFlipId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingTimeFlipWithAlreadyTimeFlipId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User currentUser = timeFlipService.loadTimeFlip(15).getUser();

        TimeFlip toBeCreatedTimeFlip = new TimeFlip();
        timeFlipService.createTimeFlip(toBeCreatedTimeFlip, 15, null);

        Assert.assertEquals("TimeFlip 15 was created new and is not assigned to user59 anymore.", currentUser, timeFlipService.loadTimeFlip(15).getUser());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadTimeFlips() {
        for (TimeFlip timeFlip : timeFlipService.getAllTimeFlips()) {
            Assert.fail("Call to timeFlipService.getAllTimeFlips() should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadTimeFlips() {
        for (TimeFlip timeFlip : timeFlipService.getAllTimeFlips()) {
            Assert.fail("Call to timeFlipService.getAllTimeFlips() should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedTimeFlip() {
        TimeFlip timeFlip = timeFlipService.loadTimeFlip(1);
        Assert.fail("Call to timeFlipService.loadTimeFlip should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadTimeFlip() {
        TimeFlip timeFlip = timeFlipService.loadTimeFlip(1);
        Assert.assertEquals("Call to timeFlipService.loadTimeFlip() returned wrong TimeFlip", 1, timeFlip.getTimeFlipId());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveTimeFlip() {
        timeFlipService.saveTimeFlip(new TimeFlip());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteTimeFlip() {
        TimeFlip timeFlip = timeFlipService.loadTimeFlip(1);
        Assert.assertEquals("Call to timeFlipService.loadTimeFlip returned wrong TimeFlip", 1, timeFlip.getTimeFlipId());
        timeFlipService.deleteTimeFlip(timeFlip);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateTimeFlip() {
        TimeFlip newTimeFlip = new TimeFlip();
        timeFlipService.createTimeFlip(newTimeFlip, 123, null);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedUpdateTimeFlip() {
        TimeFlip newTimeFlip = new TimeFlip();
        timeFlipService.updateTimeFlip(newTimeFlip, "newUser", "oldUser");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetAllTasks() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        List<String> taskList = timeFlipService.getAllTasks();
        Assert.assertEquals("timeFlipService.getAllTasks() did not return all tasks", 12, taskList.size());
        Assert.assertTrue("TaskList does not contain Task 'Pause/Dienstfrei'", taskList.contains("Pause/Dienstfrei"));
        Assert.assertTrue("TaskList does not contain Task 'Konzeption'", taskList.contains("Konzeption"));
        Assert.assertTrue("TaskList does not contain Task 'Design'", taskList.contains("Design"));
        Assert.assertTrue("TaskList does not contain Task 'Implementierung'", taskList.contains("Implementierung"));
        Assert.assertTrue("TaskList does not contain Task 'Testen'", taskList.contains("Testen"));
        Assert.assertTrue("TaskList does not contain Task 'Dokumentation'", taskList.contains("Dokumentation"));
        Assert.assertTrue("TaskList does not contain Task 'Fehleranalyse und -korrektur'", taskList.contains("Fehleranalyse und -korrektur"));
        Assert.assertTrue("TaskList does not contain Task 'Meeting (intern)'", taskList.contains("Meeting (intern)"));
        Assert.assertTrue("TaskList does not contain Task 'Kundenbesprechung'", taskList.contains("Kundenbesprechung"));
        Assert.assertTrue("TaskList does not contain Task 'Fortbildung'", taskList.contains("Fortbildung"));
        Assert.assertTrue("TaskList does not contain Task 'Projektmanagement'", taskList.contains("Projektmanagement"));
        Assert.assertTrue("TaskList does not contain Task 'Sonstiges'", taskList.contains("Sonstiges"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTimeFlipSids() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Map<Integer, String> sideMap = timeFlipService.getTimeFlipSides();
        Assert.assertEquals("timeFlipService.getTimeFlipSides() did not return all sides", 12, sideMap.size());
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(0));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(1));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(2));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(3));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(4));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(5));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(6));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(7));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(8));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(9));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(10));
        Assert.assertTrue("SideMap does not contain side 0", sideMap.containsKey(11));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTimeFlipUserMap() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Map<TimeFlip, User> timeFlipUserMap = timeFlipService.getTimeFlipUserMap();
        Assert.assertEquals("Did not found all TimeFlips.", 20, timeFlipUserMap.size());
        Assert.assertEquals("Wrong User for TimeFlip 1 returned.", "user45", timeFlipUserMap.get(timeFlipService.loadTimeFlip(1)).getUsername());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetFreeTimeFlips() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        List<TimeFlip> availableTimeFlips = timeFlipService.getAllFreeTimeFlips();
        Assert.assertEquals("Wrong number of TimeFlips found.", 0, availableTimeFlips.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindMaxTimeFlipId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong highest TimeFlipId found", 20, timeFlipService.findMaxTimeFlipId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetAvailableTimeFlipsId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        List<Integer> availableTimeFlips = timeFlipService.getAvailableTimeFlips();
        Assert.assertEquals("Wrong number of TimeFlips found.", 0, availableTimeFlips.size());
    }

}
