package TimeManager.ui.controllers;

import TimeManager.services.RoomService;
import TimeManager.model.Miniserver;
import TimeManager.services.MiniserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * Controller for the miniserver detail view.
 *
 */
@Component
@Scope("view")
public class MiniserverDetailController implements Serializable {

    @Autowired
    private MiniserverService miniserverService;

    @Autowired
    private RoomService roomService;

    /**
     * Attribute to cache the currently displayed miniserver
     */
    private Miniserver miniserver;

    private List<String> availableRooms;
    private String selectedRoom;
    private String currentRoom;

    public MiniserverDetailController(){}

    public MiniserverDetailController(MiniserverService miniserverService, RoomService roomService){
        this.miniserverService = miniserverService;
        this.roomService = roomService;
    }

    @PostConstruct
    public void init(){
        availableRooms = roomService.getAvailableRooms();
    }

    public List<String> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<String> availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public String getCurrentRoom() {
        if(roomService.findRoomByMiniserver(miniserver) != null){
            currentRoom = roomService.findRoomByMiniserver(miniserver).toString();
            return currentRoom;
        }
        else{
            return "Kein Raum";
        }
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Sets the currently displayed miniserver and reloads it form db. This miniserver is
     * targeted by any further calls of
     * {@link #doReloadMiniserver()}, {@link #doSaveMiniserver()} and
     * {@link #doDeleteMiniserver()}.
     *
     * @param miniserver the miniserver
     */
    public void setMiniserver(Miniserver miniserver) {
        this.miniserver = miniserver;
        doReloadMiniserver();
    }

    /**
     * Returns the currently displayed miniserver.
     *
     * @return the miniserver
     */
    public Miniserver getMiniserver() {
        return miniserver;
    }

    /**
     * Action to force a reload of the currently displayed miniserver.
     */
    public void doReloadMiniserver() {
        miniserver = miniserverService.loadMiniserver(miniserver.getMiniserverId());
    }

    /**
     * Action to save the currently displayed miniserver.
     */
    public void doSaveMiniserver() {
        miniserver = miniserverService.updateMiniserver(miniserver, selectedRoom);
    }

    /**
     * Action to delete the currently displayed Miniserver.
     */
    public void doDeleteMiniserver() {
        this.miniserverService.deleteMiniserver(miniserver);
        miniserver = null;
    }
}
