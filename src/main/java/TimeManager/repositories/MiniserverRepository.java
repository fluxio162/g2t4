package TimeManager.repositories;

import TimeManager.model.Miniserver;
import TimeManager.model.TimeFlip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link Miniserver} entities.
 */
public interface MiniserverRepository extends AbstractRepository<Miniserver, Integer> {

    /**
     * Find first miniserver by miniserver id.
     *
     * @param miniserverId the miniserver id
     * @return the miniserver
     */
    Miniserver findFirstByMiniserverId(int miniserverId);

    /**
     * Find all minisevers.
     *
     * @return the list of miniservers
     */
    @Query("SELECT m FROM Miniserver m WHERE m.enabled = true")
    List<Miniserver> findAll();

    /**
     * Find highest miniserver id.
     *
     * @return the miniserver id
     */
    @Query("SELECT MAX(miniserverId) FROM Miniserver WHERE enabled = true")
    int findMaxMiniserverId();

    /**
     * Find a miniserver by its assigned time flip.
     *
     * @param timeFlip the time flip.
     * @return the miniserver
     */
    @Query("SELECT m FROM Miniserver m WHERE :timeFlip MEMBER OF m.timeFlip AND m.enabled = true")
    Miniserver findMiniserverWithTimeFlip(@Param("timeFlip") TimeFlip timeFlip);

    /**
     * Find all miniservers that have no time flip assigned.
     *
     * @return the list of miniservers
     */
    @Query("SELECT m FROM Miniserver m WHERE m.timeFlip IS EMPTY AND m.enabled = true")
    List<Miniserver> getAllFreeMiniserver();
}
