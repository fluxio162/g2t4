package TimeManager.repositories;

import TimeManager.model.Badge;
import TimeManager.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Repository for managing {@link Badge} entities.
 */
public interface BadgeRepository extends AbstractRepository <Badge, String>{
    /**
     * Find first badge by badge id.
     *
     * @param badgeId the badge id
     * @return the badge
     */
    Badge findFirstByBadgeId (int badgeId);

    /**
     * Find all badges from user.
     *
     * @param user the user
     * @return the list of badges
     */
    @Query("SELECT b FROM Badge b WHERE :user = b.user AND b.enabled = true")
    List<Badge> findAllFromUser(@Param("user") User user);

    /**
     * Find all badges created after a given date.
     *
     * @param date the creation date
     * @return the list of badges
     */
    @Query("SELECT b FROM Badge b WHERE :date < b.createDate AND b.enabled = true")
    List<Badge> findNewestEntries(@Param("date") Date date);

    /**
     * Find all badges created between two given dates.
     *
     * @param startDate the lower bound
     * @param endDate the upper bound
     * @return the collection of badges
     */
    @Query("SELECT b FROM Badge b WHERE :startDate <= b.createDate AND :endDate >= b.createDate AND b.enabled = true")
    Collection<Badge> allBadgesBetweenDates(Date startDate, Date endDate);

    /**
     * Find all badges created between two given dates for a given user.
     *
     * @param startDate the lower bound
     * @param endDate the upper bound
     * @param user the user
     * @return the collection of badges
     */
    @Query("SELECT b FROM Badge b WHERE :startDate <= b.createDate AND :endDate >= b.createDate AND :user = b.user AND b.enabled = true")
    Collection<Badge> allBadgesBetweenDatesFromUser(Date startDate, Date endDate, User user);

}
