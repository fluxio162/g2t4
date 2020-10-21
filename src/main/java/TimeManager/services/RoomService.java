package TimeManager.services;

import TimeManager.model.Miniserver;
import TimeManager.model.Room;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.repositories.MiniserverRepository;
import TimeManager.repositories.RoomRepository;
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
 * Service for accessing and manipulating room data.
 */
@Component
@Scope("application")
public class RoomService {

    private static final Logger logger = Logger.getLogger(RoomService.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MiniserverRepository miniserverRepository;

    /**
     * Returns a collection of all rooms.
     *
     * @return all rooms
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Find all rooms with a miniserver assigned.
     *
     * @return the list of rooms with miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Room> findRoomsWithMiniserver(){
        return roomRepository.findRoomsWithMiniserver();
    }

    /**
     * Find room with a specific miniserver assigned.
     *
     * @param miniserver the miniserver
     * @return the room assigned to the miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room findRoomByMiniserver(Miniserver miniserver){
        return roomRepository.findRoomByMiniserver(miniserver);
    }

    /**
     * Find highest RoomId in database.
     *
     * @return the highest RoomId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public int findMaxRoomId(){
        return roomRepository.findMaxRoomId();
    }

    /**
     * Loads a single room identified by its roomId.
     *
     * @param roomId the roomId to search for
     * @return the room with the given roomId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room loadRoom(int roomId) {
        return roomRepository.findFirstByRoomId(roomId);
    }

    /**
     * Saves the room. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param room the room to save
     * @return the updated room
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room saveRoom(Room room) {
        String message;
        if (room.isNew()) {
            room.setCreateDate(new Date());
            room.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            room.setUpdateDate(new Date());
            room.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        logger.info(message + ": Room " + room.getRoomId() + " by User " + getAuthenticatedUser().getUsername());
        return roomRepository.save(room);
    }

    /**
     * Removes Miniserver assigned to Room.
     * Disables Room.
     *
     * @param room the room to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRoom(Room room) {
        if(room.getMiniserver() != null){
            room = nullMiniserver(room);
        }
        nullUser(room);

        room.setDeleteUser(getAuthenticatedUser());
        room.setDeleteDate(new Date());
        room.setEnabled(false);
        roomRepository.save(room);


        logger.info("Deleted: Room " + room.getRoomId() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * Creates a new Room with input from RoomCreationController.
     *
     * @param room the room to create
     * @param roomId the roomId
     * @param selectedMiniserver the miniserver to assign to new room
     * @return the room
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Room createRoom(Room room, int roomId, String selectedMiniserver){

        room.setRoomId(roomId);

        if (roomRepository.findFirstByRoomId(roomId) != null) {
            return null;
        }

        if (room.getRoomId()==0) {
            return null;
        }

        if(!(selectedMiniserver.equals("noMiniserver"))){
            Miniserver miniserver = miniserverRepository.findFirstByMiniserverId(Integer.parseInt(selectedMiniserver));
            if(miniserver.getRoom() != null){
                miniserver.setTimeFlip(new HashSet<>());
                miniserverRepository.save(miniserver);
                logger.info("Edited: Miniserver " + miniserver.getMiniserverId() + " by User " + getAuthenticatedUser().getUsername());
                Room oldRoom = findRoomByMiniserver(miniserver);
                oldRoom.setMiniserver(null);
                saveRoom(oldRoom);
            }
            room.setMiniserver(miniserver);
        }

        room.setEnabled(true);
        return saveRoom(room);
    }


    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes all TimeFlips from Miniserver assigned to Room.
     * Removes Miniserver from Room.
     *
     * @param room the room
     * @return room without miniserver
     */
    private Room nullMiniserver(Room room){
        Miniserver miniserver = room.getMiniserver();
        miniserver.setTimeFlip(new HashSet<>());
        miniserver.setRoom(null);
        miniserverRepository.save(miniserver);
        room.setMiniserver(null);
        return room;
    }

    /**
     * Removes Room from all User in Room.
     *
     * @param room the room
     */
    private void nullUser(Room room){
        List<User> userList = new ArrayList<>();
        userList.addAll(userRepository.findUserInRoom(room));
        for(User itUser : userList){
            itUser.setRoom(null);
            userRepository.save(itUser);
        }
    }

    /**
     * Fill availableRoom list with username of all Rooms.
     *
     * @return list of available rooms
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAvailableRooms(){
        List<String> availableRooms = new ArrayList<>();
        roomRepository.findAll().forEach(r -> availableRooms.add(r.toString()));
        return availableRooms;
    }

    /**
     * Generates a map of Rooms and all User in each Room.
     *
     * @return the map
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<Room, List<User>> getRoomUserMap(){
        Map<Room, List<User>> roomUserMap = new TreeMap<>();
        roomRepository.findAll().forEach(r -> roomUserMap.put(r, null));
        roomUserMap.keySet().forEach(r -> roomUserMap.put(r, userRepository.findUserInRoom(r)));
        return roomUserMap;
    }

    /**
     * Generates a list of TimeFlips from a List of User in a Room.
     *
     * @param user the list of user
     * @return the list of timeflips
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TimeFlip> getTimeFlipsInRoom(List<User> user){
        List<TimeFlip> timeFlipList = new ArrayList<>();
        user.forEach(u -> timeFlipList.add(u.getTimeFlip()));
        timeFlipList.remove(null);
        return timeFlipList;
    }
}
