package TimeManager.ui.activitycontrollers;

import TimeManager.model.Task;
import TimeManager.model.Team;
import TimeManager.model.User;
import TimeManager.services.TaskService;
import TimeManager.services.TeamService;
import TimeManager.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

@Component
@Scope("view")
public class TeamActivityController extends ActivityController implements Serializable {

    @PostConstruct
    public void init(){
    }

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    /**
     * this integer is set according to the selection for the view
     * viewSelection = 1 -- shows view for the week ("Wochenansicht")
     * viewSelection = 2 -- shows view for the month ("Monatsansicht")
     */
    private int viewSelection = 0;

    /**
     * stores the by the user via the primeface datepicker entered day as a Date
     */
    private Date selectedDate;

    /**
     * stores the entered day as a Calendar object, this is necessary to find the week the day belongs to
     */
    private Calendar calendar;

    /**
     * allActivities: stores all the activities of one user from the database
     */
    private Collection<Task> allActivities;

    /**
     * @param allActivitiesToOrder all the activities that are stored for the current user in the database
     * sets the attributes dailyActivities, weeklyActivities, monthlyActivities
     * to set attributes the methods determineWeekStart and determineWeekEnd are called
     */
    private void determineActivities(Collection<Task> allActivitiesToOrder){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);

        calendar = cal;

        // store the year, month and day of the calendar object to compare it
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        Date monday = determineWeekStart(calendar).getTime();
        Date sunday = determineWeekEnd(calendar).getTime();

        int activityYear;
        int activityMonth;
        int activityDay;

        Calendar act = Calendar.getInstance();

        for (Task task:allActivities) {
            act.setTime(task.getTaskStart());

            activityYear = act.get(Calendar.YEAR);
            activityMonth = act.get(Calendar.MONTH);
            activityDay = act.get(Calendar.DAY_OF_MONTH);

            Date activityToCompareForWeeklyViewDate = new GregorianCalendar(activityYear, activityMonth, activityDay).getTime();   // used to compare for the weekly Activities

            // if an activity started between monday and sunday of the selected week but did not take place within the last 7 days
            if(activityToCompareForWeeklyViewDate.after(monday) && activityToCompareForWeeklyViewDate.before(sunday) && activityToCompareForWeeklyViewDate.before(determineWeekStart(today).getTime()))
            {
                weeklyActivities.add(task);
            }

            // if an activity started during the month where the day was selected but did not take place within the last 7 days
            if((activityYear == selectedYear && activityMonth == selectedMonth) && activityToCompareForWeeklyViewDate.before(determineWeekStart(today).getTime()))
            {
                monthlyActivities.add(task);
            }
        }
    }

    /**
     * This method is executed as soon as the user selects the Switch View button in the frontend
     */
    public void executeSwitchView(){
        if(selectedDate == null) {
            return;
        }
            clearActivityLists();

            User currentUser = sessionInfoBean.getCurrentUser();

            Team theTeam = teamService.findTeamByTeamLeader(currentUser);

            Set<User> membersOfTeam;
            membersOfTeam = theTeam.getTeamMember();

            for (User teamMember : membersOfTeam) {
                this.allActivities = taskService.loadTaskByUser(teamMember);
            }

            determineActivities(allActivities);

            showViewSelection(viewSelection);

    }

    public int getViewSelection() {
        return viewSelection;
    }

    public void setViewSelection(int viewSelection) {
        this.viewSelection = viewSelection;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setCurrentActivitiesPercentage(HashMap<String, Float> currentActivitiesPercentage) {
        this.currentActivitiesPercentage = currentActivitiesPercentage;
    }

}