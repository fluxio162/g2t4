package TimeManager.ui.activitycontrollers;

import TimeManager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Controller for the all activities view.
 */
@Component
@Scope("view")
public class AllActivitiesController extends ActivityController {

    @Autowired
    private TaskService taskService;

    /**
     * Instantiates a new controller.
     */
    public AllActivitiesController(){ }

    /**
     * Instantiates a new controller.
     *
     * @param taskService the task service
     */
    public AllActivitiesController(TaskService taskService){
        this.taskService = taskService;
    }

    /**
     * Initializes current activities and generates a hash map with percentages.
     */
    @PostConstruct
    public void init() {
        Date today = new Date();
        Date aWeekAgo = new Date(today.getTime() - 7 * (1000 * 60 * 60 * 24));
        this.currentActivities = taskService.getListOfTasksBetweenDates(aWeekAgo, today);
        this.generateHashMapPercent(currentActivities);
    }
}

