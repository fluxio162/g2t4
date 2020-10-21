package TimeManager.ui.controllers;

import TimeManager.model.TimeFlip;
import TimeManager.services.TimeFlipService;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * Controller to create new TimeFlip
 */
@Component
@Scope("view")
public class TimeFlipCreationController implements Serializable {

    @Autowired
    private TimeFlipService timeFlipService;

    @Autowired
    private UserService userService;

    private TimeFlip timeFlip = new TimeFlip();
    private int timeFlipId;

    private List<String> availableUser;
    private String selectedUser;

    private boolean disableSaveButton = false;

    public TimeFlipCreationController(){}

    public TimeFlipCreationController(TimeFlipService timeFlipService, UserService userService){
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

    public void setTimeFlipId(int timeFlipId) {
        this.timeFlipId = timeFlipId;
    }

    public int getTimeFlipId() { return timeFlipId = timeFlipService.findMaxTimeFlipId()+1;}

    public void setTimeFlip(TimeFlip timeFlip) {
        this.timeFlip = timeFlip;
    }

    public TimeFlip getTimeFlip() {return timeFlip; }

    public boolean isDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    /**
     * Action to save the currently displayed timeflip.
     */
    public void doSaveTimeFlip() {
        timeFlip = this.timeFlipService.createTimeFlip(timeFlip, timeFlipId, selectedUser);
    }

    /**
     * checks if the MAC address is available or already taken
     */
    public void validateMACAddress(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(timeFlipService.findTimeFlipByMACAddress(timeFlip.getMacAddress()) == null){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg:", "MAC Adresse gültig."));
            disableSaveButton = false;
        }
        else{
            timeFlipService.findTimeFlipByMACAddress(timeFlip.getMacAddress());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehlgeschlagen:", "MAC Adresse bereits vergeben."));
            disableSaveButton = true;
        }
    }
}
