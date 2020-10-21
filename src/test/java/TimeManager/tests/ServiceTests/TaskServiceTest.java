package TimeManager.tests.ServiceTests;

import TimeManager.model.Task;
import TimeManager.model.TaskCategory;
import TimeManager.model.TaskChangeRequest;
import TimeManager.services.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

/**
 * Some very basic tests for {@link TaskService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TaskServiceTest {

     @Autowired
     UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TimeFlipService timeFlipService;

    @Autowired
    TaskService taskService;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateTask() {
        Task tobeCreatedTask = new Task();
        Date startDate = new Date(new Date().getTime()-10000);
        Date endDate = new Date(new Date().getTime()-5000);
        tobeCreatedTask.setTaskStart(startDate);
        tobeCreatedTask.setTaskEnd(endDate);
        tobeCreatedTask.setTaskId(99999);

        taskService.saveTask(tobeCreatedTask);


        Task freshlyLoadedTask = taskService.loadTask(99999);
        Assert.assertNotNull("No Task created.", freshlyLoadedTask);
        Assert.assertNotNull("CreateUser not set.", freshlyLoadedTask.getCreateUser());
        Assert.assertNotNull("CreateDate not set.", freshlyLoadedTask.getCreateDate());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testRequestUpdateTeamLeader() {
        Task toBeTestedTask = taskService.loadTask(59);
        Date newStartDate = new Date(toBeTestedTask.getTaskStart().getTime()+1000);
        Date newEndDate = new Date(toBeTestedTask.getTaskEnd().getTime()+1000);

        taskService.requestUpdate(toBeTestedTask, newStartDate, newEndDate, "Design");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testRequestDeleteTeamLeader() {
        Task toBeTestedTask = taskService.loadTask(42);

        taskService.requestDelete(toBeTestedTask);

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testRequestEmptyUpdate() {
        Task toBeTestedTask = taskService.loadTask(42);

        taskService.requestUpdate(toBeTestedTask, null, null, null);

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testAcceptRequestTeamLeader() {
        Task toBeTestedTask = taskService.loadTask(59);
        Date newStartDate = new Date(toBeTestedTask.getTaskStart().getTime()+1000);
        Date newEndDate = new Date(toBeTestedTask.getTaskEnd().getTime()+1000);

        taskService.requestUpdate(toBeTestedTask, newStartDate, newEndDate, "Design");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());

        for(TaskChangeRequest tcr : taskService.getAllTaskChangeRequestsFromTeam().values()){
            taskService.acceptRequest(tcr);
        }

        Task freshlyLoadedTask = taskService.loadTask(59);
        Assert.assertNotNull("UpdateUser not set.", freshlyLoadedTask.getUpdateUser());
        Assert.assertNotNull("UpdateDate not set.", freshlyLoadedTask.getUpdateDate());
        Assert.assertEquals("Start Date did not change.", newStartDate, freshlyLoadedTask.getTaskStart());
        Assert.assertEquals("End Date did not change.", newEndDate, freshlyLoadedTask.getTaskEnd());
        Assert.assertTrue("Task Category did not change.", freshlyLoadedTask.getTaskCategory().contains(TaskCategory.DESIGN));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testAcceptDeleteRequestTeamLeader() {
        Task toBeTestedTask = taskService.loadTask(59);

        taskService.requestUpdate(toBeTestedTask, null, null, "DELETE");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());

        for(TaskChangeRequest tcr : taskService.getAllTaskChangeRequestsFromTeam().values()){
            taskService.acceptRequest(tcr);
        }

        Task freshlyLoadedTask = taskService.loadTask(59);
        Assert.assertNotNull("DeleteUser not set.", freshlyLoadedTask.getDeleteUser());
        Assert.assertNotNull("DeleteDate not set.", freshlyLoadedTask.getDeleteDate());
        Assert.assertFalse("Task is still enabled.", freshlyLoadedTask.isEnabled());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user030", authorities = {"TEAM_LEADER"})
    public void testRefuseRequestTeamLeader() {
        Task toBeTestedTask = taskService.loadTask(59);
        Date newStartDate = new Date(toBeTestedTask.getTaskStart().getTime()+1000);
        Date newEndDate = new Date(toBeTestedTask.getTaskEnd().getTime()+1000);

        taskService.requestUpdate(toBeTestedTask, newStartDate, newEndDate, "Design");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromTeam());

        for(TaskChangeRequest tcr : taskService.getAllTaskChangeRequestsFromTeam().values()){
            taskService.refuseRequest(tcr);
        }

        Task freshlyLoadedTask = taskService.loadTask(59);
        Assert.assertNull("UpdateUser set.", freshlyLoadedTask.getUpdateUser());
        Assert.assertNull("UpdateDate set.", freshlyLoadedTask.getUpdateDate());
        Assert.assertEquals("Start Date did change.", toBeTestedTask.getTaskStart(), freshlyLoadedTask.getTaskStart());
        Assert.assertEquals("End Date did change.", toBeTestedTask.getTaskEnd(), freshlyLoadedTask.getTaskEnd());
        Assert.assertFalse("Task Category did change.", freshlyLoadedTask.getTaskCategory().contains(TaskCategory.DESIGN));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user003", authorities = {"DEPARTMENT_MANAGER"})
    public void testAcceptRequestDepartmentManager() {
        Task toBeTestedTask = taskService.loadTask(42);
        Date newStartDate = new Date(toBeTestedTask.getTaskStart().getTime()+1000);
        Date newEndDate = new Date(toBeTestedTask.getTaskEnd().getTime()+1000);

        taskService.requestUpdate(toBeTestedTask, newStartDate, newEndDate, "Design");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromDepartment());

        for(TaskChangeRequest tcr : taskService.getAllTaskChangeRequestsFromDepartment().values()){
            taskService.acceptRequest(tcr);
        }

        Task freshlyLoadedTask = taskService.loadTask(42);
        Assert.assertNotNull("UpdateUser not set.", freshlyLoadedTask.getUpdateUser());
        Assert.assertNotNull("UpdateDate not set.", freshlyLoadedTask.getUpdateDate());
        Assert.assertEquals("Start Date did not change.", newStartDate, freshlyLoadedTask.getTaskStart());
        Assert.assertEquals("End Date did not change.", newEndDate, freshlyLoadedTask.getTaskEnd());
        Assert.assertTrue("Task Category did not change.", freshlyLoadedTask.getTaskCategory().contains(TaskCategory.DESIGN));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user003", authorities = {"DEPARTMENT_MANAGER"})
    public void testRefuseRequestDepartmentManager() {
        Task toBeTestedTask = taskService.loadTask(59);
        Date newStartDate = new Date(toBeTestedTask.getTaskStart().getTime()+1000);
        Date newEndDate = new Date(toBeTestedTask.getTaskEnd().getTime()+1000);

        taskService.requestUpdate(toBeTestedTask, newStartDate, newEndDate, "Design");

        Assert.assertNotNull("No TaskChangeRequest created.", taskService.getAllTaskChangeRequestsFromDepartment());

        for(TaskChangeRequest tcr : taskService.getAllTaskChangeRequestsFromDepartment().values()){
            taskService.refuseRequest(tcr);
        }

        Task freshlyLoadedTask = taskService.loadTask(59);
        Assert.assertNull("UpdateUser set.", freshlyLoadedTask.getUpdateUser());
        Assert.assertNull("UpdateDate set.", freshlyLoadedTask.getUpdateDate());
        Assert.assertEquals("Start Date did change.", toBeTestedTask.getTaskStart(), freshlyLoadedTask.getTaskStart());
        Assert.assertEquals("End Date did change.", toBeTestedTask.getTaskEnd(), freshlyLoadedTask.getTaskEnd());
        Assert.assertFalse("Task Category did change.", freshlyLoadedTask.getTaskCategory().contains(TaskCategory.DESIGN));
    }

    @Test
    @WithMockUser(username = "user64", authorities = {"EMPLOYEE"})
    public void testLoadTaskByUser() {
        Assert.assertEquals("Wrong number of Tasks returned.", 68, taskService.loadTaskByUser(userService.loadUser("user64")).size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testLoadTaskByTeam() {
        Assert.assertEquals("Wrong number of Tasks returned.", 8, taskService.loadTaskByTeam(userService.loadUser("user030")).size());

    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testLoadAllTasks() {
        Assert.assertEquals("Wrong number of Tasks returned.", 339, taskService.getAllTasks().size());
    }
}
