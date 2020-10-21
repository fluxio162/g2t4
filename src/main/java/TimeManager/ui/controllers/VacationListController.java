package TimeManager.ui.controllers;

import TimeManager.model.*;
import TimeManager.services.VacationService;
import TimeManager.services.DepartmentService;
import TimeManager.services.TeamService;
import TimeManager.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


@Component
@Scope("view")
public class VacationListController {

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DepartmentService departmentService;

    private Collection<User> filteredVacations;

    public Collection<User> getFilteredVacations() {
        return filteredVacations;
    }

    public void setFilteredVacations(Collection<User> filteredVacations) {
        this.filteredVacations = filteredVacations;
    }

    public VacationListController(){}

    public VacationListController(SessionInfoBean sessionInfoBean, VacationService vacationService, TeamService teamService, DepartmentService departmentService) {
        this.sessionInfoBean = sessionInfoBean;
        this.vacationService = vacationService;
        this.teamService = teamService;
        this.departmentService = departmentService;
    }

    /**
     * Returns a list of all vacations.
     *
     * @return list of all vacation
     */
    public Collection<Vacation> getVacation() {
        return vacationService.getAllFromUser(sessionInfoBean.getCurrentUser());
    }


    /**
     * Creates a list of vacation of a employee
     *
     * @return employeeVacation all Vacation of one employee
     */
    public Collection<Vacation> getEmployeeVacation() {
        Collection<Vacation> employeeVacations = new ArrayList<>();
        if (sessionInfoBean.getCurrentUser().getRoles().contains(UserRole.TEAM_LEADER)) {
            Team team = teamService.findTeamByTeamLeader(sessionInfoBean.getCurrentUser());
            if (team != null) {
                for (User u : team.getTeamMember()) {
                    for (Vacation v : vacationService.getAllFromUser(u)) {
                        if (v.getStatus().equals("Ausstehend")) {
                            employeeVacations.add(v);
                        }
                    }
                }
            }
        }
        if (sessionInfoBean.getCurrentUser().getRoles().contains(UserRole.DEPARTMENT_MANAGER)) {
            Department department = departmentService.findDepartmentByDepartmentManager(sessionInfoBean.getCurrentUser());
            if (department != null) {
                Set<Team> teams = department.getTeam();
                if (teams != null) {
                    for (Team team : teams) {
                        for (User u : team.getTeamMember()) {
                            for (Vacation v : vacationService.getAllFromUser(u)) {
                                if (v.getStatus().equals("Ausstehend")) {
                                    employeeVacations.add(v);
                                }
                            }
                        }
                    }
                }
            }
        }
        return employeeVacations;
    }
}
