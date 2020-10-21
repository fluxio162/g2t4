package TimeManager.ui.controllers;

import TimeManager.model.Miniserver;
import TimeManager.services.MiniserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Controller to create new Miniserver.
 */
@Component
@Scope("view")
public class MiniserverCreationController implements Serializable {

    @Autowired
    private MiniserverService miniserverService;

    private Miniserver miniserver = new Miniserver();

    private int miniserverId;

    public MiniserverCreationController(){
    }

    public MiniserverCreationController(MiniserverService miniserverService){
        this.miniserverService = miniserverService;
    }

    public void setMiniserverId(int miniserverId) {
        this.miniserverId = miniserverId;
    }

    public int getMiniserverId() { return miniserverId = miniserverService.findMaxMiniserverId()+1;}

    public void setMiniserver(Miniserver miniserver) {
        this.miniserver = miniserver;
    }

    public Miniserver getMiniserver() {return miniserver; }

    /**
     * Action to create the new Miniserver.
     */
    public void doSaveMiniserver() {
        miniserver = this.miniserverService.createMiniserver(miniserver, miniserverId);
    }
}
