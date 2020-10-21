package TimeManager.services;

import TimeManager.model.DefaultTimeFlipConfiguration;
import TimeManager.model.Miniserver;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.repositories.MiniserverRepository;
import TimeManager.repositories.TimeFlipRepository;
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
 * Service for accessing and manipulating timeFlip data.
 */
@Component
@Scope("application")
public class TimeFlipService {

    private static final Logger logger = Logger.getLogger(TimeFlipService.class);

    @Autowired
    private TimeFlipRepository timeFlipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MiniserverRepository miniserverRepository;

    @Autowired
    private MiniserverService miniserverService;

    private Miniserver oldMiniserver;
    private Miniserver newMiniserver;

    /**
     * Returns a collection of all timeFlips.
     *
     * @return all time flips
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<TimeFlip> getAllTimeFlips() {
        return timeFlipRepository.findAll();
    }

    /**
     * Find highest TimeFlipId in database.
     *
     * @return the highest TimeFlipId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public int findMaxTimeFlipId(){
        return timeFlipRepository.findMaxTimeFlipId();
    }

    /**
     * Find TimeFlip by MACAddress.
     *
     * @param macAddress the macAddress
     * @return the timeFlip
     */
    public TimeFlip findTimeFlipByMACAddress(String macAddress){
        return timeFlipRepository.findTimeFlipByMACAddress(macAddress);
    }


    /**
     * Find all TimeFlips without an User assigned.
     *
     * @return the list of all unassigned TimeFlips
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TimeFlip> getAllFreeTimeFlips(){
        List<TimeFlip> tempList = new ArrayList<>(timeFlipRepository.findAll());
        tempList.removeIf(m -> m.getUser() != null);
        return tempList;
    }

    /**
     * Loads a single timeFlip identified by its timeFlipId.
     *
     * @param timeFlipId the timeFlipId to search for
     * @return the timeFlip with the given timeFlipId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public TimeFlip loadTimeFlip(int timeFlipId) {
        return timeFlipRepository.findFirstByTimeFlipId(timeFlipId);
    }

    /**
     * Saves the timeFlip. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param timeFlip the timeFlip to save
     * @return the updated timeFlip
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public TimeFlip saveTimeFlip(TimeFlip timeFlip) {
        String message;
        if (timeFlip.isNew()) {
            timeFlip.setCreateDate(new Date());
            timeFlip.setCreateUser(getAuthenticatedUser());
            message = "Created";
        } else {
            timeFlip.setUpdateDate(new Date());
            timeFlip.setUpdateUser(getAuthenticatedUser());
            message = "Edited";
        }
        logger.info(message + ": TimeFlip " + timeFlip.getTimeFlipId() + " by User " + getAuthenticatedUser().getUsername());
        return timeFlipRepository.save(timeFlip);
    }

    /**
     * Removes TimeFlip from assigned User.
     * Removes User from TimeFlip.
     * Removes TimeFlip from assigned Miniserver.
     * Disables TimeFlip.
     *
     * @param timeFlip the timeFlip to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTimeFlip(TimeFlip timeFlip) {
        nullUser(timeFlip);
        timeFlip.setUser(null);
        nullMiniserver(timeFlip);
        timeFlip.setDeleteUser(getAuthenticatedUser());
        timeFlip.setDeleteDate(new Date());
        timeFlip.setEnabled(false);
        timeFlipRepository.save(timeFlip);


        logger.info("Deleted: TimeFlip " + timeFlip.getTimeFlipId() + " by User " + getAuthenticatedUser().getUsername());
    }

    /**
     * Creates a new TimeFlip with input from TimeFlipCreationController.
     *
     * @param timeFlip the timeflip to create
     * @param timeFlipId the timeFlipId
     * @param selectedUser the user to assign timeflip to
     * @return the timeflip
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public TimeFlip createTimeFlip(TimeFlip timeFlip, int timeFlipId, String selectedUser) {

        timeFlip.setTimeFlipId(timeFlipId);

        if (timeFlipRepository.findFirstByTimeFlipId(timeFlipId) != null) {
            return null;
        }

        if (timeFlip.getTimeFlipId() == 0) {
            return null;
        }

        timeFlip.setTimeFlipConfiguration(DefaultTimeFlipConfiguration.getDefaultTimeFlipConfiguration());
        timeFlip.setEnabled(true);
        saveTimeFlip(timeFlip);

        if(!(selectedUser.equals("noUser"))){
            setUser(timeFlip, selectedUser);
        }

        return timeFlip;
    }

    /**
     * Updates the timeflip with input from TimeFlipDetailController.
     *
     * @param timeFlip the timeflip to update
     * @param selectedUser the new user
     * @param currentUser the current user
     * @return the timeflip
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public TimeFlip updateTimeFlip(TimeFlip timeFlip, String selectedUser, String currentUser){
        if(!(selectedUser.isEmpty())){
            changeUser(timeFlip, selectedUser, currentUser);
            changeMiniserver(timeFlip);
        }
        return saveTimeFlip(timeFlip);
    }

    /**
     * Removes TimeFlip from old User and assigns to new User.
     * Caches Miniserver in new Room and Miniserver in old Room.
     */
    private void changeUser(TimeFlip timeFlip, String selectedUser, String currentUser){
        if (!(selectedUser.isEmpty())) {
            User newUser = userRepository.findFirstByUsername(selectedUser);
            if(newUser.getRoom() != null){
                if(newUser.getRoom().getMiniserver() != null){
                    newMiniserver = miniserverService.loadMiniserver(newUser.getRoom().getMiniserver().getMiniserverId());
                }
            }
            if (currentUser == null){
                newUser.setTimeFlip(timeFlip);
                userRepository.save(newUser);
                logger.info("Edited : User " + newUser.getUsername() + " by User " + getAuthenticatedUser());
            }
            else{
                User oldUser = userRepository.findUserByTimeFlip(timeFlip);
                if(oldUser.getRoom() != null){
                    if(oldUser.getRoom().getMiniserver() != null){
                        oldMiniserver = miniserverService.loadMiniserver(oldUser.getRoom().getMiniserver().getMiniserverId());
                    }
                }
                oldUser.setTimeFlip(null);
                userRepository.save(oldUser);
                logger.info("Edited : User " + oldUser.getUsername() + " by User " + getAuthenticatedUser());
                newUser.setTimeFlip(timeFlip);
                userRepository.save(newUser);
                logger.info("Edited : User " + newUser.getUsername() + " by User " + getAuthenticatedUser());
            }
        }
    }

