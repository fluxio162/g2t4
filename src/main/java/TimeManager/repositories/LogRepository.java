package TimeManager.repositories;

import TimeManager.model.Log;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for managing {@link Log} entities.
 */
public interface LogRepository extends AbstractRepository <Log, Integer>{

    /**
     * Find all Logs.
     *
     * @return the list of logs
     */
    @Query("SELECT l FROM Log l")
    List<Log> findAll();

}
