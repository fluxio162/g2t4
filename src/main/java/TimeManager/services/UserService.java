package TimeManager.services;

import TimeManager.model.*;
import TimeManager.repositories.*;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MiniserverRepository miniserverRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TimeFlipRepository timeFlipRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MiniserverService miniserverService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TimeFlipService timeFlipService;

    /**
     * Returns a collection of all users.
     *
     * @return all users
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find User with TimeFlip assigned.
     *
     * @return the list of User with TimeFlip
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findUserWithTimeFlip(){
        return userRepository.findUserWithTimeFlip();
    }


    /**
     * Find User with specific TimeFlip assigned.
     *
     * @param timeFlip the timeFlip
     * @return the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findUserByTimeFlip(TimeFlip timeFlip){
        return userRepository.findUserByTimeFlip(timeFlip);
    }

    /**
     * Find User in specific Room.
     *
     * @param room the room
     * @return the list of all user in room
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findUserInRoom(Room room){
        return userRepository.findUserInRoom(room);
    }

    /**
     * Find all User in specific Team.
     *
     * @param team the team
     * @return the list of all user in team
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findUserInTeam(Team team){
        return userRepository.findUserInTeam(team);
    }

    /**
     * Find all User in specific Department
     *
     * @param department the department
     * @return the list of all user in department
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findUserInDepartment(Department department){
        return userRepository.findUserInDepartment(department);
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public User loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User saveUser(User user) {
        String message;
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }

        logger.info(message + ": User " + user.getUsername() + " by User " + getAuthenticatedUser());
        return userRepository.save(user);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    public User settingsSaveUser(User user) {
        String message;
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }

        logger.info(message + ": User " + user.getUsername() + " by User " + getAuthenticatedUser());
        return userRepository.save(user);
    }

    /**
     * Removes Team from User.
     * Removes User from all Teams.
     * Removes Department from User.
     * Removes TimeFlip from User.
     * Removes User from Room.
     * Disables User.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(User user) {
        if(user.getTeam() != null){
            user = nullTeam(user);
        }

        if(user.getDepartment() != null){
            user = nullDepartment(user);
        }

        if(user.getTimeFlip() != null){
            user = nullTimeFlip(user);
        }

        user.setRoom(null);

        user.setDeleteUser(getAuthenticatedUser());
        user.setDeleteDate(new Date());
        user.setEnabled(false);
        userRepository.save(user);


        logger.info("Deleted: User " + user.getUsername() + " by User " + getAuthenticatedUser());
    }

    /**
     * Creates a new User with input from UserCreationController.
     *
     *
     * @param user the user to create
     * @param userRolesString the selected user roles
     * @param selectedTeam the selected team
     * @param selectedDepartment the selected department
     * @param selectedRoom the selected room
     * @param selectedTimeFlip the selected timeflip
     * @return the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User createUser (User user, List<String> userRolesString, String selectedTeam, String selectedDepartment, String selectedRoom, Integer selectedTimeFlip){

        boolean teamFlag = false;

        if (userRepository.findFirstByUsername(user.getUsername()) != null) {
            return null;
        }

        if (user.getUsername().isEmpty()) {
            return null;
        }

        Set<UserRole> selectedRoles = new HashSet<>();
        userRolesString.forEach(r -> selectedRoles.add(UserRole.valueOf(r)));
        user.setRoles(selectedRoles);
        if (!(selectedTeam.isEmpty())) {
            user.setTeam(teamRepository.findFirstByTeamName(selectedTeam));
            teamFlag = true;
        }
        if (!(selectedDepartment.isEmpty())) {
            user.setDepartment(departmentRepository.findFirstByDepartmentName(selectedDepartment));
        }

        if (selectedTimeFlip != null) {
            user.setTimeFlip(timeFlipRepository.findFirstByTimeFlipId(selectedTimeFlip));
        }

        if (!(selectedRoom.isEmpty())) {
            Room assignedRoom = roomRepository.findFirstByRoomId(Integer.parseInt(selectedRoom));
            user.setRoom(assignedRoom);
            if(assignedRoom.getMiniserver() != null){
                Miniserver assignedMiniserver = assignedRoom.getMiniserver();
                Set<TimeFlip> timeFlipSet = new HashSet<>(assignedMiniserver.getTimeFlip());
                timeFlipSet.add(user.getTimeFlip());
                assignedMiniserver.setTimeFlip(timeFlipSet);
                miniserverService.saveMiniserver(assignedMiniserver);
            }
        }

        user.setEnabled(true);
        user = saveUser(user);

        if(teamFlag){
            Team team = teamRepository.findFirstByTeamName(selectedTeam);
            Set<User> teamMember = new HashSet<>(team.getTeamMember());
            teamMember.add(user);
            team.setTeamMember(teamMember);
            teamService.saveTeam(team);
        }
        return user;
    }

    /**
     * Updates an User with input from UserDetailController.
     *
     * @param user the user to update
     * @param userRolesString the new user roles
     * @param selectedTeam the new team
     * @param selectedDepartment the new department
     * @param selectedTimeFlip the new timeflip
     * @param selectedRoom the new room
     * @return the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(User user, List<String> userRolesString, String selectedTeam, String selectedDepartment, Integer selectedTimeFlip, String selectedRoom){
        Set<UserRole> selectedRoles = new HashSet<>();
        userRolesString.forEach(r -> selectedRoles.add(UserRole.valueOf(r)));
        user.setRoles(selectedRoles);

        if (!(selectedTeam.isEmpty())) {
            if(user.getTeam() != null){
                HashSet<User> oldTeamMember = new HashSet<>();
                oldTeamMember.addAll(user.getTeam().getTeamMember());
                oldTeamMember.remove(user);
                Team oldTeam = teamService.loadTeam(user.getTeam().getTeamName());
                oldTeam.setTeamMember(oldTeamMember);
                teamService.saveTeam(oldTeam);
            }
            user.setTeam(teamService.loadTeam(selectedTeam));
            Team newTeam = teamService.loadTeam(selectedTeam);
            HashSet<User> newTeamMember = new HashSet<>();
            newTeamMember.addAll(user.getTeam().getTeamMember());
            newTeamMember.add(user);
            newTeam.setTeamMember(newTeamMember);
            teamService.saveTeam(newTeam);

        }
        if (!(selectedDepartment.isEmpty())) {
            user.setDepartment(departmentService.loadDepartment(selectedDepartment));
        }

        if (selectedTimeFlip != null) {
            if(userRepository.findUserByTimeFlip(timeFlipService.loadTimeFlip(selectedTimeFlip)) != null){
                User oldUser = userRepository.findUserByTimeFlip(timeFlipService.loadTimeFlip(selectedTimeFlip));
                oldUser.setTimeFlip(null);
                saveUser(oldUser);
            }
            user.setTimeFlip(timeFlipService.loadTimeFlip(selectedTimeFlip));
        }

        if(!(selectedRoom.isEmpty())){
            Room newRoom = roomService.loadRoom(Integer.parseInt(selectedRoom));
            user.setRoom(newRoom);
            if(user.getTimeFlip() != null){
                Miniserver updatedMiniserver = newRoom.getMiniserver();
                HashSet<TimeFlip> newRoomTimeFlips = new HashSet<>(newRoom.getMiniserver().getTimeFlip());
                newRoomTimeFlips.add(user.getTimeFlip());
                updatedMiniserver.setTimeFlip(newRoomTimeFlips);
                miniserverService.saveMiniserver(updatedMiniserver);
            }
        }

        return saveUser(user);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes User from all Teams and removes Team from User.
     *
     * @param user the user
     * @return the user without team
     */
    private User nullTeam(User user){
        try{
            List<Team> teamList = teamRepository.findTeamsByTeamMember(user);
            for(Team itTeam : teamList){
                Set<User> teamMember = new HashSet<>(itTeam.getTeamMember());
                teamMember.remove(user);
                itTeam.setTeamMember(teamMember);
                teamRepository.save(itTeam);
                logger.info("Removed: User " + user.getUsername() + " from Team " + itTeam.getTeamName() + " by User " + getAuthenticatedUser().getUsername());
            }
        } catch (NullPointerException e){
            user.setTeam(null);
            return user;
        }

        user.setTeam(null);
        return user;
    }

    /**
     * Removes User from Department if User is department manager.
     * Removes Department from User.
     *
     * @param user the user
     * @return the user
     */
    private User nullDepartment(User user){
        try{
            Department department = departmentRepository.findByDepartmentManager(user);
            department.setDepartmentManager(null);
            departmentRepository.save(department);
            logger.info("Removed: User " + user.getUsername() + " from Department " + department.getDepartmentName() + " by User " + getAuthenticatedUser().getUsername());
        } catch (NullPointerException e){
            user.setDepartment(null);
            return user;
        }

        user.setDepartment(null);
        return user;
    }

    /**
     * Removes TimeFlip assigned to User from Miniserver.
     * Removes TimeFlip from User.
     *
     * @param user the user
     * @return the user
     */
    private User nullTimeFlip(User user){
        try{
            TimeFlip timeFlip = user.getTimeFlip();
            Miniserver miniserver = miniserverRepository.findMiniserverWithTimeFlip(timeFlip);
            Set<TimeFlip> timeFlipSet = new HashSet<>(miniserver.getTimeFlip());
            timeFlipSet.remove(timeFlip);
            miniserver.setTimeFlip(timeFlipSet);
            miniserverRepository.save(miniserver);
            logger.info("Removed: TimeFlip " + timeFlip.getTimeFlipId() + " from Miniserver " + miniserver.getMiniserverId() + " by User " + getAuthenticatedUser().getUsername());
        } catch (NullPointerException e){
            user.setTimeFlip(null);
            return user;
        }

        user.setTimeFlip(null);
        return user;
    }


    /**
     * Fill availableUser list with username of all User.
     *
     * @return list of available user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAvailableUser(){
        List<String> availableUser = new ArrayList<>();
        userRepository.findAll().forEach(u -> availableUser.add(u.getUsername()));
        return availableUser;
    }

    /**
     * Validates the entered user name in UserCreationController and updates save button on user_management.xhtml newUserDialog.
     *
     * @param userName the user name to validate
     * @return false enables save button
     */
    public boolean validateUserName(String userName){
        if (userRepository.findFirstByUsername(userName) != null) {
            return true;
        }

        return userName.isEmpty();
    }
}
