package TimeManager.ui.controllers;

import TimeManager.services.*;
import TimeManager.model.User;
import TimeManager.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 * Controller to create new User.
 */
@Component
@Scope("view")
public class UserCreationController implements Serializable {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TimeFlipService timeFlipService;

    @Autowired
    private RoomService roomService;


    private User user = new User();
    private boolean disableSaveButton = true;
    private boolean unvalidatedUsername = true;
    private boolean unvalidatedPassword = true;

    private List<String> availableRolesString;
    private List<String> userRolesString;

    private List<String> availableTeams;
    private String selectedTeam;
    private String currentTeam;

    private List<String> availableDepartments;
    private String selectedDepartment;
    private String currentDepartment;

    private List<String> availableRooms;
    private String selectedRoom;
    private String currentRoom;

    private List<Integer> availableTimeFlips;
    private Integer selectedTimeFlip;

    /**
     * Instantiates a new controller.
     */
    public UserCreationController(){
    }

    /**
     * Instantiates a new controller.
     *
     * @param userService       the user service
     * @param teamService       the team service
     * @param departmentService the department service
     * @param timeFlipService   the time flip service
     * @param roomService       the room service
     */
    public UserCreationController(UserService userService, TeamService teamService, DepartmentService departmentService, TimeFlipService timeFlipService, RoomService roomService){
        this.userService = userService;
        this.teamService = teamService;
        this.departmentService = departmentService;
        this.timeFlipService = timeFlipService;
        this.roomService = roomService;
    }

    /**
     * Initializes available roles, teams, departments, rooms and time flips.
     *
     */
    @PostConstruct
    public void init() {
        List<UserRole> availableRoles = new ArrayList<>(Arrays.asList(UserRole.values()));
        availableRolesString = new ArrayList<>();
        availableRoles.forEach(r -> availableRolesString.add(r.toString()));

        availableTeams = teamService.getAvailableTeam();

        availableDepartments = departmentService.getAvailableDepartments();

        availableRooms = roomService.getAvailableRooms();

        availableTimeFlips = timeFlipService.getAvailableTimeFlips();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    public List<String> getAvailableRolesString() {
        return availableRolesString;
    }

    public void setAvailableRolesString(List<String> availableRolesString) {
        this.availableRolesString = availableRolesString;
    }

    public List<String> getUserRolesString() {
        return userRolesString;
    }

    public List<String> getAvailableTeams() {
        return availableTeams;
    }

    public void setAvailableTeams(List<String> availableTeams) {
        this.availableTeams = availableTeams;
    }

    public String getSelectedTeam() {
        return currentTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    /**
     * Gets team name of currently displayed user if user has a team.
     *
     * @return the name of the user's team
     */
    public String getCurrentTeam() {
        if (user.getTeam() != null) {
            return user.getTeam().getTeamName();
        } else {
            return "Kein Team";
        }
    }

    public List<String> getAvailableDepartments() {
        return availableDepartments;
    }

    public void setAvailableDepartments(List<String> availableDepartments) {
        this.availableDepartments = availableDepartments;
    }

    public String getSelectedDepartment() {
        return currentDepartment;
    }

    public void setSelectedDepartment(String selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    /**
     * Gets department name of currently displayed user if user has a department.
     *
     * @return the name of the user's department
     */
    public String getCurrentDepartment() {
        if (user.getDepartment() != null) {
            return user.getDepartment().getDepartmentName();
        } else {
            return "Keine Abteilung";
        }
    }

    public void setCurrentDepartment(String currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public List<String> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<String> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getSelectedRoom() {
        return currentRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    /**
     * Gets room name of currently displayed user if user has a room.
     *
     * @return the name of the user's room
     */
    public String getCurrentRoom() {
        if (user.getRoom() != null) {
            return user.getRoom().toString();
        } else {
            return "Kein Raum";
        }
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void setCurrentTeam(String currentTeam) {
        this.currentTeam = currentTeam;
    }

    public void setUserRolesString(List<String> userRolesString) {
        this.userRolesString = userRolesString;
    }

    public List<Integer> getAvailableTimeFlips() {
        return availableTimeFlips;
    }

    public void setAvailableTimeFlips(List<Integer> availableTimeFlips) {
        this.availableTimeFlips = availableTimeFlips;
    }

    public Integer getSelectedTimeFlip() {
        return selectedTimeFlip;
    }

    public void setSelectedTimeFlip(Integer selectedTimeFlip) {
        this.selectedTimeFlip = selectedTimeFlip;
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
        userService.createUser(user, userRolesString, selectedTeam, selectedDepartment, selectedRoom, selectedTimeFlip);
    }

    /**
     * Enables save button on user_management.xhtml if entered username is unique in database.
     *
     */
    public void validateUsername() {
        FacesContext context = FacesContext.getCurrentInstance();
        unvalidatedUsername = userService.validateUserName(user.getUsername());
        if (unvalidatedUsername){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehlgeschlagen:", "Benutzername ist ungültig."));
            disableSaveButton = true;
        }
        else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg:", "Benutzername ist gültig."));
            if(!unvalidatedPassword){
                disableSaveButton = false;
            }
        }

    }

    /**
     * Enables save button on user_management.xhtml if a password is entered.
     *
     */
    public void validatePassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(this.user.getPassword().isEmpty()){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehlgeschlagen:", "Passwort ist ungültig."));
            disableSaveButton = true;
        }
        else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg:", "Passwort ist gültig."));
            unvalidatedPassword = false;
            if(!unvalidatedUsername){
                disableSaveButton = false;
            }
        }

    }
}
