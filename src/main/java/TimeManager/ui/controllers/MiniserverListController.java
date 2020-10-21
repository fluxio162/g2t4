package TimeManager.ui.controllers;

import TimeManager.model.Miniserver;
import TimeManager.model.Room;
import TimeManager.services.MiniserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Controller for the miniserver list view.
 *
 */
@Component
@Scope("view")
public class MiniserverListController implements Serializable {

    @Autowired
    private MiniserverService miniserverService;


    private Collection<Miniserver> filteredMiniservers;
    /**
     * Returns a list of all miniservers.
     *
     * @return list of all miniserver
     */

    public Collection<Miniserver> getFilteredMiniservers(){
        return this.filteredMiniservers;
    }

    public void setFilteredMiniservers(Collection<Miniserver> filteredMiniservers){
       this.filteredMiniservers = filteredMiniservers;
    }

    public Map<Miniserver, Room> getMiniserverRoomMap() {
        return miniserverService.getMiniserverRoomMap();
    }
}
