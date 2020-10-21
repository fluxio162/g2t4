package TimeManager.services;

import TimeManager.model.Department;
import TimeManager.model.Team;
import TimeManager.model.User;
import TimeManager.model.UserRole;
import TimeManager.repositories.DepartmentRepository;
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
 * Service for accessing and manipulating department data.
 */
@Component
@Scope("application")
public class DepartmentService {

    private static final Logger logger = Logger.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Returns a collection of all departments.
     *
     * @return all departments
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Finds a department by department manager.
     *
     * @param departmentManager the department manager
     * @return the department
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('DEPARTMENT_MANAGER')")
    public Department findDepartmentByDepartmentManager(User departmentManager){
        return departmentRepository.findByDepartmentManager(departmentManager);
    }

    /**
     * Loads a single department identified by its departmentId.
     *
     * @param departmentName the department name
     * @return the department with the given departmentId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Department loadDepartment(String departmentName) {
        return departmentRepository.findFirstByDepartmentName(departmentName);
    }

    /**
     * Saves the department. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param department the department to save
     * @return the updated department
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Department saveDepartment(Department department) {
        String message;
        if (department.isNew()) {
            department.setCreateDate(new Date());
            department.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            department.setUpdateDate(new Date());
            department.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        logger.info(message + ": Department " + department.getDepartmentName() + " by " + getAuthenticatedUser().getUsername());
        return departmentRepository.save(department);
    }

    /**
     * Removes departmentManager from Department.
     * Removes all User from Department.
     * Removes all Teams from Department.
     * Disables Department.
     *
     * @param department the department to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDepartment(Department department) {
        department.setDepartmentManager(null);
        department = nullTeam(department);
        nullUser(department);

        department.setDeleteUser(getAuthenticatedUser());
        department.setDeleteDate(new Date());
        department.setEnabled(false);
        departmentRepository.save(department);


        logger.info("Deleted: Department " + department.getDepartmentName() + " by " + getAuthenticatedUser().getUsername());
    }

    /**
     * Validates input from DepartmentCreationController and creates new Department.
     *
     * @param department the department to create
     * @param departmentManager the department manager
     * @return the department
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Department createDepartment(Department department, String departmentManager) {

        if(department.getDepartmentName() == null || department.getDepartmentName().isEmpty()){
            return null;
        }


        if(departmentRepository.findFirstByDepartmentName(department.getDepartmentName()) != null){
            return null;
        }

        if(departmentManager != null){
            updateDepartmentManager(departmentManager);

            if(findDepartmentByDepartmentManager(userRepository.findFirstByUsername(departmentManager)) != null){
                Department oldDepartment = findDepartmentByDepartmentManager(userRepository.findFirstByUsername(departmentManager));
                oldDepartment.setDepartmentManager(null);
                saveDepartment(oldDepartment);
            }
            department.setDepartmentManager(userRepository.findFirstByUsername(departmentManager));
        }

        department.setEnabled(true);
        return saveDepartment(department);
    }

    /**
     * Validates input from DepartmentDetailController and updates the Department.
     *
     * @param teams the updated list of teams
     * @param department the department to update
     * @param departmentManager the new department manager
     * @return the department
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Department updateDepartment(Department department, String departmentManager, List<String> teams){
        if(!(departmentManager.isEmpty())){
            updateDepartmentManager(departmentManager);

            if(departmentRepository.findByDepartmentManager(userRepository.findFirstByUsername(departmentManager)) != null){
                Department oldDepartment = departmentRepository.findByDepartmentManager(userRepository.findFirstByUsername(departmentManager));
                oldDepartment.setDepartmentManager(null);
                departmentRepository.save(oldDepartment);
            }
            department.setDepartmentManager(userRepository.findFirstByUsername(departmentManager));
        }

        Set<Team> departmentTeams = new HashSet<>(department.getTeam());

        for(Team oldTeam : departmentTeams){
            if(!teams.contains(oldTeam.getTeamName())){
                departmentTeams.remove(oldTeam);
                for(User oldUser : userRepository.findUserInTeam(oldTeam)){
                    oldUser.setDepartment(null);
                    userRepository.save(oldUser);
                    logger.info("Edited: User " + oldUser.getUsername() + " by " + getAuthenticatedUser().getUsername());
                }
            }
        }


        for(String teamString : teams){
            Team team = teamRepository.findFirstByTeamName(teamString);
            if(!department.getTeam().contains(team)){
                departmentTeams.add(team);
                for(User newUser : userRepository.findUserInTeam(team)){
                    newUser.setDepartment(department);
                    userRepository.save(newUser);
                    logger.info("Edited: User " + newUser.getUsername() + " by " + getAuthenticatedUser().getUsername());
                }
                if(departmentRepository.findByTeam(team) != null){
                    Department oldDepartment = departmentRepository.findByTeam(team);
                    Set<Team> oldDepartmentTeams = new HashSet<>(oldDepartment.getTeam());
                    oldDepartmentTeams.remove(team);
                    oldDepartment.setTeam(oldDepartmentTeams);
                    departmentRepository.save(oldDepartment);
                    logger.info("Edited: Department " + oldDepartment.getDepartmentName() + " by " + getAuthenticatedUser().getUsername());
                }
            }
        }

        department.setTeam(departmentTeams);
        return saveDepartment(department);
    }

    /**
     * Validates the entered department name in DepartmentCreationController and updates save button on department_management.xhtml newDepartmentDialog.
     *
     * @param departmentName the department name to validate
     * @return false enables save button
     */
    public boolean validateDepartmentName(String departmentName){
        if (departmentRepository.findFirstByDepartmentName(departmentName) != null) {
            return true;
        }

        return departmentName.isEmpty();
    }


