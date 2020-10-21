package TimeManager.repositories;

import TimeManager.model.User;
import TimeManager.model.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link  Vacation} entities.
 */
public interface VacationRepository extends AbstractRepository <Vacation, String>  {
    /**
     * Find first vacation by vacation id.
     *
     * @param vacationId the vacation id
     * @return the vacation
     */
    Vacation findFirstByVacationId(int vacationId);

    /**
     * Find all vacations from user.
     *
     * @param user the user
     * @return the list
     */
    @Query("SELECT u FROM Vacation u WHERE :user = u.createUser")
    List<Vacation> findAllFromUser(@Param("user") User user);

}
