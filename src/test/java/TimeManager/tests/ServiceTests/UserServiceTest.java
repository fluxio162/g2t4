package TimeManager.tests.ServiceTests;

import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.model.UserRole;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Some very basic tests for {@link UserService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

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


    @Test
    @WithMockUser(username = "testUser", authorities = {"ADMIN"})
    public void testDatainitialization() {
        Assert.assertEquals("Insufficient amount of users initialized for test data source", 52, userService.getAllUsers().size());
        for (User user : userService.getAllUsers()) {
            if ("admin".equals(user.getUsername())) {
                Assert.assertTrue("User \"admin\" does not have role ADMIN", user.getRoles().contains(UserRole.ADMIN));
                Assert.assertNotNull("User \"admin\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"admin\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"admin\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"admin\" has a updateDate defined", user.getUpdateDate());
            } else if ("user003".equals(user.getUsername())) {
                Assert.assertTrue("User \"user003\" does not have role MANAGER", user.getRoles().contains(UserRole.DEPARTMENT_MANAGER));
                Assert.assertNotNull("User \"user003\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user003\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user003\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user003\" has a updateDate defined", user.getUpdateDate());
            } else if ("user018".equals(user.getUsername())) {
                Assert.assertTrue("User \"user018\" does not have role EMPLOYEE", user.getRoles().contains(UserRole.TEAM_LEADER));
                Assert.assertNotNull("User \"user018\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user018\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user018\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user018\" has a updateDate defined", user.getUpdateDate());
            } else if ("user45".equals(user.getUsername())) {
                Assert.assertTrue("User \"user45\" does not have role EMPLOYEE", user.getRoles().contains(UserRole.EMPLOYEE));
                Assert.assertNotNull("User \"user45\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"user45\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"user45\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"user45\" has a updateDate defined", user.getUpdateDate());
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeDeletedUser = userService.loadUser("user50");
        Assert.assertNotNull("user50 could not be loaded from test data source", toBeDeletedUser);

        userService.deleteUser(toBeDeletedUser);

        Assert.assertEquals("No user has been deleted after calling UserService.deleteUser", 51, userService.getAllUsers().size());
        Assert.assertFalse("user50 is still in Team 4", teamService.loadTeam("Team 4").getTeamMember().contains(toBeDeletedUser));
        Assert.assertNull("user50 is still in Room", toBeDeletedUser.getRoom());
        Assert.assertNull("user50 is still in Department", toBeDeletedUser.getDepartment());
        Assert.assertNull("user50 still has TimeFlip assigned", toBeDeletedUser.getTimeFlip());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedUser.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedUser.getDeleteDate());
        Assert.assertFalse("user50 is still enabled", toBeDeletedUser.isEnabled());

        for (User remainingUser : userService.getAllUsers()) {
            Assert.assertNotEquals("Deleted user50 could still be loaded from test data source via UserService.getAllUsers", toBeDeletedUser.getUsername(), remainingUser.getUsername());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeUpdatedUser = userService.loadUser("user45");
        Assert.assertNotNull("User45 could not be loaded from test data source", toBeUpdatedUser);

        Assert.assertNull("User \"user45\" has a updateUser defined", toBeUpdatedUser.getUpdateUser());
        Assert.assertNull("User \"user45\" has a updateDate defined", toBeUpdatedUser.getUpdateDate());


        List<String> updatedRole = new ArrayList<>();
        updatedRole.add("TEAM_LEADER");
        String team = "Team 2";
        String department = "2";
        String room = "2";
        toBeUpdatedUser.setEmail("changed-email@whatever.wherever");
        userService.updateUser(toBeUpdatedUser, updatedRole, team, department, 7, room);

        User freshlyLoadedUser = userService.loadUser("user45");
        Assert.assertNotNull("User45 could not be loaded from test data source after being saved", freshlyLoadedUser);
        Assert.assertNotNull("User \"user45\" does not have a updateUser defined after being saved", freshlyLoadedUser.getUpdateUser());
        Assert.assertEquals("User \"user45\" has wrong updateUser set", adminUser, freshlyLoadedUser.getUpdateUser());
        Assert.assertNotNull("User \"user45\" does not have a updateDate defined after being saved", freshlyLoadedUser.getUpdateDate());
        Assert.assertEquals("User \"user45\" does not have the correct email attribute stored being saved", "changed-email@whatever.wherever", freshlyLoadedUser.getEmail());
        Assert.assertTrue("User \"user45\" does not have the correct role attribute stored being saved", freshlyLoadedUser.getRoles().contains(UserRole.TEAM_LEADER));
        Assert.assertEquals("User \"user45\" does not have the correct team attribute stored being saved", team, freshlyLoadedUser.getTeam().getTeamName());
        Assert.assertEquals("User \"user45\" does not have the correct department attribute stored being saved", department, freshlyLoadedUser.getDepartment().getDepartmentName());
        Assert.assertEquals("User \"user45\" does not have the correct room attribute stored being saved", 2, freshlyLoadedUser.getRoom().getRoomId());
        Assert.assertEquals("User \"user45\" does not have the correct timeflip attribute stored being saved", 7, freshlyLoadedUser.getTimeFlip().getTimeFlipId());
        Assert.assertNull("TimeFlip 7 is still assigned to old User", userService.loadUser("user51").getTimeFlip());
}

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        TimeFlip timeFlip = new TimeFlip();
        timeFlip.setTimeFlipId(100);
        timeFlipService.saveTimeFlip(timeFlip);

        Date birthday = new Date(2000, 1, 1);

        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername("newuser");
        toBeCreatedUser.setPassword("passwd");
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName("New");
        toBeCreatedUser.setLastName("User");
        toBeCreatedUser.setEmail("new-email@whatever.wherever");
        toBeCreatedUser.setPhone("+12 345 67890");
        List<String> updatedRole = new ArrayList<>();
        updatedRole.add("EMPLOYEE");
        String team = "Team 1";
        String department = "1";
        String room = "1";
        toBeCreatedUser.setBirthDay(birthday);
        toBeCreatedUser.setNotification(true);
        userService.createUser(toBeCreatedUser, updatedRole, team, department, room, 100);

        User freshlyCreatedUser = userService.loadUser("newuser");
        Assert.assertNotNull("New user could not be loaded from test data source after being saved", freshlyCreatedUser);
        Assert.assertEquals("User \"newuser\" does not have a the correct username attribute stored being saved", "newuser", freshlyCreatedUser.getUsername());
        Assert.assertEquals("User \"newuser\" does not have a the correct password attribute stored being saved", "passwd", freshlyCreatedUser.getPassword());
        Assert.assertEquals("User \"newuser\" does not have a the correct firstName attribute stored being saved", "New", freshlyCreatedUser.getFirstName());
        Assert.assertEquals("User \"newuser\" does not have a the correct lastName attribute stored being saved", "User", freshlyCreatedUser.getLastName());
        Assert.assertEquals("User \"newuser\" does not have a the correct email attribute stored being saved", "new-email@whatever.wherever", freshlyCreatedUser.getEmail());
        Assert.assertEquals("User \"newuser\" does not have a the correct phone attribute stored being saved", "+12 345 67890", freshlyCreatedUser.getPhone());
        Assert.assertTrue("User \"newuser\" does not have role EMPLOYEE", freshlyCreatedUser.getRoles().contains(UserRole.EMPLOYEE));
        Assert.assertNotNull("User \"newuser\" does not have a createUser defined after being saved", freshlyCreatedUser.getCreateUser());
        Assert.assertEquals("User \"newuser\" has wrong createUser set", adminUser, freshlyCreatedUser.getCreateUser());
        Assert.assertNotNull("User \"newuser\" does not have a createDate defined after being saved", freshlyCreatedUser.getCreateDate());
        Assert.assertEquals("User \"newuser\" does not have the correct team attribute stored being saved", team, freshlyCreatedUser.getTeam().getTeamName());
        Assert.assertEquals("User \"newuser\" does not have the correct department attribute stored being saved", department, freshlyCreatedUser.getDepartment().getDepartmentName());
        Assert.assertEquals("User \"newuser\" does not have the correct room attribute stored being saved", 1, freshlyCreatedUser.getRoom().getRoomId());
        Assert.assertEquals("User \"newuser\" does not have the correct timeflip attribute stored being saved", timeFlip, freshlyCreatedUser.getTimeFlip());
        Assert.assertEquals("User \"newuser\" does not have the correct birthday attribute stored being saved", birthday, freshlyCreatedUser.getBirthDay());
        Assert.assertTrue("User \"newuser\" does not have the correct notification attribute stored being saved", freshlyCreatedUser.isNotification());
    }


    @Test(expected=NullPointerException.class)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserWithEmptyUsername() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User toBeCreatedUser = new User();
        userService.createUser(toBeCreatedUser, null, null, null, null, null);
        Assert.assertNull("User \"toBeCreatedUser\" could be loaded from database", userService.loadUser(toBeCreatedUser.getUsername()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserAlreadyExistingUsername() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        String adminEmail = adminUser.getEmail();

        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername("admin");
        toBeCreatedUser.setEmail("test");
        userService.createUser(toBeCreatedUser, null, null, null, null, null);

        Assert.assertEquals("Mail from \"adminUser\" has changed.", adminEmail, userService.loadUser(adminUser.getUsername()).getEmail());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadUser() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUser() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadSingleUser() {
        User user = userService.loadUser("admin");
        Assert.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadUser() {
        User user = userService.loadUser("user1");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user1", user.getUsername());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveUser() {
        userService.saveUser(new User());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteUser() {
        User user = new User();
        userService.deleteUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateUser() {
        User newUser = new User();
        userService.createUser(newUser, null, null, null, null, null);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedUpdateUser() {
        User newUser = new User();
        userService.updateUser(newUser, null, null, null, null, null);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllUserWithTimeFlipAssigned() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all User with TimeFlip assigned.", 20, userService.findUserWithTimeFlip().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindUserByAssignedTimeFlip() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User testUser = userService.loadUser("user45");
        Assert.assertNotNull("User45 could not be loaded from test data source", testUser);

        Assert.assertEquals("Did not found all User with TimeFlip assigned.", testUser, userService.findUserByTimeFlip(testUser.getTimeFlip()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllUserInRoom() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all User in Room 1.", 5, userService.findUserInRoom(roomService.loadRoom(1)).size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllUserInDepartment() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all User in Department 1.", 10, userService.findUserInDepartment(departmentService.loadDepartment("1")).size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAllUserInTeam() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all User in Team 1.", 5, userService.findUserInTeam(teamService.loadTeam("Team 1")).size());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFillAvailableUserList() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Did not found all available User.", 52, userService.getAvailableUser().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testValidateUsername() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertTrue("admin was not found in database", userService.validateUserName("admin"));
        Assert.assertTrue("username is not empty", userService.validateUserName(""));
    }
}
