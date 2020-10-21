package TimeManager.ui.controllers;

import TimeManager.services.RoomService;
import TimeManager.model.Room;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * Controller for the room list view.
 *
 */
@Component
@Scope("view")
public class RoomListController implements Serializable {

    @Autowired
    private RoomService roomService;

    private Collection<Room> filteredRooms;


    public Collection<Room> getFilteredRooms(){
        return this.filteredRooms;
    }

    public void setFilteredRooms(Collection<Room> filteredRooms){
       this.filteredRooms = filteredRooms;
    }

    public Map<Room, List<User>> getRoomListMap() {
        return roomService.getRoomUserMap();
    }

    public List<TimeFlip> getTimeFlips (List<User> user){
        return roomService.getTimeFlipsInRoom(user);
    }
}