    /**
     * Removes TimeFlip from Miniserver in old Room and assigns TimeFlip to Miniserver in new Room.
     */
    private void changeMiniserver(TimeFlip timeFlip){
        if(newMiniserver != null){
            Set<TimeFlip> tempNewSet = new HashSet(newMiniserver.getTimeFlip());
            tempNewSet.add(timeFlip);
            newMiniserver.setTimeFlip(tempNewSet);
            miniserverService.saveMiniserver(newMiniserver);
            if(oldMiniserver != null){
                Set<TimeFlip> tempOldSet = new HashSet(oldMiniserver.getTimeFlip());
                tempOldSet.remove(timeFlip);
                oldMiniserver.setTimeFlip(tempOldSet);
                miniserverService.saveMiniserver(oldMiniserver);
            }
        }
    }


    /**
     * Assigns TimeFlip to User if User is changed.
     */
    private void setUser(TimeFlip timeFlip, String selectedUser){
        if(!(selectedUser.isEmpty())){
            User user = userRepository.findFirstByUsername(selectedUser);
            user.setTimeFlip(timeFlip);
            userRepository.save(user);
            logger.info("Edited : User " + user.getUsername() + " by User " + getAuthenticatedUser());
            if(user.getRoom() != null){
                setRoom(timeFlip, user);
            }
        }
    }

