package TimeManager.repositories;

import TimeManager.model.Miniserver;
import TimeManager.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link Room} entities.
 */
public interface RoomRepository extends AbstractRepository <Room, Integer> {

    /**
     * Find first room by room id.
     *
     * @param roomId the room id
     * @return the room
     */
    Room findFirstByRoomId(int roomId);

    /**
     * Find all rooms.
     *
     * @return the list of rooms
     */
    @Query("SELECT r FROM Room r WHERE r.enabled = true")
    List<Room> findAll();

    /**
     * Find rooms that have a miniserver assigned.
     *
     * @return the list of rooms
     */
    @Query("SELECT r FROM Room r WHERE r.miniserver IS NOT NULL AND r.enabled = true")
    List<Room> findRoomsWithMiniserver();

    /**
     * Find room by its assigned miniserver.
     *
     * @param miniserver the miniserver
     * @return the room
     */
    @Query("SELECT r FROM Room r WHERE r.miniserver = :miniserver AND r.enabled = true")
    Room findRoomByMiniserver(@Param("miniserver") Miniserver miniserver);

    /**
     * Find highest room id.
     *
     * @return the room id
     */
    @Query("SELECT MAX(roomId) FROM Room WHERE enabled = true")
    int findMaxRoomId();
}
