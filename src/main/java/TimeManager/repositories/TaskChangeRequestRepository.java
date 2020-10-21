package TimeManager.repositories;

import TimeManager.model.TaskChangeRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskChangeRequestRepository extends AbstractRepository<TaskChangeRequest, Integer> {

    TaskChangeRequest findFirstById(int id);

    @Query("SELECT t FROM TaskChangeRequest t WHERE t.open = true")
    List<TaskChangeRequest> findAll();
}