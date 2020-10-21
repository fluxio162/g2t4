package TimeManager.ui.controllers;

import TimeManager.model.Team;
import TimeManager.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * Controller for the team list view.
 *
 */
@Component
@Scope("view")
public class TeamListController implements Serializable {

    @Autowired
    private TeamService teamService;
    private Collection<Team> filteredTeams;

    /**
     * Returns a list of all teams.
     *
     * @return list of all teams
     */
    public Collection<Team> getTeams() {
        return teamService.getAllTeams();
    }

    public Collection<Team> getFilteredTeams(){
        return this.filteredTeams;
    }

    public void setFilteredTeams(Collection<Team> filteredTeams){
       this.filteredTeams = filteredTeams;
    }
}
