package TimeManager.ui.controllers;

import TimeManager.services.TimeFlipService;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Controller for the timeFlip list view.
 *
 */
@Component
@Scope("view")
public class TimeFlipListController implements Serializable {

    @Autowired
    private TimeFlipService timeFlipService;

    private Collection<TimeFlip> filteredTimeFlips;

    /**
     * Returns a list of all timeFlips.
     *
     * @return list of all timeFlips
     */
    public Collection<TimeFlip> getFilteredTimeFlips(){
        return this.filteredTimeFlips;
    }

    public void setFilteredTimeFlips(Collection<TimeFlip> filteredTimeFlips){
       this.filteredTimeFlips = filteredTimeFlips;
    }

    public Map<TimeFlip, User> getTimeFlipUserMap() {
        return timeFlipService.getTimeFlipUserMap();
    }
}
