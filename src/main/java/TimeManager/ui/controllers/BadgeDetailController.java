package TimeManager.ui.controllers;


import TimeManager.model.Badge;
import TimeManager.services.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller for the badge detail view.
 */
@Component
@Scope("view")
public class BadgeDetailController implements Serializable {

    @Autowired
    private BadgeService badgeService;

    /**
     * Attribute to cache the currently displayed badge
     */
    private Badge badge;

    /**
     * Instantiates a new controller.
     *
     */
    public BadgeDetailController(){}

    /**
     * Instantiates a new controller.
     *
     * @param badgeService the badge service
     */
    public BadgeDetailController(BadgeService badgeService){
        this.badgeService = badgeService;
    }


    /**
     * Sets the currently displayed badge and reloads it form db. This badge is
     * targeted by any further calls of
     * {@link #doReloadBadge()}, {@link #doSaveBadge()} and
     * {@link #doDeleteBadge()}.
     *
     * @param badge the badge
     */
    public void setBadge(Badge badge) {
        this.badge = badge;
        doReloadBadge();
    }

    /**
     * Returns the currently displayed badge.
     *
     * @return badge
     */
    public Badge getBadge() {
        return badge;
    }

    /**
     * Action to force a reload of the currently displayed badge.
     */
    public void doReloadBadge() {
        badge = badgeService.loadBadge(badge.getBadgeId());
    }

    /**
     * Action to save the currently displayed badge.
     */
    public void doSaveBadge() {
        badge = this.badgeService.saveBadge(badge);
    }

    /**
     * Save a given badge.
     *
     * @param badge the badge
     */
    public void save(Badge badge){
        this.badgeService.saveBadge(badge);
    }

    /**
     * Action to delete the currently displayed badge.
     */
    public void doDeleteBadge() {
        this.badgeService.deleteBadge(badge);
        badge = null;
    }
}
