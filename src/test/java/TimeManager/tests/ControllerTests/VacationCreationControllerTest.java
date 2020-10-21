package TimeManager.tests.ControllerTests;



import TimeManager.model.User;
import TimeManager.model.Vacation;
import TimeManager.services.UserService;
import TimeManager.services.VacationService;
import TimeManager.ui.controllers.VacationCreationController;
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

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VacationCreationControllerTest {
    @Autowired
    VacationService vacationService;

    @Autowired
    UserService userService;

    VacationCreationController vacationCreationController;

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
        this.vacationCreationController = new VacationCreationController();
        this.vacationCreationController = new VacationCreationController(vacationService);
        Assert.assertNotNull("VacationCreationController could not be initialized", vacationCreationController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN", "EMPLOYEE"})
    public void testCreateVacation() {
        this.vacationCreationController = new VacationCreationController(vacationService);

        User user = userService.loadUser("user64");
        Assert.assertNotNull("user64 could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        int vacationSize = this.vacationService.getAllVacation().size();

        Vacation vacation = createTestVacation(user, 25,5,2042, 28,5, 2042);
        Vacation vacation1 = createTestVacation(user, 25,5,1997, 28,5, 2042);
        Vacation vacation2 = createTestVacation(user, 25,5,2042, 28,5, 1997);
        Vacation vacation3 = createTestVacation(user, 26,11,2019, 26,11, 2019);

        Assertions.assertEquals(vacationSize, vacationService.getAllVacation().size());

        Vacation toBeCreatedVacation = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);

        vacationCreationController.setStartDate(toBeCreatedVacation.getVacationStart());
        vacationCreationController.setEndDate(toBeCreatedVacation.getVacationEnd());
        vacationCreationController.doSaveVacation();

        Assert.assertNotEquals("vacation not saved", vacationSize,this.vacationService.getAllVacation().size());
        Assert.assertNotNull(vacationCreationController.getDlgMessage());
        Assert.assertNotNull(vacationCreationController.getEndDate());
        Assert.assertNotNull(vacationCreationController.getStartDate());

        int vacationSize1 = this.vacationService.getAllVacation().size();


        vacationCreationController.setStartDate(null);
        vacationCreationController.setEndDate(null);
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());


        vacationCreationController.setStartDate(null);
        vacationCreationController.setEndDate(vacation.getVacationEnd());
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());


        vacationCreationController.setStartDate(vacation.getVacationStart());
        vacationCreationController.setEndDate(null);
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());

        vacationCreationController.setStartDate(vacation1.getVacationStart());
        vacationCreationController.setEndDate(vacation1.getVacationEnd());
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());


        vacationCreationController.setStartDate(vacation2.getVacationStart());
        vacationCreationController.setEndDate(vacation2.getVacationEnd());
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());

        vacationCreationController.setStartDate(vacation3.getVacationStart());
        vacationCreationController.setEndDate(vacation3.getVacationEnd());
        vacationCreationController.doSaveVacation();
        Assertions.assertEquals(vacationSize1, this.vacationService.getAllVacation().size());

        vacationCreationController.setDlgMessage("test");
        Assertions.assertEquals("test", vacationCreationController.getDlgMessage());

    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAuthority() {
        this.vacationCreationController = new VacationCreationController(vacationService);

        User user = userService.loadUser("user64");
        Assert.assertNotNull("user64 could not be loaded from test data source", user);
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Vacation toBeCreatedVacation = createTestVacation(user, 25, 5, 2042, 29, 5, 2042);

        vacationCreationController.setStartDate(toBeCreatedVacation.getVacationStart());
        vacationCreationController.setEndDate(toBeCreatedVacation.getVacationEnd());

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> vacationCreationController.doSaveVacation());
    }

}
