package TimeManager.services;

import TimeManager.model.Task;
import TimeManager.model.TaskCategory;
import TimeManager.model.TaskChangeRequest;
import TimeManager.model.User;
import TimeManager.repositories.TaskChangeRequestRepository;
import TimeManager.repositories.TaskRepository;
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
 * Service for accessing and manipulating task data.
 *
 */
@Component
@Scope("application")
public class TaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskChangeRequestRepository taskChangeRequestRepository;

    /**
     * Returns a collection of all tasks that are stored in the database.
     *
     * @return collection of all tasks
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Returns a collection of all taskchangerequests.
     *
     * @return
     */
    private Map<Task, TaskChangeRequest> getAllTaskChangeRequests() {
        Map<Task, TaskChangeRequest> usernameRequestMap = new HashMap<>();

        for(TaskChangeRequest request : taskChangeRequestRepository.findAll()){
            usernameRequestMap.put(taskRepository.findFirstByTaskId(request.getTaskId()), request);
        }

        return usernameRequestMap;
    }

    /**
     * Returns a collection of all taskchangerequests by the department.
     * @return a Map with all tasks requested to be changed of one department
     */
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGER')")
    public Map<Task, TaskChangeRequest> getAllTaskChangeRequestsFromDepartment() {
        Map<Task, TaskChangeRequest> usernameRequestMap = getAllTaskChangeRequests();

        for(Task task : usernameRequestMap.keySet()){
            if(!task.getCreateUser().getDepartment().getDepartmentName().equals(getAuthenticatedUser().getDepartment().getDepartmentName())){
                usernameRequestMap.remove(task);
            }
        }

        return usernameRequestMap;
    }

    /**
     * Returns a collection of all taskchangerequests by the team.
     * @return a Map with all tasks requested to be changed of one team
     */
    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    public Map<Task, TaskChangeRequest> getAllTaskChangeRequestsFromTeam() {
        Map<Task, TaskChangeRequest> usernameRequestMap = getAllTaskChangeRequests();

        for(Task task : usernameRequestMap.keySet()){
            if(!task.getCreateUser().getTeam().getTeamName().equals(getAuthenticatedUser().getTeam().getTeamName())){
                usernameRequestMap.remove(task);
            }
        }

        return usernameRequestMap;
    }

    /**
     * Loads a single task identified by its taskId.
     * @param taskId the taskId to search for
     * @return the task with the given taskId
     */
    @PreAuthorize("isAuthenticated()")
    public Task loadTask(int taskId) {
        return taskRepository.findFirstByTaskId(taskId);
    }

    /**
     * load task by user
     * @param user the authenticated user
     * @return list of task from user
     */
    @PreAuthorize("isAuthenticated()")
    public List<Task> loadTaskByUser(User user){return taskRepository.allActivitiesByUser(user);}

    /**
     * load task by category
     * @param taskCategory user admin
     * @return all activities by category
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Task> loadTaskByCategory (Set<TaskCategory> taskCategory){return taskRepository.allActivitiesByCategory(taskCategory);}

    /**
     * load all tasks between two dates
     * @param startDate the first date
     * @param endDate the second date
     * @return all tasks between startDate and endDate
     */
    public Collection<Task> getAllTasksBetweenDates(Date startDate, Date endDate){
        return taskRepository.allActivitiesBetweenDates(startDate, endDate);
    }

    /**
     * load all tasks between two dates as a list
     * @param startDate the first date
     * @param endDate the second date
     * @return list with all tasks between startDate and endDate
     */
    public List<Task> getListOfTasksBetweenDates(Date startDate, Date endDate){
        return taskRepository.allTasksBetweenDates(startDate, endDate);
    }

    /**
     *  Load tasks by team
     *
     * @param user user in team
     * @return list of task from team
     *
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Task> loadTaskByTeam(User user){return taskRepository.allActivitiesByUser(user);}

    /**
     * Saves the task. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param task the task to save
     * @return the updated task
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Task saveTask(Task task) {
        String message;
        if (task.isNew()) {
            task.setCreateDate(new Date());
            task.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            task.setUpdateDate(new Date());
            task.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        return taskRepository.save(task);
    }

    /**
     * writes the tasks to the taskRepository
     * @param task the task
     * @param user the user
     * @return the taskRepository with the handed over tasks
     */
    public Task writeTask(Task task, User user) {
        task.setEnabled(true);
        String message;
        if (task.isNew()) {
            task.setCreateDate(new Date());
            task.setCreateUser(user);
            message = "Created";
        } else {
            task.setUpdateDate(new Date());
            task.setUpdateUser(user);
            message = "Edited";
        }
        return taskRepository.save(task);
    }

    /**
     * executes the requested update
     * @param task the task
     * @param taskStart provides information about the start of the task
     * @param taskEnd provides information about the end of the task
     * @param selectedTaskCategory the selected category of tasks
     */
    @PreAuthorize("isAuthenticated()")
    public void requestUpdate(Task task, Date taskStart, Date taskEnd, String selectedTaskCategory){
        long twoWeeks = 14 * 24 * 3600 * 1000;
        if((System.currentTimeMillis() - task.getTaskStart().getTime()) > twoWeeks){
            TaskChangeRequest request = new TaskChangeRequest();
            request.setCreateDate(new Date());
            request.setCreateUser(getAuthenticatedUser());
            request.setTaskId(task.getTaskId());
            request.setTaskStart(taskStart);
            request.setTaskEnd(taskEnd);
            request.setTaskCategory(selectedTaskCategory);
            request.setOpen(true);
            taskChangeRequestRepository.save(request);
            logger.info("Created: TaskChangeRequest for Task " + task.getTaskId() + " by User " + getAuthenticatedUser().getUsername());
            return;
        }
        updateTask(task, taskStart, taskEnd, selectedTaskCategory);
    }

    /**
     * to accept a request either as a leader of a team or as the manager of a department
     * @param taskChangeRequest the task requested to be changed
     */
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public void acceptRequest(TaskChangeRequest taskChangeRequest){
        updateTask(taskRepository.findFirstByTaskId(taskChangeRequest.getTaskId()), taskChangeRequest.getTaskStart(), taskChangeRequest.getTaskEnd(), taskChangeRequest.getTaskCategory());
        taskChangeRequest.setOpen(false);
        taskChangeRequest.setUpdateDate(new Date());
        taskChangeRequest.setUpdateUser(getAuthenticatedUser());
        logger.info("Accepted: TaskChangeRequest for Task " + taskChangeRequest.getTaskId() + " by User " + getAuthenticatedUser().getUsername());
        taskChangeRequestRepository.save(taskChangeRequest);
    }

    /**
     * to refuse a request for a task to be changed either as a leader of a team or as the manager of a department
     * @param taskChangeRequest the task requested to be changed
     */
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER')")
    public void refuseRequest(TaskChangeRequest taskChangeRequest){
        taskChangeRequest.setOpen(false);
        taskChangeRequest.setUpdateDate(new Date());
        taskChangeRequest.setUpdateUser(getAuthenticatedUser());
        taskChangeRequestRepository.save(taskChangeRequest);
        logger.info("Refused: TaskChangeRequest for Task " + taskChangeRequest.getTaskId() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * to update a changed task either as a leader of a team or as the manager of a department
     * @param task the changed task that has to be updated
     * @param taskStart provides information about the start of the task
     * @param taskEnd provides information about the end of the task
     * @param selectedTaskCategory the selected category of tasks
     */
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER') or hasAuthority('ADMIN')")
    public void updateTask(Task task, Date taskStart, Date taskEnd, String selectedTaskCategory){

        if(selectedTaskCategory.equals("DELETE")){
            deleteTask(task);
        }

        if(taskStart != null && taskEnd != null){
            if(taskStart.after(taskEnd)){
                return;
            }
        }

        if(taskStart != null){
            task.setTaskStart(taskStart);
        }

        if(taskEnd != null){
            task.setTaskEnd(taskEnd);
        }

        if(selectedTaskCategory != null){
            if(!selectedTaskCategory.equalsIgnoreCase(task.getTaskCategory().toString())){
                for(TaskCategory taskCat : TaskCategory.values()){
                    if (taskCat.toString().equals(selectedTaskCategory)){
                        Set<TaskCategory> selectedCategory = new HashSet<>();
                        selectedCategory.add(taskCat);
                        task.setTaskCategory(selectedCategory);
                    }
                }
            }
        }

        task.setUpdateDate(new Date());
        task.setUpdateUser(getAuthenticatedUser());
        saveTask(task);
        logger.info("Edited: Task " + task.getTaskId() + " from User " + task.getCreateUser().getUsername() + " by User " + getAuthenticatedUser().getUsername());

    }

    /**
     * to delete a task that was requested to be deleted and confirmed to be deleted by either as a leader of a team
     * or as the manager of a department
     * @param task the task to delete
     */
    @PreAuthorize("isAuthenticated()")
    public void requestDelete(Task task){
        long twoWeeks = 14 * 24 * 3600 * 1000;
        if((System.currentTimeMillis() - task.getTaskStart().getTime()) > twoWeeks){
            TaskChangeRequest request = new TaskChangeRequest();
            request.setCreateDate(new Date());
            request.setCreateUser(getAuthenticatedUser());
            request.setTaskId(task.getTaskId());
            request.setTaskStart(null);
            request.setTaskEnd(null);
            request.setTaskCategory("DELETE");
            request.setOpen(true);
            taskChangeRequestRepository.save(request);
            logger.info("Created: TaskChangeRequest for Task " + task.getTaskId() + " by User " + getAuthenticatedUser().getUsername());
            return;
        }
        deleteTask(task);
    }

    /**
     * Deletes the task.
     * @param task the task to delete
     */
    @PreAuthorize("hasAuthority('DEPARTMENT_MANAGER') or hasAuthority('TEAM_LEADER') or hasAuthority('ADMIN')")
    public void deleteTask(Task task) {
        task.setDeleteUser(getAuthenticatedUser());
        task.setDeleteDate(new Date());
        task.setEnabled(false);
        taskRepository.save(task);
        logger.info("Deleted: Task " + task.getTaskId() + " from User " + task.getCreateUser().getUsername() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * @return returns the authenticated user
     */
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

}