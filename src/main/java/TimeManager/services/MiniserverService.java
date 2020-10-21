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
 * Service for accessing and manipulating miniserver data.
 */
@Component
@Scope("application")
public class MiniserverService {

    private static final Logger logger = Logger.getLogger(MiniserverService.class);

    @Autowired
    private MiniserverRepository miniserverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Returns a collection of all miniservers.
     *
     * @return all miniservers
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Miniserver> getAllMiniservers() {
        return miniserverRepository.findAll();
    }

    /**
     * Find highest MiniserverId in database.
     *
     * @return the highest MiniserverId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public int findMaxMiniserverId(){
        return miniserverRepository.findMaxMiniserverId();
    }

    /**
     * Find miniserver with a specific TimeFlip.
     *
     * @param timeFlip the timeFlip
     * @return the miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Miniserver findMiniserverWithTimeFlip(TimeFlip timeFlip){
        return miniserverRepository.findMiniserverWithTimeFlip(timeFlip);
    }

    /**
     * Find all miniserver not assigned to a room.
     *
     * @return the list of all free miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Miniserver> getAllFreeMiniserver(){
        List<Miniserver> tempList = new ArrayList<>(miniserverRepository.getAllFreeMiniserver());
        tempList.removeIf(m -> m.getRoom() != null);
        return tempList;
    }

    /**
     * Loads a single miniserver identified by its miniserverId.
     *
     * @param miniserverId the miniserverId to search for
     * @return the miniserver with the given miniserverId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Miniserver loadMiniserver(int miniserverId) {
        return miniserverRepository.findFirstByMiniserverId(miniserverId);
    }

    /**
     * Saves the miniserver. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param miniserver the miniserver to save
     * @return the updated miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Miniserver saveMiniserver(Miniserver miniserver) {
        String message;
        if (miniserver.isNew()) {
            miniserver.setCreateDate(new Date());
            miniserver.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            miniserver.setUpdateDate(new Date());
            miniserver.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        logger.info(message + ": Miniserver " + miniserver.getMiniserverId() + " by User " + getAuthenticatedUser().getUsername());
        return miniserverRepository.save(miniserver);
    }

    /**
     * Removes Miniserver from assigned Room.
     * Removes all TimeFlips assigned to Miniserver.
     * Disables Miniserver.
     *
     * @param miniserver the miniserver to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMiniserver(Miniserver miniserver) {
        if(miniserver.getRoom() != null){
            Room room = miniserver.getRoom();
            room.setMiniserver(null);
            roomRepository.save(room);
            miniserver.setRoom(null);
        }
        miniserver.setTimeFlip(new HashSet<>());

        miniserver.setDeleteUser(getAuthenticatedUser());
        miniserver.setDeleteDate(new Date());
        miniserver.setEnabled(false);
        miniserverRepository.save(miniserver);


        logger.info("Deleted: Miniserver " + miniserver.getMiniserverId() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * Creates a new Miniserver with input from MiniserverCreationController.
     *
     * @param miniserver the miniserver to create
     * @param miniserverId the miniserverId for new miniserver
     * @return the miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Miniserver createMiniserver(Miniserver miniserver, int miniserverId){
        miniserver.setMiniserverId(miniserverId);
        if (miniserverRepository.findFirstByMiniserverId(miniserverId) != null) {
            return null;
        }

        if (miniserver.getMiniserverId()==0) {
            return null;
        }

        miniserver.setEnabled(true);
        return saveMiniserver(miniserver);
    }

    /**
     * Updates a Miniserver with input from MiniserverDetailController.
     * Reassigns TimeFlips in new Room.
     *
     * @param miniserver the miniserver to update
     * @param selectedRoom the new room
     * @return the miniserver
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Miniserver updateMiniserver(Miniserver miniserver, String selectedRoom){
        if(!(selectedRoom.isEmpty())){

            if(miniserver.getRoom() != null){
                Room oldRoom = roomRepository.findFirstByRoomId(miniserver.getRoom().getRoomId());
                oldRoom.setMiniserver(null);
                roomRepository.save(oldRoom);
            }

            Room room = roomRepository.findFirstByRoomId(Integer.parseInt(selectedRoom));

            if(room.getMiniserver() != null){
                Miniserver oldMiniserver = room.getMiniserver();
                oldMiniserver.setTimeFlip(null);
                miniserverRepository.save(oldMiniserver);
                room.setMiniserver(null);
                roomRepository.save(room);
            }

            miniserver.setTimeFlip(null);
            room.setMiniserver(miniserver);
            roomRepository.save(room);
        }



        if(!(selectedRoom.isEmpty())){
            return allocateTimeFlips(miniserver, selectedRoom);
        }
        else{
            return saveMiniserver(miniserver);
        }
    }

    /**
     * Assigns all TimeFlips in new Room to Miniserver if Room is changed.
     */
    private Miniserver allocateTimeFlips(Miniserver miniserver, String selectedRoom){
            HashSet<TimeFlip> timeFlipHashSet = new HashSet<>();
            List<User> userList = new ArrayList<>(userRepository.findUserInRoom(roomRepository.findFirstByRoomId(Integer.parseInt(selectedRoom))));
            userList.forEach(u -> {if(u.getTimeFlip() != null){
                timeFlipHashSet.add(u.getTimeFlip());
                logger.info("Edited: TimeFlip " + u.getTimeFlip().getTimeFlipId() + " assigned to Miniserver " + miniserver.getMiniserverId());
            }});
            miniserver.setTimeFlip(timeFlipHashSet);
            saveMiniserver(miniserver);
            return miniserver;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<Miniserver, Room> getMiniserverRoomMap() {
        Map<Miniserver, Room> miniserverRoomMap = new TreeMap<>();
        miniserverRepository.findAll().forEach(s -> miniserverRoomMap.put(s, null));
        roomRepository.findRoomsWithMiniserver().forEach(r -> miniserverRoomMap.put(r.getMiniserver(), r));
        return miniserverRoomMap;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getUnassignedMiniserver() {
        List<String> unassignedMiniserver = new ArrayList<>();
        miniserverRepository.getAllFreeMiniserver().forEach(m -> unassignedMiniserver.add(m.toString()));
        return unassignedMiniserver;
    }
}
