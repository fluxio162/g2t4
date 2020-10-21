package TimeManager.tests.ControllerTests;


import TimeManager.model.User;
import TimeManager.model.Vacation;
import TimeManager.services.DepartmentService;
import TimeManager.services.TeamService;
import TimeManager.services.UserService;
import TimeManager.services.VacationService;
import TimeManager.ui.beans.SessionInfoBean;
import TimeManager.ui.controllers.VacationListController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VacationListControllerTest {

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    VacationListController vacationListController;

    public Vacation createTestVacation(User testUser, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd){
        Vacation testVacation = new Vacation();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, monthStart);
        calendar.set(Calendar.DAY_OF_MONTH, dayStart);
        calendar.set(Calendar.YEAR, yearStart);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart = calendar.getTime();

        calendar.set(Calendar.MONTH, monthEnd);
        calendar.set(Calendar.DAY_OF_MONTH, dayEnd);
        calendar.set(Calendar.YEAR, yearEnd);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd = calendar.getTime();

        testVacation.setVacationStart(vacationStart);
        testVacation.setVacationEnd(vacationEnd);
        testVacation.setEnabled(true);
        testVacation.setVacationId(this.vacationService.generateVacationId());
        testVacation.setCreateUser(testUser);
        testVacation.setCreateDate(vacationStart);


        return testVacation;
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializationVacationCreationController() {
        this.vacationListController = new VacationListController();
        this.vacationListController = new VacationListController(sessionInfoBean,vacationService,teamService, departmentService);
        Assert.assertNotNull("VacationCreationController could not be initialized", vacationListController);
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "employee", authorities = {"ADMIN", "EMPLOYEE"})
    public void testGetVacationOfUser(){

        this.vacationListController = new VacationListController(sessionInfoBean,vacationService,teamService, departmentService);

        User user = userService.loadUser("employee");
        Assert.assertNotNull("employee could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);


        Assertions.assertEquals(user,sessionInfoBean.getCurrentUser() );

        Assertions.assertTrue(this.vacationListController.getVacation().isEmpty());

        Vacation toBeCreatedVacation1 = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation2 = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation3 = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation4 = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);

        this.vacationService.saveVacation(toBeCreatedVacation1);
        this.vacationService.saveVacation(toBeCreatedVacation2);
        this.vacationService.saveVacation(toBeCreatedVacation3);
        this.vacationService.saveVacation(toBeCreatedVacation4);

        Assertions.assertNotNull(this.vacationListController.getVacation());
        Assertions.assertFalse(this.vacationListController.getVacation().isEmpty());
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "user030", authorities = {"ADMIN", "TEAM_LEADER"})
    public void testGetVacationOfEmployeeForTeamLeader(){

        this.vacationListController = new VacationListController(sessionInfoBean,vacationService,teamService, departmentService);

        User user = userService.loadUser("user030");
        Assert.assertNotNull("user030 could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User teamMember = userService.loadUser("user61");
        Assert.assertNotNull("teamMember could not be loaded from test data source", teamMember);

        Assertions.assertEquals(user,sessionInfoBean.getCurrentUser() );
        Assertions.assertTrue(this.vacationListController.getEmployeeVacation().isEmpty());

        Vacation toBeCreatedVacation1 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation2 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation3 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation4 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);

        this.vacationService.saveVacation(toBeCreatedVacation1);
        this.vacationService.saveVacation(toBeCreatedVacation2);
        this.vacationService.saveVacation(toBeCreatedVacation3);
        this.vacationService.saveVacation(toBeCreatedVacation4);

        Assertions.assertNotNull(this.vacationListController.getEmployeeVacation());
        Assertions.assertFalse(this.vacationListController.getEmployeeVacation().isEmpty());

        this.vacationListController.setFilteredVacations(new ArrayList<>());
        Assertions.assertTrue(this.vacationListController.getFilteredVacations().isEmpty());
    }
    @Test
    @DirtiesContext
    @WithMockUser(username = "user003", authorities = {"ADMIN", "DEPARTMENT_MANAGER"})
    public void testGetVacationOfEmployeeForDepartmentManager(){

        this.vacationListController = new VacationListController(sessionInfoBean,vacationService,teamService, departmentService);

        User user = userService.loadUser("user003");
        Assert.assertNotNull("user003 could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User teamMember = userService.loadUser("user61");
        Assert.assertNotNull("teamMember could not be loaded from test data source", teamMember);

        Assertions.assertEquals(user,sessionInfoBean.getCurrentUser() );
        Assertions.assertTrue(this.vacationListController.getEmployeeVacation().isEmpty());

        Vacation toBeCreatedVacation1 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation2 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation3 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);
        Vacation toBeCreatedVacation4 = createTestVacation(teamMember, 25, 5, 2042, 29, 5, 2042);

        this.vacationService.saveVacation(toBeCreatedVacation1);
        this.vacationService.saveVacation(toBeCreatedVacation2);
        this.vacationService.saveVacation(toBeCreatedVacation3);
        this.vacationService.saveVacation(toBeCreatedVacation4);

        Assertions.assertNotNull(this.vacationListController.getEmployeeVacation());
        Assertions.assertFalse(this.vacationListController.getEmployeeVacation().isEmpty());

        this.vacationListController.setFilteredVacations(new ArrayList<>());
        Assertions.assertTrue(this.vacationListController.getFilteredVacations().isEmpty());
    }
}
