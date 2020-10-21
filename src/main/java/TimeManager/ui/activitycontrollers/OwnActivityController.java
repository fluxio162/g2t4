package TimeManager.ui.activitycontrollers;

import TimeManager.model.TaskCategory;
import TimeManager.model.Task;
import TimeManager.model.User;
import TimeManager.services.TaskService;
import TimeManager.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

@Component
@Scope("view")
public class OwnActivityController extends ActivityController implements Serializable {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    /***
     * this integer is set according to the selection for the view
     * viewSelection = 0 -- shows view for the day ("Tagesansicht") (which is set as the standard)
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
     * currentActivitiesPercentage: stores the activities of the currentActivities as well as the percentage
     */
    private HashMap<String, Float> currentActivitiesPercentage = new HashMap<>();

    private List<TaskCategory> availableTaskCategories;
    private List<String> availableTaskCategoriesString = new ArrayList<>();

    private String selectedTaskCategory;

    /**
     * initializes the List with available task categories, associated strings and adds the string names to the
     * associated categories in the list
     */
    @PostConstruct
    public void init(){
        availableTaskCategories = new ArrayList<>(Arrays.asList(TaskCategory.values()));
        availableTaskCategoriesString = new ArrayList<>();
        availableTaskCategories.forEach(r -> availableTaskCategoriesString.add(r.toString()));
    }

    /**
     * for the date selected by the user in the frontend (saved in selectedDate), the day, week and month data are
     * saved in the lists available for this (declared in the ActivityController class)
     * the showViewSelection method is then called, which fills the HashMap with the percentage values depending on the
     * selection (daily, weekly or monthly view)
     */
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    private void determineActivities(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);

        calendar = cal;

        // store the year, month and day of the calendar object to compare it
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
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

            if (activityYear == selectedYear && activityMonth == selectedMonth && activityDay == selectedDay)    // if an activity started during the selected day
            {
                dailyActivities.add(task);
            }

            if (activityToCompareForWeeklyViewDate.after(monday) && activityToCompareForWeeklyViewDate.before(sunday))    // if an activity started between monday and sunday of the selected week
            {
                weeklyActivities.add(task);
            }

            if (activityYear == selectedYear && activityMonth == selectedMonth)    // if an activity started during the month where the day was selected
            {
                monthlyActivities.add(task);
            }
        }

        showViewSelection(viewSelection);
    }

    /**
     * This method is executed as soon as the user selects the Switch View button in the frontend
     */
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public void executeSwitchView(){

        if(selectedDate == null) {
            return;
        }
            clearActivityLists();

            User currentUser = sessionInfoBean.getCurrentUser();

            this.allActivities = taskService.loadTaskByUser(currentUser);

            determineActivities();

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

    public String getSelectedTaskCategory() {
        return selectedTaskCategory;
    }

    public List<TaskCategory> getAvailableTaskCategories() {
        return availableTaskCategories;
    }
}