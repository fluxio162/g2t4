package TimeManager.services;

import TimeManager.model.Team;
import TimeManager.model.User;
import TimeManager.model.UserRole;
import TimeManager.repositories.TeamRepository;
import TimeManager.repositories.UserRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Service for accessing and manipulating team data.
 */
@Component
@Scope("application")
public class TeamService {

    private static final Logger logger = Logger.getLogger(TeamService.class);

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Find team by a specific team leader.
     *
     * @param teamLeader the team leader
     * @return the team
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEAM_LEADER')")
    public Team findTeamByTeamLeader(User teamLeader){
        return teamRepository.findByTeamLeader(teamLeader);
    }

    /**
     * Returns a collection of all teams.
     *
     * @return all teams
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Team> getAllTeams() {
        return teamRepository.findAll();
    }


    /**
     * Find team from a specific User.
     *
     * @param teamMember the User
     * @return the list of teams with User
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Team> getTeamsByTeamMember(User teamMember){
        return teamRepository.findTeamsByTeamMember(teamMember);
    }


    /**
     * Loads a single team identified by its teamId.
     *
     * @param teamName the team name
     * @return the team with the given teamId
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEAMLEADER')")
    public Team loadTeam(String teamName) {
        return teamRepository.findFirstByTeamName(teamName);
    }

    /**
     * Saves the team. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param team the team to save
     * @return the updated team
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Team saveTeam(Team team) {
        String message;
        if (team.isNew()) {
            team.setCreateDate(new Date());
            team.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            team.setUpdateDate(new Date());
            team.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        logger.info(message + ": Team " + team.getTeamName() + " by User " + getAuthenticatedUser().getUsername());
        return teamRepository.save(team);
    }

    /**
     * Removes team leader from Team.
     * Removes all User from Team.
     * Removes Team from all User in Team
     * Removes Room from Team.
     * Disables Team.
     *
     * @param team the team to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTeam(Team team) {
        team.setTeamLeader(null);
        team = nullTeamMember(team);
        team.setRoom(null);

        team.setDeleteUser(getAuthenticatedUser());
        team.setDeleteDate(new Date());
        team.setEnabled(false);
        teamRepository.save(team);

        logger.info("Deleted: Team " + team.getTeamName() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * Creates a new Team with input from TeamCreationController.
     *
     * @param team the team to create
     * @param selectedUser the team leader
     * @return the team
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Team createTeam(Team team, String selectedUser){

        if(team.getTeamName() == null || team.getTeamName().isEmpty()){
            return null;
        }


        if(teamRepository.findFirstByTeamName(team.getTeamName()) != null){
            return null;
        }

        if(selectedUser != null){
            updateTeamLeader(selectedUser);
            User teamLeader = userRepository.findFirstByUsername(selectedUser);
            if(teamRepository.findByTeamLeader(teamLeader) != null){
                Team oldTeam = teamRepository.findByTeamLeader(teamLeader);
                oldTeam.setTeamLeader(null);
                saveTeam(oldTeam);
            }
            team.setTeamLeader(teamLeader);
        }

        team.setEnabled(true);
        saveTeam(team);

        if(selectedUser != null){
            User teamLeader = userRepository.findFirstByUsername(selectedUser);
            teamLeader.setTeam(team);
            userRepository.save(teamLeader);
        }
        return team;
    }

    /**
     * Updates a Team with input from TeamDetailController.
     *
     * @param team the team to update
     * @param selectedUser the new team leader
     * @return the team
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Team updateTeam(Team team, String selectedUser){
        if(!(selectedUser.isEmpty())){
            updateTeamLeader(selectedUser);
            User teamLeader = userRepository.findFirstByUsername(selectedUser);
            if(teamRepository.findByTeamLeader(teamLeader) != null){
                Team oldTeam = teamRepository.findByTeamLeader(userRepository.findFirstByUsername(selectedUser));
                oldTeam.setTeamLeader(null);
                saveTeam(oldTeam);
            }
            teamLeader.setTeam(team);
            userRepository.save(teamLeader);
            team.setTeamLeader(teamLeader);
        }
        return saveTeam(team);
    }

    /**
     * Validates the entered team name in TeamCreationController and updates save button on team_management.xhtml newTeamDialog.
     *
     * @param teamName the team name to validate
     * @return false enables save button
     */
    public boolean validateTeamName(String teamName){
        if (teamRepository.findFirstByTeamName(teamName) != null) {
            return true;
        }

        return teamName.isEmpty();
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes Team from all User in Team.
     * Removes all User from Team.
     *
     * @param team the team
     * @return the team without User
     */
    private Team nullTeamMember(Team team){
        try{
            List<User> userList = userRepository.findUserInTeam(team);
            for(User itUser : userList){
                itUser.setTeam(null);
                userRepository.save(itUser);
                logger.info("Removed: User " + itUser.getUsername() + " from Team " + team.getTeamName() + " by User " + getAuthenticatedUser().getUsername());
            }
        } catch (NullPointerException e){
            team.setTeamMember(new HashSet<>());
            return team;
        }
        team.setTeamMember(new HashSet<>());
        return team;
    }

    /**
     * Returns a list with all TeamNames as String.
     * @return list of all teamNames
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAvailableTeam(){
        List<String> availableTeams = new ArrayList<>();
        teamRepository.findAll().forEach(t -> availableTeams.add(t.getTeamName()));
        return availableTeams;
    }

    /**
     * Find a Team by TeamName
     *
     * @param teamName the teamName
     * @return the team
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TEAMLEADER') or hasAuthority('DEPARTMENT_MANAGER')")
    public Team findTeamByTeamName(String teamName){ return teamRepository.findTeamByTeamName(teamName);}

    /**
     * Updates the new assigned TeamLeader:
     *
     * Sets UserRole to TEAM_LEADER.
     *
     * @param teamLeader the teamLeader to update
     */
    private void updateTeamLeader(String teamLeader){
        User user = userRepository.findFirstByUsername(teamLeader);
        if(user.getRoles().contains(UserRole.EMPLOYEE)){
            user.setRoles(Collections.singleton(UserRole.TEAM_LEADER));
            userRepository.save(user);
            logger.info("Edited: User " + user.getUsername() + " by " + getAuthenticatedUser().getUsername());
        }
        if(!user.getRoles().contains(UserRole.TEAM_LEADER)){
            Set<UserRole> roles = new HashSet<>(user.getRoles());
            roles.add(UserRole.TEAM_LEADER);
            user.setRoles(roles);
            userRepository.save(user);
            logger.info("Edited: User " + user.getUsername() + " by " + getAuthenticatedUser().getUsername());
        }
    }
}
