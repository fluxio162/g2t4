package TimeManager.ui.controllers;

import TimeManager.model.Department;
import TimeManager.services.DepartmentService;
import TimeManager.services.TeamService;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the department detail view.
 *
 */
@Component
@Scope("view")
public class DepartmentDetailController implements Serializable {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    private List<String> availableUser;
    private String selectedUser;
    private String currentUser;

    private List<String> availableTeams;
    private List<String> departmentTeams;

    public DepartmentDetailController(){}

    public DepartmentDetailController(DepartmentService departmentService, UserService userService, TeamService teamService){
        this.departmentService = departmentService;
        this.userService = userService;
        this.teamService = teamService;
    }

    /**
     * Attribute to cache the currently displayed department
     */
    private Department department;

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
        if(department.getDepartmentManager() != null){
            currentUser = department.getDepartmentManager().getUsername();
            return currentUser;
        }
        else{
            return "Kein Abteilungsleiter";
        }
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * creates a list of all team which do not have any department
     *
     * @return availableTeams
     */
    public List<String> getAvailableTeams() {
        List<String> availableTeams = new ArrayList<>();
        teamService.getAllTeams().forEach(t -> availableTeams.add(t.getTeamName()));
        return availableTeams;
    }

    public void setAvailableTeams(List<String> availableTeams) {
        this.availableTeams = availableTeams;
    }

    public List<String> getDepartmentTeams() {
        List<String> departmentTeams = new ArrayList<>();
        department.getTeam().forEach(t -> departmentTeams.add(t.getTeamName()));
        return departmentTeams;
    }

    public void setDepartmentTeams(List<String> departmentTeams) {
        this.departmentTeams = departmentTeams;
    }

    /**
     * Sets the currently displayed department and reloads it form db. This department is
     * targeted by any further calls of
     * {@link #doReloadDepartment()}, {@link #doSaveDepartment()} and
     * {@link #doDeleteDepartment()}.
     *
     * @param department the department
     */
    public void setDepartment(Department department) {
        this.department = department;
        doReloadDepartment();
    }

    /**
     * Returns the currently displayed department.
     *
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Action to force a reload of the currently displayed department.
     */
    public void doReloadDepartment() {
        department = departmentService.loadDepartment(department.getDepartmentName());
    }

    /**
     * Action to update the currently displayed department.
     */
    public void doSaveDepartment() {
        department = departmentService.updateDepartment(department, selectedUser, departmentTeams);
    }

    /**
     * Action to delete the currently displayed department.
     */
    public void doDeleteDepartment() {
        this.departmentService.deleteDepartment(department);
        department = null;
    }
}
