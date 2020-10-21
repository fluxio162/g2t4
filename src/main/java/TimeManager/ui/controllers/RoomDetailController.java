package TimeManager.ui.controllers;

import TimeManager.model.Room;
import TimeManager.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for the room detail view.
 *
 */
@Component
@Scope("view")
public class RoomDetailController implements Serializable {

    @Autowired
    private RoomService roomService;

    /**
     * Attribute to cache the currently displayed room
     */
    private Room room;


    /**
     * Sets the currently displayed room and reloads it form db. This room is
     * targeted by any further calls of
     * {@link #doReloadRoom()} and
     * {@link #doDeleteRoom()}.
     *
     * @param room the room
     */
    public void setRoom(Room room) {
        this.room = room;
        doReloadRoom();
    }

    /**
     * Returns the currently displayed room.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Action to force a reload of the currently displayed room.
     */
    public void doReloadRoom() {
        room = roomService.loadRoom(room.getRoomId());
    }

    public void save(Room room){
        this.roomService.saveRoom(room);
    }

    /**
     * Action to delete the currently displayed Room.
     */
    public void doDeleteRoom() {
        this.roomService.deleteRoom(room);
        room = null;
    }
}
