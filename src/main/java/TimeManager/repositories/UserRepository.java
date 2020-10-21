package TimeManager.repositories;

import TimeManager.model.*;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link User} entities.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public interface UserRepository extends AbstractRepository<User, String> {

    /**
     * Find first user by username.
     *
     * @param username the username
     * @return the user
     */
    User findFirstByUsername(String username);

    /**
     * Find all users.
     *
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    List<User> findAll();

    /**
     * Find all users that were deleted.
     *
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = false")
    List<User> findAllDeleted();

    /**
     * Find all users that have a given notification interval selected.
     *
     * @param notificationInterval the notification interval
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.notificationCategory = :notificationInterval AND u.enabled = true")
    List<User> findAllWithNotificationInterval(String notificationInterval);

    /**
     * Find all users with the given whole name.
     *
     * @param wholeName the whole name
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName AND u.enabled = true")
    List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

    /**
     * Find all users that have a given user role.
     *
     * @param role the user role
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles AND u.enabled = true")
    List<User> findByRole(@Param("role") UserRole role);

    /**
     * Find all users that own a time flip.
     *
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.timeFlip IS NOT NULL AND u.enabled = true")
    List<User> findUserWithTimeFlip();

    /**
     * Find user that owns the given time flip.
     *
     * @param timeFlip the time flip
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.timeFlip = :timeFlip AND u.enabled = true")
    User findUserByTimeFlip(@Param("timeFlip") TimeFlip timeFlip);

    /**
     * Find all users that work in the given room.
     *
     * @param room the room
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.room = :room AND u.enabled = true")
    List<User> findUserInRoom(@Param("room") Room room);

    /**
     * Find all users (team members) of a team.
     *
     * @param team the team
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.team = :team AND u.enabled = true")
    List<User> findUserInTeam(@Param("team") Team team);

    /**
     * Find all users (department members) of a department.
     *
     * @param department the department
     * @return the list of users
     */
    @Query("SELECT u FROM User u WHERE u.department = :department AND u.enabled = true")
    List<User> findUserInDepartment(@Param("department") Department department);
}
