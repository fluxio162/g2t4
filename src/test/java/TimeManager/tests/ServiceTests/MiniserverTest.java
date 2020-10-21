package TimeManager.tests.ServiceTests;

import TimeManager.model.Miniserver;
import TimeManager.model.Room;
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

import java.util.Map;

/**
 * Some very basic tests for {@link MiniserverService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MiniserverTest {

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
    public void testDatainitialization() {
        Assert.assertEquals("Insufficient amount of miniserverss initialized for test data source", 5, miniserverService.getAllMiniservers().size());
        for (Miniserver miniserver : miniserverService.getAllMiniservers()) {
            if (miniserver.getId() == 1) {
                Assert.assertNotNull("Miniserver \"1\" does not have a createUser defined", miniserver.getCreateUser());
                Assert.assertNotNull("Miniserver \"1\" does not have a createDate defined", miniserver.getCreateDate());
                Assert.assertNull("Miniserver \"1\" has a updateUser defined", miniserver.getUpdateUser());
                Assert.assertNull("Miniserver \"1\" has a updateDate defined", miniserver.getUpdateDate());
            } else if (miniserver.getId() == 3) {
                Assert.assertNotNull("Miniserver \"3\" does not have a createUser defined", miniserver.getCreateUser());
                Assert.assertNotNull("Miniserver \"3\" does not have a createDate defined", miniserver.getCreateDate());
                Assert.assertNull("Miniserver \"3\" has a updateUser defined", miniserver.getUpdateUser());
                Assert.assertNull("Miniserver \"3\" has a updateDate defined", miniserver.getUpdateDate());
            } else if (miniserver.getId() == 5) {
                Assert.assertNotNull("Miniserver \"5\" does not have a createUser defined", miniserver.getCreateUser());
                Assert.assertNotNull("Miniserver \"5\" does not have a createDate defined", miniserver.getCreateDate());
                Assert.assertNull("Miniserver \"5\" has a updateUser defined", miniserver.getUpdateUser());
                Assert.assertNull("Miniserver \"5\" has a updateDate defined", miniserver.getUpdateDate());
            }
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testLoadMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Miniserver toBeLoadedMiniserver = miniserverService.loadMiniserver(1);
        Assert.assertNotNull("Miniserver 1 could not be loaded from test data source", toBeLoadedMiniserver);

        Assert.assertEquals("Miniserver \"1\" has wrong IP Address stored.", "138.232.17.110", toBeLoadedMiniserver.getIPAddress());
        Assert.assertEquals("Miniserver \"1\" has a wrong number of TimeFlips stored", 4, toBeLoadedMiniserver.getTimeFlip().size());
        Assert.assertEquals("Miniserver \"1\" has a wrong room stored", toBeLoadedMiniserver.getRoom(), roomService.loadRoom(1));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Miniserver toBeDeletedMiniserver = miniserverService.loadMiniserver(5);
        Assert.assertNotNull("Miniserver 20 could not be loaded from test data source", toBeDeletedMiniserver);

        miniserverService.deleteMiniserver(toBeDeletedMiniserver);

        Assert.assertEquals("No Miniserver has been deleted after calling MiniserverService.deleteMiniserver", 4, miniserverService.getAllMiniservers().size());
        Assert.assertTrue("Miniserver 5 has still TimeFlips assigned", miniserverService.loadMiniserver(5).getTimeFlip().isEmpty());
        Assert.assertNull("Miniserver 5 is still assigned to Room 5", miniserverService.loadMiniserver(5).getRoom());
        Assert.assertNull("Miniserver 5 is still assigned to Room 5", roomService.loadRoom(5).getMiniserver());

        for (Miniserver remainingMiniserver : miniserverService.getAllMiniservers()) {
            Assert.assertNotEquals("Deleted Miniserver 5 could still be loaded from test data source via MiniserverService.getAllMiniservers()", toBeDeletedMiniserver, remainingMiniserver);
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Miniserver toBeUpdatedMiniserver = miniserverService.loadMiniserver(3);
        Assert.assertNotNull("Miniserver 3 could not be loaded from test data source", toBeUpdatedMiniserver);

        Assert.assertNull("Miniserver \"3\" has a updateUser defined", toBeUpdatedMiniserver.getUpdateUser());
        Assert.assertNull("Miniserver \"3\" has a updateDate defined", toBeUpdatedMiniserver.getUpdateDate());


        toBeUpdatedMiniserver.setIPAddress("1.1.1.1");

        miniserverService.updateMiniserver(toBeUpdatedMiniserver, "4");

        Miniserver freshlyLoadedMiniserver = miniserverService.loadMiniserver(3);

        Assert.assertEquals("Miniserver 3 is still assigned to Room 3", roomService.loadRoom(4), freshlyLoadedMiniserver.getRoom());
        Assert.assertEquals("Miniserver 3 does not have updated IP Address stored", "1.1.1.1", freshlyLoadedMiniserver.getIPAddress());
        Assert.assertFalse("TimeFlip 9 is still assigned to Miniserver 3", freshlyLoadedMiniserver.getTimeFlip().contains(timeFlipService.loadTimeFlip(9)));
        Assert.assertFalse("TimeFlip 10 is still assigned to Miniserver 3", freshlyLoadedMiniserver.getTimeFlip().contains(timeFlipService.loadTimeFlip(10)));
        Assert.assertFalse("TimeFlip 11 is still assigned to Miniserver 3", freshlyLoadedMiniserver.getTimeFlip().contains(timeFlipService.loadTimeFlip(11)));
        Assert.assertFalse("TimeFlip 12 is still assigned to Miniserver 3", freshlyLoadedMiniserver.getTimeFlip().contains(timeFlipService.loadTimeFlip(12)));
        Assert.assertFalse("TimeFlip 13 is still assigned to Miniserver 4", miniserverService.loadMiniserver(4).getTimeFlip().contains(timeFlipService.loadTimeFlip(9)));
        Assert.assertFalse("TimeFlip 14 is still assigned to Miniserver 4", miniserverService.loadMiniserver(4).getTimeFlip().contains(timeFlipService.loadTimeFlip(10)));
        Assert.assertFalse("TimeFlip 15 is still assigned to Miniserver 4", miniserverService.loadMiniserver(4).getTimeFlip().contains(timeFlipService.loadTimeFlip(11)));
        Assert.assertFalse("TimeFlip 16 is still assigned to Miniserver 4", miniserverService.loadMiniserver(4).getTimeFlip().contains(timeFlipService.loadTimeFlip(12)));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);


        Miniserver toBeCreatedMiniserver = new Miniserver();

        toBeCreatedMiniserver.setIPAddress("1.1.1.1");
        miniserverService.createMiniserver(toBeCreatedMiniserver, 13);


        Miniserver freshlyCreatedMiniserver = miniserverService.loadMiniserver(13);
        Assert.assertNotNull("New Miniserver could not be loaded from test data source after being saved", freshlyCreatedMiniserver);
        Assert.assertNotNull("Miniserver \"13\" does not have a createUser defined after being saved", freshlyCreatedMiniserver.getCreateUser());
        Assert.assertEquals("Miniserver \"13\" has wrong createUser set", adminUser, freshlyCreatedMiniserver.getCreateUser());
        Assert.assertNotNull("Miniserver \"13\" does not have a createDate defined after being saved", freshlyCreatedMiniserver.getCreateDate());
        Assert.assertEquals("Miniserver \"13\" does not have a the correct IP address stored being saved", "1.1.1.1", freshlyCreatedMiniserver.getIPAddress());
        Assert.assertTrue("Miniserver \"3\" has already TimeFlip assigned to.",  freshlyCreatedMiniserver.getTimeFlip().isEmpty());
        Assert.assertNull("Miniserver \"3\" hast already Room assigned to.", freshlyCreatedMiniserver.getRoom());
        Assert.assertTrue("Miniserver \"3\" is not enabled.", freshlyCreatedMiniserver.isEnabled());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingMiniserverWithIdNull() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Miniserver toBeCreatedMiniserver = new Miniserver();
        miniserverService.createMiniserver(toBeCreatedMiniserver, 0);
        Assert.assertNull("Miniserver \"toBeCreatedMiniserver\" could be loaded from database", miniserverService.loadMiniserver(0));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingAlreadyUsedMiniserverId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Miniserver toBeCreatedMiniserver = new Miniserver();
        toBeCreatedMiniserver.setIPAddress("1.1.1.1");
        miniserverService.createMiniserver(toBeCreatedMiniserver, 1);
        Assert.assertNotEquals("Miniserver \"1\" IP address has changed.", "1.1.1.1", miniserverService.loadMiniserver(1).getIPAddress());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadMiniservers() {
        for (Miniserver miniserver : miniserverService.getAllMiniservers()) {
            Assert.fail("Call to MiniserverService.getAllMiniservers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadMiniservers() {
        for (Miniserver miniserver : miniserverService.getAllMiniservers()) {
            Assert.fail("Call to miniserverService.getAllMiniservers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadMiniserver() {
        Miniserver miniserver = miniserverService.loadMiniserver(1);
        Assert.fail("Call to miniserverService.loadMiniserver should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadMiniserver() {
        Miniserver miniserver = miniserverService.loadMiniserver(1);
        Assert.assertEquals("Call to miniserverService.loadMiniserver returned wrong Miniserver", 1, miniserver.getMiniserverId());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveMiniserver() {
        miniserverService.saveMiniserver(new Miniserver());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteMiniserver() {
        Miniserver miniserver = new Miniserver();
        miniserverService.deleteMiniserver(miniserver);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateMiniserver() {
        Miniserver miniserver = new Miniserver();
        miniserverService.createMiniserver(miniserver, 1);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedUpdateMiniserver() {
        Miniserver miniserver = new Miniserver();
        miniserverService.updateMiniserver(miniserver, "1");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindMaxMiniserverId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong max MiniserverId returned.", 5, miniserverService.findMaxMiniserverId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllFreeMiniservers() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong number of Miniserver returned.", 0, miniserverService.getAllFreeMiniserver().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetMiniserverRoomMap() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong number of Miniserver returned.", 5, miniserverService.getMiniserverRoomMap().size());
        for(Map.Entry<Miniserver, Room> entry : miniserverService.getMiniserverRoomMap().entrySet()){
            Assert.assertEquals("Wrong Room in Map", roomService.loadRoom(entry.getKey().getRoom().getRoomId()), entry.getValue());
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllUnassignedMiniservers() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong number of Miniserver returned.", 0, miniserverService.getUnassignedMiniserver().size());
    }

}
