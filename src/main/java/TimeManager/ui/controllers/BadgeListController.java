package TimeManager.ui.controllers;

import TimeManager.model.Badge;
import TimeManager.model.User;
import TimeManager.services.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Controller for the badge list view.
 */
@Component
@Scope("view")
public class BadgeListController implements Serializable {

    @Autowired
    private BadgeService badgeService;

    private Collection<Badge> filteredBadges;

    private Collection<Badge> employeeBadges = new ArrayList<>();

    /**
     * Instantiates a new controller.
     */
    public BadgeListController(){
    }

    /**
     * Instantiates a new controller.
     *
     * @param badgeService the badge service
     */
    public BadgeListController(BadgeService badgeService){
        this.badgeService = badgeService;
    }


    /**
     * Returns a list of all badges.
     *
     * @return all badges
     */
    public Collection<Badge> getBadges() {
        return badgeService.getAllBadges();
    }

    /**
     * Returns a list of filtered badges.
     *
     * @return filtered badges
     */
    public Collection<Badge> getFilteredBadges(){
        return this.filteredBadges;
    }

    /**
     * Sets filtered badges.
     *
     * @param filteredBadges the filtered badges
     */
    public void setFilteredBadges(Collection<Badge> filteredBadges) {
        this.filteredBadges = filteredBadges;
    }


    /**
     * Get all badges from a given user.
     *
     * @param user the user
     * @return the collection badges
     */
    public Collection<Badge> getAllBadgesFromUser (User user){
        return badgeService.getAllFromUser(user);
    }

    public Collection<Badge> getEmployeeBadges (){
        if(employeeBadges.size() == 0){
            employeeBadges.addAll(badgeService.getEmployeeBadges());
        }
        return this.employeeBadges;
    }

    /**
     * Get badges from the previous week.
     *
     * @return the collection of badges
     */
    public Collection<Badge> getLatestBadges (){
        return badgeService.getNewestEntries(new Date(new Date().getTime()-7*(1000 * 60 * 60 * 24)));
    }
}
