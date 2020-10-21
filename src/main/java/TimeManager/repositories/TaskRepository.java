package TimeManager.repositories;

import TimeManager.model.User;
import TimeManager.model.Task;
import TimeManager.model.TaskCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Repository for managing {@link Task} entities.
 */
public interface TaskRepository extends AbstractRepository<Task, String> {

    /**
     * Find first task by task id.
     *
     * @param taskId the task id
     * @return the task
     */
    Task findFirstByTaskId(int taskId);

    /**
     * Find all tasks.
     *
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE t.enabled = true")
    List<Task> findAll();

    /**
     * Find all tasks done by a given user.
     *
     * @param user the user
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE :user = t.createUser AND t.enabled = true")
    List<Task> allActivitiesByUser(@Param("user") User user);

    /**
     * Find all tasks that belong to a given task category.
     *
     * @param taskCategory the task category
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE :taskCategory = t.taskCategory AND t.enabled = true")
    List<Task> allActivitiesByCategory(@Param("taskCategory") Set<TaskCategory> taskCategory);

    /**
     * Find all tasks by their end date.
     *
     * @param taskEnd the task end date
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE :taskEnd = t.taskEnd AND t.enabled = true")
    List<Task> allActivitiesByEndDate(@Param("taskEnd") Date taskEnd);

    /**
     * Find all tasks done between two given dates.
     *
     * @param startDate the lower bound
     * @param endDate the upper bound
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE :startDate <= t.taskEnd AND :endDate >= t.taskEnd AND t.enabled = true")
    List<Task> allTasksBetweenDates(Date startDate, Date endDate);

    /**
     * Find all tasks done between two given dates and by a given user.
     *
     * @param startDate the lower bound
     * @param endDate the upper bound
     * @param user the user
     * @return the list of tasks
     */
    @Query("SELECT t FROM Task t WHERE :startDate <= t.taskEnd AND :endDate >= t.taskEnd AND :user = t.createUser AND t.enabled = true")
    List<Task> allTasksBetweenDatesFromUser(Date startDate, Date endDate, User user);

    /**
     * Find all tasks done between two given dates.
     *
     * @param startDate the lower bound
     * @param endDate the upper bound
     * @return the collection of tasks
     */
    @Query("SELECT t FROM Task t WHERE :startDate <= t.taskEnd AND :endDate >= t.taskEnd AND t.enabled = true")
    Collection<Task> allActivitiesBetweenDates(Date startDate, Date endDate);

}