package TimeManager.repositories;

import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link  TimeFlip} entities.
 */
public interface TimeFlipRepository extends AbstractRepository<TimeFlip, Integer>{

    /**
     * Find first time flip by id.
     *
     * @param timeFlipId the time flip id
     * @return the time flip
     */
    TimeFlip findFirstByTimeFlipId(int timeFlipId);

    /**
     * Find all time flips.
     *
     * @return the list of time flips
     */
    @Query("SELECT t FROM TimeFlip t WHERE t.enabled = true")
    List<TimeFlip> findAll();

    /**
     * Find highest time flip id.
     *
     * @return the time flip id
     */
    @Query("SELECT MAX(timeFlipId) FROM TimeFlip WHERE enabled = true")
    int findMaxTimeFlipId();

    /**
     * Find highest time flip id.
     *
     * @param macAddress the macAddress
     * @return the time flip id
     */
    @Query("SELECT t FROM TimeFlip t WHERE t.macAddress = :macAddress")
    TimeFlip findTimeFlipByMACAddress(@Param("macAddress") String macAddress);

}
