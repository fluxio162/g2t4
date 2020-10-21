package TimeManager.ui.activitycontrollers;

import TimeManager.model.Task;
import TimeManager.services.TaskService;
import TimeManager.ui.beans.SessionInfoBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ActivityControllerClassTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    ActivityController activityController = new ActivityController();
    OwnActivityController ownActivityController = new OwnActivityController();
    TeamActivityController teamActivityController = new TeamActivityController();
    DepartmentTeamActivityController departmentTeamActivityController = new DepartmentTeamActivityController();

    @Test
    @DirtiesContext
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void determineWeekStartTest() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2020, 04, 07);
        Calendar weekStart = Calendar.getInstance();
        weekStart.set(2020, 04, 04);

        Assert.assertEquals("The beginning of the week could not be determined successfully. ", activityController.determineWeekStart(selectedDate), weekStart);

    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void determineWeekEndTest() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2020, 04, 07);
        Calendar weekEnd = Calendar.getInstance();
        weekEnd.set(2020, 04, 10);

        Assert.assertEquals("The end of the week could not be determined successfully.", activityController.determineWeekEnd(selectedDate), weekEnd);

    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void notInThisMonthTest(){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.add(Calendar.MONTH, -1);

        Assert.assertFalse("The check to see whether today's date is in today's month failed.", activityController.notInThisMonth(today.get(Calendar.YEAR), today.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void determineActivitiesTest(){
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2020, 04, 07);
        ownActivityController.executeSwitchView();
        Task task = new Task();
        task.setTaskId(17);

        Assert.assertFalse("The check whether the employee contains the corresponding task on the selected day has failed.", activityController.dailyActivities.contains(task));

    }
}