    /**
     * get the current user
     * @return authenticatedUser
     */
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes all User from a Department by updated {@link User#department} to null.
     *
     * @param department the department
     */
    private void nullUser(Department department) {
        List<User> userList = new ArrayList<>();
        userList.addAll(userRepository.findUserInDepartment(department));
        for (User itUser : userList) {
            itUser.setDepartment(null);
            userRepository.save(itUser);
            logger.info("Removed: User " + itUser.getUsername() + " from Department " + department.getDepartmentName() + " by User " + getAuthenticatedUser().getUsername());
        }
    }

    /**
     * Removes all Teams from a Department.
     *
     * @param department the department
     */
    private Department nullTeam(Department department) {
        Set<Team> teamList = new HashSet<>();
        teamList.addAll(department.getTeam());
        for(Team itTeam : teamList){
            logger.info("Removed: Team " + itTeam.getTeamName() + " from Department " + department.getDepartmentName() + " by User " + getAuthenticatedUser().getUsername());
        }
        department.setTeam(new HashSet<>());
        return department;
    }

    /**
     * Fill availableDepartments list with department name of all Departments.
     *
     * @return list of available departments
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAvailableDepartments(){
        List<String> availableDepartments = new ArrayList<>();
        departmentRepository.findAll().forEach(d -> availableDepartments.add(d.getDepartmentName()));
        return availableDepartments;
    }

    /**
     * updates the department manager
     * only updates if the new department manager is not already a department manager
     * @param departmentManager new department manager
     */
    private void updateDepartmentManager(String departmentManager){
        User user = userRepository.findFirstByUsername(departmentManager);
        if(user.getRoles().contains(UserRole.EMPLOYEE)){
            user.setRoles(Collections.singleton(UserRole.DEPARTMENT_MANAGER));
            userRepository.save(user);
            logger.info("Edited: User " + user.getUsername() + " by " + getAuthenticatedUser().getUsername());
        }
        if(!user.getRoles().contains(UserRole.DEPARTMENT_MANAGER)){
            Set<UserRole> roles = new HashSet<>(user.getRoles());
            roles.add(UserRole.DEPARTMENT_MANAGER);
            user.setRoles(roles);
            userRepository.save(user);
            logger.info("Edited: User " + user.getUsername() + " by " + getAuthenticatedUser().getUsername());
        }
    }
}
