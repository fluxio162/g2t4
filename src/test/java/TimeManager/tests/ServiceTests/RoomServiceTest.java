package TimeManager.tests.ServiceTests;

import TimeManager.model.Room;
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

import java.util.*;

/**
 * Some very basic tests for {@link RoomService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RoomServiceTest {

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
        Assert.assertEquals("Insufficient amount of users initialized for test data source", 5, roomService.getAllRooms().size());
        for (Room room : roomService.getAllRooms()) {
            if (room.getRoomId() == 1) {
                Assert.assertNotNull("Room 1 does not have a createUser defined", room.getCreateUser());
                Assert.assertNotNull("Room 1 does not have a createDate defined", room.getCreateDate());
                Assert.assertNull("Room 1 has a updateUser defined", room.getUpdateUser());
                Assert.assertNull("Room 1 has a updateDate defined", room.getUpdateDate());
            } else if (room.getRoomId() == 3) {
                Assert.assertNotNull("Room 3 does not have a createUser defined", room.getCreateUser());
                Assert.assertNotNull("Room 3 does not have a createDate defined", room.getCreateDate());
                Assert.assertNull("Room 3 has a updateUser defined", room.getUpdateUser());
                Assert.assertNull("Room 3 has a updateDate defined", room.getUpdateDate());
            } else if (room.getRoomId() == 5) {
                Assert.assertNotNull("Room 5 does not have a createUser defined", room.getCreateUser());
                Assert.assertNotNull("Room 5 does not have a createDate defined", room.getCreateDate());
                Assert.assertNull("Room 5 has a updateUser defined", room.getUpdateUser());
                Assert.assertNull("Room 5 has a updateDate defined", room.getUpdateDate());
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteRoom() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Room toBeDeletedRoom = roomService.loadRoom(1);
        Assert.assertNotNull("Room 1 could not be loaded from test data source", toBeDeletedRoom);

        roomService.deleteRoom(toBeDeletedRoom);

        Assert.assertEquals("No Room has been deleted after calling RoomService.deleteRoom", 4, roomService.getAllRooms().size());
        Assert.assertNull("user61 still in Room 1", userService.loadUser("user61").getRoom());
        Assert.assertNull("Miniserver 5 is still assigned to Room 1",toBeDeletedRoom.getMiniserver());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedRoom.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedRoom.getDeleteDate());
        Assert.assertFalse("Room 1 is still enabled", toBeDeletedRoom.isEnabled());

        for (Room remainingRoom : roomService.getAllRooms()) {
            Assert.assertNotEquals("Deleted Room 1 could still be loaded from test data source via RoomService.getAllRooms", toBeDeletedRoom, remainingRoom);
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateRoom() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        roomService.createRoom(new Room(), 10, "5");

        Room freshlyCreatedRoom = roomService.loadRoom(10);
        Assert.assertNotNull("New Room could not be loaded from test data source after being saved", freshlyCreatedRoom);
        Assert.assertNotNull("Room 10 does not have a createUser defined after being saved", freshlyCreatedRoom.getCreateUser());
        Assert.assertEquals("Room 10 has wrong createUser set", adminUser, freshlyCreatedRoom.getCreateUser());
        Assert.assertNotNull("Room 10 does not have a createDate defined after being saved", freshlyCreatedRoom.getCreateDate());
        Assert.assertEquals("Room 10 does not have Miniserver 5 assigned.", 5, freshlyCreatedRoom.getMiniserver().getMiniserverId());
        Assert.assertNull("Miniserver 5 is still assigned to Room 5", roomService.loadRoom(5).getMiniserver());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserWithNullRoomId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        roomService.createRoom(new Room(), 0, null);
        Assert.assertNull("Room could be loaded from database", roomService.loadRoom(0));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserWithAlreadyUsedRoomId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        roomService.createRoom(new Room(), 1, "3");
        Assert.assertEquals("Miniserver 3 is now assigned to Room 1", 1, roomService.loadRoom(1).getMiniserver().getMiniserverId());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadRooms() {
        for (Room room : roomService.getAllRooms()) {
            Assert.fail("Call to RoomService.getAllRooms should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadRooms() {
        for (Room room : roomService.getAllRooms()) {
            Assert.fail("Call to RoomService.getAllRooms should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadRoom() {
        Room room = roomService.loadRoom(1);
        Assert.fail("Call to RoomService.loadRoom should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadRoom() {
        Room room = roomService.loadRoom(1);
        Assert.assertEquals("Call to RoomService.loadRoom returned wrong Room", 1, room.getRoomId());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveRoom() {
        roomService.saveRoom(new Room());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteRoom() {
        roomService.deleteRoom(new Room());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateRoom() {
        roomService.createRoom(new Room(), 1, null);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindRoomsWithMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all Rooms with Miniserver assigned.", 5, roomService.findRoomsWithMiniserver().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindRoomByMiniserver() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did return wrong Room.", 1, roomService.findRoomByMiniserver(miniserverService.loadMiniserver(1)).getRoomId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindMaxRoomId() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did return wrong highest RoomId.", 5, roomService.findMaxRoomId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllAvailableRooms() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong number of Rooms returned", 5, roomService.getAvailableRooms().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetRoomUserMap() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Map<Room, List<User>> roomUserMap = new HashMap<>(roomService.getRoomUserMap());

        Assert.assertEquals("Wrong number of Rooms returned", 5, roomUserMap.size());
        Assert.assertEquals("Wrong number of User in Room 5", 5, roomUserMap.get(roomService.loadRoom(5)).size());
        Assert.assertTrue("User45 is not on Room 5", roomUserMap.get(roomService.loadRoom(5)).contains(userService.loadUser("user45")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTimeFlipsInRoom() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        List<TimeFlip> timeFlipList = new ArrayList<>(roomService.getTimeFlipsInRoom(userService.findUserInRoom(roomService.loadRoom(5))));

        Assert.assertEquals("Wrong number of TimeFlips returned", 4,timeFlipList.size());
        Assert.assertTrue("TimeFlip 1 is not in Room 5", timeFlipList.contains(timeFlipService.loadTimeFlip(1)));
    }
}
