package TimeManager.ui.controllers;

import TimeManager.model.Room;
import TimeManager.services.MiniserverService;
import TimeManager.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * Controller to create new Room.
 */
@Component
@Scope("view")
public class RoomCreationController implements Serializable {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MiniserverService miniserverService;

    private Room room = new Room();

    private int roomId;

    private List<String> availableMiniserver;
    private String selectedMiniserver;

    /**
     * Fill availableMiniserver list with miniserverId of all available Miniserver.
     */
    @PostConstruct
    public void init(){
        availableMiniserver = miniserverService.getUnassignedMiniserver();
    }


    public List<String> getAvailableMiniserver() {
        return availableMiniserver;
    }

    public void setAvailableMiniserver(List<String> availableMiniserver) {
        this.availableMiniserver = availableMiniserver;
    }

    public String getSelectedMiniserver() {
        return selectedMiniserver;
    }

    public void setSelectedMiniserver(String selectedMiniserver) {
        this.selectedMiniserver = selectedMiniserver;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() { return roomId = roomService.findMaxRoomId()+1;}

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {return room; }

    /**
     * Action to save the currently displayed room.
     */
    public void doSaveRoom() {
        room = this.roomService.createRoom(room, roomId, selectedMiniserver);
    }
}
