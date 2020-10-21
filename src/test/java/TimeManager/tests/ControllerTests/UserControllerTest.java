package TimeManager.tests.ControllerTests;

import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.model.UserRole;
import TimeManager.services.*;
import TimeManager.ui.controllers.UserCreationController;
import TimeManager.ui.controllers.UserDetailController;
import TimeManager.ui.controllers.UserListController;
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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

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


    UserListController userListController;

    UserCreationController userCreationController;

    UserDetailController userDetailController;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserListController() {
        userListController = new UserListController(userService);
        Assert.assertEquals("Wrong number of user returned.", 52, userListController.getUsers().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerInit() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);

        userCreationController.init();

        Assert.assertNull("userRolesString is not null.", userCreationController.getUserRolesString());
        Assert.assertEquals("currentTeam is already assigned.", "Kein Team", userCreationController.getCurrentTeam());
        Assert.assertNull("selectedTeam is not null.", userCreationController.getSelectedTeam());
        Assert.assertNull("selectedDepartment is not null.", userCreationController.getSelectedDepartment());
        Assert.assertEquals("currentDepartment is already assigned.", "Keine Abteilung", userCreationController.getCurrentDepartment());
        Assert.assertNull("selectedTimeFlip is not null.", userCreationController.getSelectedTimeFlip());
        Assert.assertEquals("currentRoom is already assigned.", "Kein Raum", userCreationController.getCurrentRoom());
        Assert.assertNull("selectedRoom is not null.", userCreationController.getSelectedRoom());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerCreateUser() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);
        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername("testUser");
        userCreationController.setUser(toBeCreatedUser);
        userCreationController.setUserRolesString(Collections.singletonList("EMPLOYEE"));
        userCreationController.setSelectedTeam("Team 1");
        userCreationController.setSelectedDepartment("1");
        userCreationController.setSelectedRoom("1");
        userCreationController.doSaveUser();

        User freshlyCreatedUser = userService.loadUser("testUser");
        Assert.assertNotNull("testUser could not be loaded from database.", freshlyCreatedUser);
        Assert.assertEquals("Wrong createUser stored.", "admin", freshlyCreatedUser.getCreateUser().getUsername());
        Assert.assertNotNull("No createDate stored.", freshlyCreatedUser.getCreateDate());
        Assert.assertTrue("Wrong User Role stored during creation.", freshlyCreatedUser.getRoles().contains(UserRole.EMPLOYEE));
        Assert.assertEquals("Wrong Team attribute stored during creation.", "Team 1", freshlyCreatedUser.getTeam().getTeamName());
        Assert.assertEquals("Wrong Department attribute stored during creation.", "1", freshlyCreatedUser.getDepartment().getDepartmentName());
        Assert.assertEquals("Wrong Room attribute stored during creation.", 1, freshlyCreatedUser.getRoom().getRoomId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerGetAvailableRoles() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);

        userCreationController.init();

        List<String> availableRoles = userCreationController.getAvailableRolesString();
        Assert.assertEquals("Wrong number of availableRoles returned.", 4, availableRoles.size());
        Assert.assertTrue("List does not contain 'ADMIN'", availableRoles.contains("ADMIN"));
        Assert.assertTrue("List does not contain 'EMPLOYEE'", availableRoles.contains("EMPLOYEE"));
        Assert.assertTrue("List does not contain 'DEPARTMENT_MANAGER'", availableRoles.contains("DEPARTMENT_MANAGER"));
        Assert.assertTrue("List does not contain 'TEAM_LEADER'", availableRoles.contains("TEAM_LEADER"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerGetAvailableTeams() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);

        userCreationController.init();

        List<String> availableTeams = userCreationController.getAvailableTeams();
        Assert.assertEquals("Wrong number of availableTeams returned.", 5, availableTeams.size());
        Assert.assertTrue("List does not contain 'Team 1'", availableTeams.contains("Team 1"));
        Assert.assertTrue("List does not contain 'Team 2'", availableTeams.contains("Team 2"));
        Assert.assertTrue("List does not contain 'Team 3'", availableTeams.contains("Team 3"));
        Assert.assertTrue("List does not contain 'Team 4'", availableTeams.contains("Team 4"));
        Assert.assertTrue("List does not contain 'Team 5'", availableTeams.contains("Team 5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerGetAvailableDepartments() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);

        userCreationController.init();

        List<String> availableDepartments = userCreationController.getAvailableDepartments();
        Assert.assertEquals("Wrong number of availableTeams returned.", 5, availableDepartments.size());
        Assert.assertTrue("List does not contain Department '1'", availableDepartments.contains("1"));
        Assert.assertTrue("List does not contain Department '2'", availableDepartments.contains("2"));
        Assert.assertTrue("List does not contain Department '3'", availableDepartments.contains("3"));
        Assert.assertTrue("List does not contain Department '4'", availableDepartments.contains("4"));
        Assert.assertTrue("List does not contain Department '5'", availableDepartments.contains("5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserCreationControllerGetAvailableRooms() {
        userCreationController = new UserCreationController(userService, teamService, departmentService, timeFlipService, roomService);

        userCreationController.init();

        List<String> availableRooms = userCreationController.getAvailableRooms();
        Assert.assertEquals("Wrong number of availableRooms returned.", 5, availableRooms.size());
        Assert.assertTrue("List does not contain Room '1'", availableRooms.contains("1"));
        Assert.assertTrue("List does not contain Room '2'", availableRooms.contains("2"));
        Assert.assertTrue("List does not contain Room '3'", availableRooms.contains("3"));
        Assert.assertTrue("List does not contain Room '4'", availableRooms.contains("4"));
        Assert.assertTrue("List does not contain Room '5'", availableRooms.contains("5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerGetAvailableRoles() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();

        List<String> availableRoles = userDetailController.getAvailableRolesString();
        Assert.assertEquals("Wrong number of availableRoles returned.", 4, availableRoles.size());
        Assert.assertTrue("List does not contain 'ADMIN'", availableRoles.contains("ADMIN"));
        Assert.assertTrue("List does not contain 'EMPLOYEE'", availableRoles.contains("EMPLOYEE"));
        Assert.assertTrue("List does not contain 'DEPARTMENT_MANAGER'", availableRoles.contains("DEPARTMENT_MANAGER"));
        Assert.assertTrue("List does not contain 'TEAM_LEADER'", availableRoles.contains("TEAM_LEADER"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerGetAvailableTeams() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();

        List<String> availableTeams = userDetailController.getAvailableTeams();
        Assert.assertEquals("Wrong number of availableTeams returned.", 5, availableTeams.size());
        Assert.assertTrue("List does not contain 'Team 1'", availableTeams.contains("Team 1"));
        Assert.assertTrue("List does not contain 'Team 2'", availableTeams.contains("Team 2"));
        Assert.assertTrue("List does not contain 'Team 3'", availableTeams.contains("Team 3"));
        Assert.assertTrue("List does not contain 'Team 4'", availableTeams.contains("Team 4"));
        Assert.assertTrue("List does not contain 'Team 5'", availableTeams.contains("Team 5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerGetAvailableDepartments() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();

        List<String> availableDepartments = userDetailController.getAvailableDepartments();
        Assert.assertEquals("Wrong number of availableTeams returned.", 5, availableDepartments.size());
        Assert.assertTrue("List does not contain Department '1'", availableDepartments.contains("1"));
        Assert.assertTrue("List does not contain Department '2'", availableDepartments.contains("2"));
        Assert.assertTrue("List does not contain Department '3'", availableDepartments.contains("3"));
        Assert.assertTrue("List does not contain Department '4'", availableDepartments.contains("4"));
        Assert.assertTrue("List does not contain Department '5'", availableDepartments.contains("5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerGetAvailableRooms() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();

        List<String> availableRooms = userDetailController.getAvailableRooms();
        Assert.assertEquals("Wrong number of availableRooms returned.", 5, availableRooms.size());
        Assert.assertTrue("List does not contain Room '1'", availableRooms.contains("1"));
        Assert.assertTrue("List does not contain Room '2'", availableRooms.contains("2"));
        Assert.assertTrue("List does not contain Room '3'", availableRooms.contains("3"));
        Assert.assertTrue("List does not contain Room '4'", availableRooms.contains("4"));
        Assert.assertTrue("List does not contain Room '5'", availableRooms.contains("5"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerGetAvailableTimeFlips() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();

        List<Integer> availableTimeFlips = userDetailController.getAvailableTimeFlips();
        Assert.assertEquals("Wrong number of availableTimeFlips returned.", 0, availableTimeFlips.size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerEditUser() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        timeFlipService.createTimeFlip(new TimeFlip(), 99, "noUser");

        User toBeEditedUser = userService.loadUser("user3");
        userDetailController.setUser(toBeEditedUser);
        userDetailController.setUserRolesString(Collections.singletonList("EMPLOYEE"));
        userDetailController.setSelectedTeam("Team 1");
        userDetailController.setSelectedDepartment("1");
        userDetailController.setSelectedRoom("1");
        userDetailController.setSelectedTimeFlip(99);
        userDetailController.doSaveUser();


        User freshlyLoadedUser = userService.loadUser("user3");
        Assert.assertNotNull("testUser could not be loaded from database.", freshlyLoadedUser);
        Assert.assertEquals("Wrong createUser stored.", "admin", freshlyLoadedUser.getUpdateUser().getUsername());
        Assert.assertNotNull("No createDate stored.", freshlyLoadedUser.getUpdateDate());
        Assert.assertTrue("Wrong User Role stored during creation.", freshlyLoadedUser.getRoles().contains(UserRole.EMPLOYEE));
        Assert.assertEquals("Wrong Team attribute stored during creation.", "Team 1", freshlyLoadedUser.getTeam().getTeamName());
        Assert.assertEquals("Wrong Department attribute stored during creation.", "1", freshlyLoadedUser.getDepartment().getDepartmentName());
        Assert.assertEquals("Wrong Room attribute stored during creation.", 1, freshlyLoadedUser.getRoom().getRoomId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerLoadUser() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);

        userDetailController.init();
        userDetailController.setUser(userService.loadUser("user45"));

        User testUser = userDetailController.getUser();
        Assert.assertEquals("Wrong firstName attribute returned.", "Janina", testUser.getFirstName());
        Assert.assertEquals("Wrong lastName attribute returned.", "Hirsch", testUser.getLastName());
        Assert.assertEquals("Wrong email address returned.", "JaninaHirsch@mail.at", testUser.getEmail());
        Assert.assertEquals("Wrong telephone number returned.", "+43 676 4580311", testUser.getPhone());
        Assert.assertTrue("Wrong role attribute returned.", testUser.getRoles().contains(UserRole.EMPLOYEE));
        Assert.assertEquals("Wrong Team attribute returned.", "Team 5", testUser.getTeam().getTeamName());
        Assert.assertEquals("Wrong Department attribute returned.", "5", testUser.getDepartment().getDepartmentName());
        Assert.assertEquals("Wrong RoomId returned.", 5, testUser.getRoom().getRoomId());
        Assert.assertEquals("Wrong TimeFlipId returned.", 1, testUser.getTimeFlip().getTimeFlipId());
        Assert.assertTrue("UserRole not found.", userDetailController.getUserRolesString().contains("EMPLOYEE"));

        Assert.assertEquals("Wrong attribute stored in currentTeam", "Team 5", userDetailController.getCurrentTeam());
        Assert.assertEquals("Wrong attribute stored in currentRoom", "5", userDetailController.getCurrentRoom());
        Assert.assertEquals("Wrong attribute stored in currentDepartment", "5", userDetailController.getCurrentDepartment());
        Assert.assertEquals("Wrong attribute stored in currentTimeFlip", Integer.valueOf(1), userDetailController.getCurrentTimeFlip());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUserDetailControllerDeleteUser() {
        userDetailController = new UserDetailController(userService, teamService, departmentService, timeFlipService, roomService);


        User toBeDeletedUser = userService.loadUser("user3");

        userDetailController.setUser(toBeDeletedUser);
        userDetailController.doDeleteUser();


        User freshlyDeletedUser = userService.loadUser("user3");
        Assert.assertNotNull("testUser could not be loaded from database.", freshlyDeletedUser);
        Assert.assertNotNull("DeleteUser not stored.", freshlyDeletedUser.getDeleteUser());
        Assert.assertNotNull("DeleteDate not stored.", freshlyDeletedUser.getDeleteDate());
        Assert.assertEquals("Wrong number of total user returned.", 51, userService.getAllUsers().size());
    }
}
