package TimeManager.ui.controllers;

import TimeManager.model.TimeFlip;
import TimeManager.services.TimeFlipService;
import org.primefaces.event.DragDropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * Controller to configure TimeFlip.
 *
 */
@Component
@Scope("view")
public class TimeFlipConfigurationController implements Serializable {

    @Autowired
    private TimeFlipService timeFlipService;

    /**
     * Attribute to cache the currently displayed timeFlip
     */
    private TimeFlip timeFlip;

    private List<String> taskList;

    private List<Integer> selectedSideList;

    private Map<Integer, String> configuration;

    public TimeFlipConfigurationController(){
    }

    public TimeFlipConfigurationController(TimeFlipService timeFlipService){
        this.timeFlipService = timeFlipService;
    }

    /**
     * Creates a list with all available task which can be assigned to TimeFlip side.
     *
     * @return list of all tasks
     */
    public List<String> getTaskList() {
        if(taskList != null){
            return taskList;
        }
        taskList = timeFlipService.getAllTasks();
        return taskList;
    }

    public void setTaskList(List<String> taskList) {
        this.taskList = taskList;
    }

    /**
     * Crates a map with all sides from TimeFlip.
     *
     * @return map with all sides
     */
    public Map<Integer, String> getConfiguration() {
        if(configuration != null){
            return configuration;
        }
        configuration = timeFlipService.getTimeFlipSides();
        return configuration;
    }

    public void setConfiguration(Map<Integer, String> configuration) {
        this.configuration = configuration;
    }

    public List<Integer> getSelectedSideList() {
        return selectedSideList;
    }

    public void setSelectedSideList(List<Integer> selectedSideList) {
        this.selectedSideList = selectedSideList;
    }

    /**
     * Sets the currently displayed timeFlip and reloads it form db. This timeFlip is
     * targeted by any further calls of
     * {@link #doReloadTimeFlip()} and {@link #doSaveTimeFlip()}
     *
     * @param timeFlip the timeFlip
     */
    public void setTimeFlip(TimeFlip timeFlip) {
        this.timeFlip = timeFlip;
        doReloadTimeFlip();
    }

    /**
     * Returns the currently displayed timeFlip.
     *
     * @return the timeFlip
     */
    public TimeFlip getTimeFlip() {
        return timeFlip;
    }


    /**
     * Action to force a reload of the currently displayed timeFlip.
     */
    public void doReloadTimeFlip() {
        timeFlip = timeFlipService.loadTimeFlip(timeFlip.getTimeFlipId());
    }

    /**
     * Action to save the currently displayed timeFlip.
     */
    public void doSaveTimeFlip() {
        timeFlip.setTimeFlipConfiguration(configuration);
        timeFlip = this.timeFlipService.saveTimeFlip(timeFlip);
    }

    /**
     * Will assign dragged task to TimeFlip side.
     *
     * @param ddEvent the ddEvent
     */
    public void onTaskDrop(DragDropEvent ddEvent) {
        String target = ddEvent.getDropId();
        int targetSide = Integer.parseInt(target.substring(13));
        String element = ddEvent.getData().toString();

        if(selectedSideList == null){
            selectedSideList = new ArrayList<>();
        }

        if(selectedSideList.contains(targetSide)){
            String task = configuration.get(targetSide);
            taskList.add(task);
        }

        configuration.put(targetSide, element);
        taskList.remove(element);
        selectedSideList.add(targetSide);
    }
}