    /**
     * Assigns TimeFlip to Miniserver in new Room if Room is changed.
     *
     * @param user the user
     */
    private void setRoom(TimeFlip timeFlip, User user){
        if(user.getRoom().getMiniserver() != null){
            Miniserver miniserver = miniserverRepository.findFirstByMiniserverId(user.getRoom().getMiniserver().getMiniserverId());
            HashSet<TimeFlip> timeFlipSet = new HashSet<>(miniserver.getTimeFlip());
            timeFlipSet.add(timeFlip);
            miniserver.setTimeFlip(timeFlipSet);
            miniserverService.saveMiniserver(miniserver);
        }
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Removes TimeFlip from assigned User.
     *
     * @param timeFlip the timeFlip
     */
    private void nullUser(TimeFlip timeFlip){
        if(timeFlip.getUser() != null){
            User user = timeFlip.getUser();
            user.setTimeFlip(null);
            userRepository.save(user);
            logger.info("Removed: TimeFlip " + timeFlip.getTimeFlipId() + " from User " + user.getUsername() + " by User " + getAuthenticatedUser().getUsername());
        }
    }

    /**
     * Removes TimeFlip from assigned Miniserver.
     *
     * @param timeFlip the timeFlip
     */
    private void nullMiniserver(TimeFlip timeFlip){
        try{
            Miniserver miniserver = miniserverRepository.findMiniserverWithTimeFlip(timeFlip);
            Set<TimeFlip> timeFlipSet = new HashSet<>(miniserver.getTimeFlip());
            timeFlipSet.remove(timeFlip);
            miniserver.setTimeFlip(timeFlipSet);
            miniserverRepository.save(miniserver);
            logger.info("Removed: TimeFlip " + timeFlip.getTimeFlipId() + " from Miniserver " + miniserver.getMiniserverId() + " by User " + getAuthenticatedUser().getUsername());
        } catch (NullPointerException e){
            logger.error("Error removing TimeFlip from Miniserver.");
        }
    }

    /**
     * Generates a list with all tasks.
     *
     * @return list of all tasks
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> getAllTasks(){
        List<String> taskList = new ArrayList<>();

        taskList.add("Pause/Dienstfrei");
        taskList.add("Konzeption");
        taskList.add("Design");
        taskList.add("Implementierung");
        taskList.add("Testen");
        taskList.add("Dokumentation");
        taskList.add("Fehleranalyse und -korrektur");
        taskList.add("Meeting (intern)");
        taskList.add("Kundenbesprechung");
        taskList.add("Fortbildung");
        taskList.add("Projektmanagement");
        taskList.add("Sonstiges");

        return taskList;
    }

    /**
     * Generates a map with values of all timeflip sides.
     *
     * @return the map
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public  Map<Integer, String> getTimeFlipSides(){
        Map<Integer, String> configuration = new HashMap<>();

        configuration.put(0, null);
        configuration.put(1, null);
        configuration.put(2, null);
        configuration.put(3, null);
        configuration.put(4, null);
        configuration.put(5, null);
        configuration.put(6, null);
        configuration.put(7, null);
        configuration.put(8, null);
        configuration.put(9, null);
        configuration.put(10, null);
        configuration.put(11, null);

        return configuration;
    }

    /**
     * Generates a map with all timeflips and its user.
     *
     * @return the map
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<TimeFlip, User> getTimeFlipUserMap(){
        Map<TimeFlip, User> timeFlipUserMap = new TreeMap<>();
        timeFlipRepository.findAll().forEach(f -> timeFlipUserMap.put(f, null));
        userRepository.findUserWithTimeFlip().forEach(f -> timeFlipUserMap.put(f.getTimeFlip(), f));
        return timeFlipUserMap;
    }

    /**
     * Fill availableTimeFlips list with timeFlipId of all TimeFlips.
     *
     * @return list of all available timeFlips
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Integer> getAvailableTimeFlips(){
        List<Integer> availableTimeFlips = new ArrayList<>();
        timeFlipRepository.findAll().forEach(t -> {if(t.getUser() == null){availableTimeFlips.add(t.getTimeFlipId());}});
        return availableTimeFlips;
    }
}
