package TimeManager.tests.ControllerTests;

import TimeManager.model.User;
import TimeManager.model.Vacation;
import TimeManager.services.VacationService;
import TimeManager.ui.controllers.VacationDetailController;
import TimeManager.services.UserService;
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
public class VacationDetailControllerTest {
    @Autowired
    VacationService vacationService;

    @Autowired
    UserService userService;

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

    VacationDetailController vacationDetailController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializeVacationDetailController() {
        this.vacationDetailController = new VacationDetailController();
        this.vacationDetailController = new VacationDetailController(vacationService);
        Assert.assertNotNull("VacationDetailController could not be initialized", vacationDetailController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN", "EMPLOYEE"})
    public void testCreateVacation() {
        this.vacationDetailController = new VacationDetailController(vacationService);
        User user = userService.loadUser("employee");
        Assert.assertNotNull("employee could not be loaded from test data source", user);


        Vacation toBeCreatedVacation = createTestVacation(user, 25, 5, 2042,27,5,2042);

        vacationDetailController.save(toBeCreatedVacation);
        vacationDetailController.setVacation(toBeCreatedVacation);
        vacationDetailController.doSaveVacation();

        Vacation loadedVacation = this.vacationService.loadVacation(toBeCreatedVacation.getVacationId());

        Assert.assertNotNull("could not load vacation",loadedVacation);
        Assert.assertEquals("vacation id is not equal", toBeCreatedVacation.getId(),loadedVacation.getId());
        Assert.assertTrue("vacation is not enabled", loadedVacation.isEnabled());
        Assert.assertEquals("create user is not equal", toBeCreatedVacation.getCreateUser(), loadedVacation.getCreateUser());

        vacationDetailController.doSaveStatusAndVacation("Bestätigt");
        vacationDetailController.doSaveStatusAndVacation("Abgelehnt");
    }
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN", "EMPLOYEE"})
    public void testDeleteVacation() {
        this.vacationDetailController = new VacationDetailController(vacationService);
        User user = userService.loadUser("employee");
        Assert.assertNotNull("employee could not be loaded from test data source", user);


        Vacation toBeCreatedVacation = createTestVacation(user, 25, 5, 2042,27,5,2042);

        vacationDetailController.save(toBeCreatedVacation);
        vacationDetailController.setVacation(toBeCreatedVacation);
        vacationDetailController.doSaveVacation();

        Vacation loadedVacation = this.vacationService.loadVacation(toBeCreatedVacation.getVacationId());

        Assert.assertNotNull("could not load vacation",loadedVacation);
        Assert.assertEquals("vacation id is not equal", toBeCreatedVacation.getId(),loadedVacation.getId());
        Assert.assertTrue("vacation is not enabled", loadedVacation.isEnabled());
        Assert.assertEquals("create user is not equal", toBeCreatedVacation.getCreateUser(), loadedVacation.getCreateUser());

        vacationDetailController.doDeleteVacation();
        Assertions.assertNull(vacationDetailController.getVacation());
    }
}