package TimeManager.ui.controllers;

import TimeManager.model.User;
import TimeManager.model.UserRole;
import TimeManager.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 * Controller for the user detail view.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserDetailController implements Serializable {

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

    /**
     * Instantiates a controller.
     */
    public UserDetailController(){
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
    public UserDetailController(UserService userService, TeamService teamService, DepartmentService departmentService, TimeFlipService timeFlipService, RoomService roomService){
        this.userService = userService;
        this.teamService = teamService;
        this.departmentService = departmentService;
        this.timeFlipService = timeFlipService;
        this.roomService = roomService;
    }


    /**
     * Attribute to cache the currently displayed user
     */
    private User user;

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
    private Integer currentTimeFlip;

    private static final Logger logger = Logger.getLogger(UserDetailController.class);


    /**
     * Initializes available roles, teams, departments, rooms and time flips.
     *
     */
    @PostConstruct
    public void init(){
        List<UserRole> availableRoles = new ArrayList<>(Arrays.asList(UserRole.values()));
        availableRolesString = new ArrayList<>();
        availableRoles.forEach(r -> availableRolesString.add(r.toString()));

        availableTeams = teamService.getAvailableTeam();

        availableDepartments = departmentService.getAvailableDepartments();

        availableRooms = roomService.getAvailableRooms();

        availableTimeFlips = timeFlipService.getAvailableTimeFlips();
    }

    public List<String> getAvailableRolesString() {
        return availableRolesString;
    }

    public void setAvailableRolesString(List<String> availableRolesString) {
        this.availableRolesString = availableRolesString;
    }

    /**
     * Returns a user's roles as string.
     *
     * @return the user roles as string
     */
    public List<String> getUserRolesString() {
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.addAll(user.getRoles());
        userRolesString = new ArrayList<>();
        userRoles.forEach(r -> userRolesString.add(r.toString()));
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
        if(user.getTeam() != null){
            return user.getTeam().getTeamName();
        }
        else{
            return "Kein Team";
        }
    }

    public void setCurrentTeam(String currentTeam) {
        this.currentTeam = currentTeam;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
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
        if(user.getRoom() != null){
            return user.getRoom().toString();
        }
        else{
            return "Kein Raum";
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
        if(user.getDepartment() != null){
            return user.getDepartment().getDepartmentName();
        }
        else{
            return "Keine Abteilung";
        }
    }

    public void setCurrentDepartment(String currentDepartment) {
        this.currentDepartment = currentDepartment;
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
     * Gets time flip id of currently displayed user's time flip if user has a time flip.
     *
     * @return the id of the user's time flip
     */
    public Integer getCurrentTimeFlip() {
        if(user.getTimeFlip() != null){
            return user.getTimeFlip().getTimeFlipId();
        }
        else{
            return null;
        }
    }

    public void setCurrentTimeFlip(Integer currentTimeFlip) {
        this.currentTimeFlip = currentTimeFlip;
    }

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
        user = userService.updateUser(user, userRolesString, selectedTeam, selectedDepartment, selectedTimeFlip, selectedRoom);
    }


    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        userService.deleteUser(user);
        user = null;
    }
}
