package TimeManager.ui.controllers;

import TimeManager.model.Team;
import TimeManager.services.TeamService;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * Controller to create new Team.
 */
@Component
@Scope("view")
public class TeamCreationController implements Serializable {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    private Team team = new Team();

    private boolean disableSaveButton = true;

    private List<String> availableUser;
    private String selectedUser;

    public TeamCreationController(){}

    public TeamCreationController(TeamService teamService, UserService userService){
        this.teamService = teamService;
        this.userService = userService;
    }

    /**
     * Fill availableUser list with username of all available user.
     * Disables save button on team_management.xhtml
     */
    @PostConstruct
    public void init(){
        availableUser = userService.getAvailableUser();
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<String> getAvailableUser() {
        return availableUser;
    }

    public void setAvailableUser(List<String> availableUser) {
        this.availableUser = availableUser;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public boolean isDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    /**
     * Action to save the currently displayed team.
     */
    public void doSaveTeam() {
        team = teamService.createTeam(team, selectedUser);
    }

    /**
     * Enables save button on team_management.xhtml if entered team name is unique in database.
     */
    public void validateTeamName() {
        disableSaveButton = teamService.validateTeamName(team.getTeamName());
    }
}
