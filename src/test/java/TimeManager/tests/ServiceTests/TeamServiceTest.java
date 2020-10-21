package TimeManager.tests.ServiceTests;

import TimeManager.model.Team;
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

/**
 * Some very basic tests for {@link TeamService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TeamServiceTest {

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
    public void testDataInitialization() {
        Assert.assertEquals("Insufficient amount of Teams initialized for test data source", 5, teamService.getAllTeams().size());
        for (Team team : teamService.getAllTeams()) {
            if ("Team 1".equals(team.getTeamName())) {
                Assert.assertNotNull("Team 1 does not have a createUser defined", team.getCreateUser());
                Assert.assertNotNull("Team 1 does not have a createDate defined", team.getCreateDate());
                Assert.assertNull("Team 1 has a updateUser defined", team.getUpdateUser());
                Assert.assertNull("Team 1 has a updateDate defined", team.getUpdateDate());
            } else if ("Team 3".equals(team.getTeamName())) {
                Assert.assertNotNull("Team 3 does not have a createUser defined", team.getCreateUser());
                Assert.assertNotNull("Team 3 does not have a createDate defined", team.getCreateDate());
                Assert.assertNull("Team 3 has a updateUser defined", team.getUpdateUser());
                Assert.assertNull("Team 3 has a updateDate defined", team.getUpdateDate());
            } else if ("Team 5".equals(team.getTeamName())) {
                Assert.assertNotNull("Team 3 does not have a createUser defined", team.getCreateUser());
                Assert.assertNotNull("Team 3 does not have a createDate defined", team.getCreateDate());
                Assert.assertNull("Team 3 has a updateUser defined", team.getUpdateUser());
                Assert.assertNull("Team 3 has a updateDate defined", team.getUpdateDate());
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteTeam() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Team toBeDeletedTeam = teamService.loadTeam("Team 5");
        Assert.assertNotNull("Team 5 could not be loaded from test data source", toBeDeletedTeam);

        teamService.deleteTeam(toBeDeletedTeam);

        Assert.assertEquals("No Team has been deleted after calling TeamService.deleteTeam", 4, teamService.getAllTeams().size());
        Assert.assertNull("Team 5 has still TeamLeader assigned.", toBeDeletedTeam.getTeamLeader());
        Assert.assertTrue("Team 5has still TeamMember assigned", toBeDeletedTeam.getTeamMember().isEmpty());
        Assert.assertNull("Team 5 is still in Room 5", toBeDeletedTeam.getRoom());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedTeam.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedTeam.getDeleteDate());
        Assert.assertFalse("Team 6 is still enabled", toBeDeletedTeam.isEnabled());

        for (Team remainingTeam : teamService.getAllTeams()) {
            Assert.assertNotEquals("Deleted Team 5 could still be loaded from test data source via TeamService.getAllTeams", toBeDeletedTeam.getTeamName(), remainingTeam.getTeamName());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateTeam() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Team toBeUpdatedTeam = teamService.loadTeam("Team 4");
        Assert.assertNotNull("Team a4 could not be loaded from test data source", toBeUpdatedTeam);

        Assert.assertNull("Team a4 has a updateUser defined", toBeUpdatedTeam.getUpdateUser());
        Assert.assertNull("Team 4 has a updateDate defined", toBeUpdatedTeam.getUpdateDate());

        teamService.updateTeam(toBeUpdatedTeam, "user032");

        Team freshlyLoadedTeam = teamService.loadTeam("Team 4");
        Assert.assertNotNull("Team 4 could not be loaded from test data source after being saved", freshlyLoadedTeam);
        Assert.assertNotNull("Team 4 does not have a updateUser defined after being saved", freshlyLoadedTeam.getUpdateUser());
        Assert.assertEquals("Team 4 has wrong updateUser set", adminUser, freshlyLoadedTeam.getUpdateUser());
        Assert.assertNotNull("Team 4 does not have a updateDate defined after being saved", freshlyLoadedTeam.getUpdateDate());
        Assert.assertEquals("Team 4 has wrong TeamLeader assigned.", "user032", freshlyLoadedTeam.getTeamLeader().getUsername());
        Assert.assertNull("User032 is still assigned to Team 3.", teamService.loadTeam("Team 3").getTeamLeader());
        Assert.assertEquals("User032 is not assigned to Team 4.", freshlyLoadedTeam, userService.loadUser("user032").getTeam());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateTeam() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Team toBeCreatedTeam = new Team();
        toBeCreatedTeam.setTeamName("Team Test");

        teamService.createTeam(toBeCreatedTeam, "user034");

        Team freshlyCreatedTeam = teamService.loadTeam("Team Test");
        Assert.assertNotNull("New Team could not be loaded from test data source after being saved", freshlyCreatedTeam);
        Assert.assertNotNull("Team Test does not have a createUser defined after being saved", freshlyCreatedTeam.getCreateUser());
        Assert.assertEquals("Team Test has wrong createUser set", adminUser, freshlyCreatedTeam.getCreateUser());
        Assert.assertNotNull("Team Test does not have a createDate defined after being saved", freshlyCreatedTeam.getCreateDate());
        Assert.assertEquals("Team 10 has wrong team leader assigned.", "user034", freshlyCreatedTeam.getTeamLeader().getUsername());
        Assert.assertTrue("Team Test is not enabled.", freshlyCreatedTeam.isEnabled());
        Assert.assertEquals("User034 is still assigned to Team 5", freshlyCreatedTeam, userService.loadUser("user034").getTeam());
        Assert.assertNull("User034 is still assigned to Team 5", teamService.loadTeam("Team 5").getTeamLeader());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserWithEmptyTeamName() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Team toBeCreatedTeam = new Team();
        teamService.createTeam(toBeCreatedTeam, null);
        Assert.assertNull("Team could be loaded from database", teamService.loadTeam(toBeCreatedTeam.getTeamName()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingUserAlreadyExistingTeamName() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);


        Team toBeCreatedTeam = new Team();
        toBeCreatedTeam.setTeamName("Team 1");

        teamService.createTeam(toBeCreatedTeam, "user64");

        Assert.assertEquals("Team 1 team leader has changed.", "user030", teamService.loadTeam("Team 1").getTeamLeader().getUsername());
    }


    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadTeam() {
        for (Team team : teamService.getAllTeams()) {
            Assert.fail("Call to TeamService.getAllTeams should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadTeams() {
        for (Team team : teamService.getAllTeams()) {
            Assert.fail("Call to TeamService.getAllTeams should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadTeam() {
        Team team = teamService.loadTeam("Team 1");
        Assert.fail("Call to TeamService.loadTeam should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadTeam() {
        Team team = teamService.loadTeam("Team 1");
        Assert.assertEquals("Call to TeamService.loadTeam returned wrong Team", "Team 1", team.getTeamName());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveTeam() {
        teamService.saveTeam(new Team());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteTeam() {
        teamService.deleteTeam(new Team());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateTeam() {
        teamService.createTeam(new Team(), null);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindTeamByTeamLeader() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong Team returned.", "Team 1", teamService.findTeamByTeamLeader(userService.loadUser("user030")).getTeamName());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindTeamByTeamMember() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);


        Team team = teamService.loadTeam("Team 1");
        Assert.assertTrue("User64 is not in Team 1", teamService.getTeamsByTeamMember(userService.loadUser("user64")).contains(team));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAvailableTeams() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong Team returned.", 5, teamService.getAvailableTeam().size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindTeamByTeamName() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong Team returned.", "Team 1", teamService.findTeamByTeamName("Team 1").getTeamName());
    }


    @Test
    public void testValidateDepartmentName(){
        Assert.assertTrue("Empty TeamName is valid.", teamService.validateTeamName(""));
        Assert.assertTrue("Already used TeamName is valid.", teamService.validateTeamName("Team 1"));
        Assert.assertFalse("Valid TeamName is not valid.", teamService.validateTeamName("Team 91"));
    }
}
