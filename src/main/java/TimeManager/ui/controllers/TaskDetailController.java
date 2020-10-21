package TimeManager.ui.controllers;

import TimeManager.model.Task;
import TimeManager.model.TaskCategory;
import TimeManager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Controller for the task detail view.
 *
 */
@Component
@Scope("view")
public class TaskDetailController implements Serializable {

    @Autowired
    private TaskService taskService;

    /**
     * Attribute to cache the currently displayed task
     */
    private Task task;

    private Date taskStart;

    private Date taskEnd;


    private List<String> taskCategories;
    private String selectedTaskCategory;


    @PostConstruct
    public void init(){
        this.taskCategories = new ArrayList<>();
        for(TaskCategory taskCategory : TaskCategory.values()){
            this.taskCategories.add(taskCategory.toString());
        }
    }


    /**
     * Sets the currently displayed task and reloads it form db. This task is
     * targeted by any further calls of
     * {@link #doReloadTask()}, {@link #doSaveTask()} and
     * {@link #doDeleteTask()}.
     *
     * @param task the task
     */
    public void setTask(Task task) {
        this.task = task;
        doReloadTask();
    }

    /**
     * Returns the currently displayed task.
     *
     * @return the task
     */
    public Task getTask() {
        return task;
    }


    public List<String> getTaskCategories() {
        return taskCategories;
    }

    public void setTaskCategories(List<String> taskCategories) {
        this.taskCategories = taskCategories;
    }

    public String getSelectedTaskCategory() {
        return selectedTaskCategory;
    }

    public void setSelectedTaskCategory(String selectedTaskCategory) {
        this.selectedTaskCategory = selectedTaskCategory;
    }

    public Date getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Date taskStart) {
        this.taskStart = taskStart;
    }

    public Date getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Date taskEnd) {
        this.taskEnd = taskEnd;
    }

    /**
     * Action to force a reload of the currently displayed task.
     */
    public void doReloadTask() {
        task = taskService.loadTask(task.getTaskId());
    }

    /**
     * Action to save the currently displayed task.
     */
    public void doSaveTask() {
            taskService.requestUpdate(task, taskStart, taskEnd, selectedTaskCategory);
    }


    /**
     * Action to delete the currently displayed Task.
     */
    public void doDeleteTask(){
        this.taskService.requestDelete(task);
        task = null;
    }
}
