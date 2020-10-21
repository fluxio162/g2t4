package TimeManager.ui.controllers;

import TimeManager.model.TaskChangeRequest;
import TimeManager.model.Task;
import TimeManager.model.User;
import TimeManager.services.TaskService;
import TimeManager.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

/**
 * Controller for the task list view.
 *
 */
@Component
@Scope("view")
public class TaskListController implements Serializable {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private Collection<Task> filteredTasks;
    private List<Task> userTasks;

    private Map<Task, TaskChangeRequest> taskChangeRequests;

    private TaskChangeRequest taskChangeRequest;

    @PostConstruct
    public void init(){
        User currentUser = sessionInfoBean.getCurrentUser();
        this.userTasks = new ArrayList<>();
        this.userTasks = this.taskService.loadTaskByUser(currentUser);
    }

    public Collection<Task> getFilteredTasks(){
        return this.filteredTasks;
    }

    public void setFilteredTasks(Collection<Task> filteredTasks){
       this.filteredTasks = filteredTasks;
    }

    public List<Task> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<Task> userTasks) {
        this.userTasks = userTasks;
    }

    public Map<Task, TaskChangeRequest> getTaskChangeRequests() {
        if(sessionInfoBean.getCurrentUserRoles().equals("DEPARTMENT_MANAGER")){
            return taskService.getAllTaskChangeRequestsFromDepartment();
        }
        else if(sessionInfoBean.getCurrentUserRoles().equals("TEAM_LEADER")){
            return taskService.getAllTaskChangeRequestsFromTeam();
        }
        return taskChangeRequests;
    }

    public void setTaskChangeRequests(Map<Task, TaskChangeRequest> taskChangeRequests) {
        this.taskChangeRequests = taskChangeRequests;
    }

    public TaskChangeRequest getTaskChangeRequest() {
        return taskChangeRequest;
    }

    public void setTaskChangeRequest(TaskChangeRequest taskChangeRequest) {
        this.taskChangeRequest = taskChangeRequest;
    }

    public void acceptRequest(){
        taskService.acceptRequest(taskChangeRequest);
    }

    public void refuseRequest(){
        taskService.refuseRequest(taskChangeRequest);
    }
}
