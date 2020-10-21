package TimeManager.ui.activitycontrollers;

import TimeManager.model.Department;
import TimeManager.model.Task;
import TimeManager.model.Team;
import TimeManager.model.User;
import TimeManager.services.TaskService;
import TimeManager.services.DepartmentService;
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
public class DepartmentTeamActivityController extends ActivityController implements Serializable {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private TeamService teamService;

    private String team = "Alle Teams";
    private List<Team> teams;

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
    private Collection<Task> allActivities = new ArrayList<>();

    User currentUser;

    Department department;

    /**
     * initializes all general information that can be filled at the start of the class (the current user, the
     * respective department, the teams in the department)
     */
    @PostConstruct
    public void init(){

        currentUser = sessionInfoBean.getCurrentUser();

        department = departmentService.findDepartmentByDepartmentManager(currentUser);

        teams = new ArrayList<Team>();

        Team allTeams = new Team();

        allTeams.setTeamName("Alle Teams");
        team = "Alle Teams";

        teams.add(allTeams);

        Iterator iterator = department.getTeam().iterator();

        while(iterator.hasNext()){
            teams.add((Team) iterator.next());
        }
    }

    /**
     * @param allActivitiesToOrder all the activities that are stored for the current user in the database
     * sets the attributes dailyActivities, weeklyActivities, monthlyActivities
     * to set attributes the methods determineWeekStart and determineWeekEnd are called
     */
    private void determineActivities(Collection<Task> allActivitiesToOrder){
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);

        calendar = cal;

        // store the year, month and day of the calendar object to compare it
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);

        int activityYear;
        int activityMonth;

        Calendar act = Calendar.getInstance();

        for (Task task:allActivities) {
            act.setTime(task.getTaskStart());

            activityYear = act.get(Calendar.YEAR);
            activityMonth = act.get(Calendar.MONTH);

            // if an activity started during the month where the day was selected but did not take place within the last 7 days
            if((activityYear == selectedYear && activityMonth == selectedMonth) && notInThisMonth(activityYear, activityMonth))
            {
                monthlyActivities.add(task);
            }
        }
    }

    /**
     * method gets executed when the switch view button is selected
     * checks first if a date was selected
     */
    public void executeSwitchView(){
        if(selectedDate != null){
            switchView();
        }
    }

    /**
     * This method is executed as soon as the user selects the Switch View button in the frontend
     */
    public void switchView(){

        clearActivityLists();

        if(team.equals("Alle Teams")) {

            for (Team theTeams : department.getTeam()) {
                Collection<Task> allActivitiesOfThisUser = null;
                for (User teamMember : theTeams.getTeamMember()) {
                    allActivitiesOfThisUser = taskService.loadTaskByUser(teamMember);
                }
                this.allActivities.addAll(allActivitiesOfThisUser);
            }

        }else{
            Team selectedTeam = teamService.findTeamByTeamName(team);

            for (User teamMember : selectedTeam.getTeamMember()) {
                this.allActivities = taskService.loadTaskByUser(teamMember);
            }
        }

        determineActivities(allActivities);

        generateHashMapPercent(monthlyActivities);

        allActivities.clear();

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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}