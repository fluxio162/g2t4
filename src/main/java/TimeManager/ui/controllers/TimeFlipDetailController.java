package TimeManager.ui.controllers;

import TimeManager.services.TimeFlipService;
import TimeManager.model.TimeFlip;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 * Controller for the timeFlip detail view.
 *
 */
@Component
@Scope("view")
public class TimeFlipDetailController implements Serializable {

    @Autowired
    private TimeFlipService timeFlipService;

    @Autowired
    private UserService userService;


    private TimeFlip timeFlip;

    private List<String> availableUser;
    private String selectedUser;
    private String currentUser;

    public TimeFlipDetailController(){}

    public TimeFlipDetailController(TimeFlipService timeFlipService, UserService userService){
        this.timeFlipService = timeFlipService;
        this.userService = userService;
    }

    /**
     * Fill availableUser list with username of all available user.
     */
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
        if(userService.findUserByTimeFlip(timeFlip) != null){
            currentUser = userService.findUserByTimeFlip(timeFlip).getUsername();
            return currentUser;
        }
        else{
            return "Kein User";
        }
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Sets the currently displayed timeFlip and reloads it form db. This timeFlip is
     * targeted by any further calls of
     * {@link #doReloadTimeFlip()}, {@link #doSaveTimeFlip()} and
     * {@link #doDeleteTimeFlip()}.
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
        timeFlip = timeFlipService.updateTimeFlip(timeFlip, selectedUser, currentUser);
    }

    /**
     * Action to delete the currently displayed TimeFlip.
     */
    public void doDeleteTimeFlip() {
        this.timeFlipService.deleteTimeFlip(timeFlip);
        timeFlip = null;
    }
}


