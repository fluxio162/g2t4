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
 * Controller for the team detail view.
 *
 */
@Component
@Scope("view")
public class TeamDetailController implements Serializable {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    private List<String> availableUser;
    private String selectedUser;
    private String currentUser;

    /**
     * Attribute to cache the currently displayed team
     */
    private Team team;

    public TeamDetailController(){}

    public TeamDetailController(TeamService teamService, UserService userService){
        this.teamService = teamService;
        this.userService = userService;
    }

    /**
     * Fill availableUser list with username of all available user.
     */
    @PostConstruct
    public void init(){
        availableUser = userService.getAvailableUser();
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

    public String getCurrentUser() {
        if(team.getTeamLeader() != null){
            currentUser = team.getTeamLeader().getUsername();
            return currentUser;
        }
        else{
            return "Kein Teamleiter";
        }
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Sets the currently displayed team and reloads it form db. This team is
     * targeted by any further calls of
     * {@link #doReloadTeam()}, {@link #doSaveTeam()} and
     * {@link #doDeleteTeam()}.
     *
     * @param team the team
     */
    public void setTeam(Team team) {
        this.team = team;
        doReloadTeam();
    }

    /**
     * Returns the currently displayed team.
     *
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Action to force a reload of the currently displayed team.
     */
    public void doReloadTeam() {
        team = teamService.loadTeam(team.getTeamName());
    }

    /**
     * Action to save the currently displayed team.
     */
    public void doSaveTeam() {
        team = this.teamService.updateTeam(team, selectedUser);
    }

    public void save(Team team){
        this.teamService.saveTeam(team);
    }

    /**
     * Action to delete the currently displayed Team.
     */
    public void doDeleteTeam() {
        this.teamService.deleteTeam(team);
        team = null;
    }
}
