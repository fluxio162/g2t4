package TimeManager.tests.ControllerTests;

import TimeManager.model.Team;
import TimeManager.model.Team;
import TimeManager.model.Miniserver;
import TimeManager.model.Team;
import TimeManager.services.MiniserverService;
import TimeManager.services.RoomService;
import TimeManager.services.TeamService;
import TimeManager.services.UserService;
import TimeManager.ui.controllers.*;
import TimeManager.ui.controllers.TeamDetailController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TeamControllerTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    TeamDetailController teamDetailController;

    TeamCreationController teamCreationController;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTeamDetailControllerUpdateTeam() {
        teamDetailController = new TeamDetailController(teamService, userService);


        teamDetailController.setTeam(teamService.loadTeam("Team 4"));
        teamDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, teamDetailController.getAvailableUser().size());
        Assert.assertEquals("Wrong TeamLeader returned", "user033", teamDetailController.getCurrentUser());

        teamDetailController.setSelectedUser("user034");
        teamDetailController.doSaveTeam();

        Team freshlyLoadedTeam = teamService.loadTeam("Team 4");
        Assert.assertEquals("Wrong TeamLeader assigned.", "user034", freshlyLoadedTeam.getTeamLeader().getUsername());
        Assert.assertNotNull("UpdateUser not stored.", freshlyLoadedTeam.getUpdateUser());
        Assert.assertNotNull("UpdateDate not stored.", freshlyLoadedTeam.getUpdateDate());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTeamDetailControllerDeleteTeam() {
        teamDetailController = new TeamDetailController(teamService, userService);


        teamDetailController.setTeam(teamService.loadTeam("Team 5"));
        teamDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, teamDetailController.getAvailableUser().size());
        Assert.assertEquals("Wrong TeamLeader returned", "user034", teamDetailController.getCurrentUser());


        teamDetailController.doDeleteTeam();

        Team freshlyDeletedTeam = teamService.loadTeam("Team 5");
        Assert.assertNotNull("DeleteUser not stored.", freshlyDeletedTeam.getDeleteUser());
        Assert.assertNotNull("DeleteDate not stored.", freshlyDeletedTeam.getDeleteDate());
        Assert.assertEquals("Wrong number of Teams returned.", 4, teamService.getAllTeams().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTeamCreationControllerCreateTeam() {
        teamCreationController = new TeamCreationController(teamService, userService);

        teamCreationController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, teamCreationController.getAvailableUser().size());

        Team toBeCreatedTeam = new Team();
        toBeCreatedTeam.setTeamName("Absolutely unique Team Name");

        teamCreationController.setTeam(toBeCreatedTeam);
        teamCreationController.validateTeamName();

        Assert.assertFalse("Team Name validation failed.", teamCreationController.isDisableSaveButton());

        teamCreationController.setSelectedUser("user003");
        teamCreationController.doSaveTeam();

        Team freshlyCreatedTeam = teamService.loadTeam("Absolutely unique Team Name");
        Assert.assertNotNull("New Team could not be loaded from database.", freshlyCreatedTeam);
        Assert.assertNotNull("CreateUser not stored.", freshlyCreatedTeam.getCreateUser());
        Assert.assertNotNull("CreateDate not stored.", freshlyCreatedTeam.getCreateDate());
    }

}
