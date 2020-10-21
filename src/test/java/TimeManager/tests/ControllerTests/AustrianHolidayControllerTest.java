package TimeManager.tests.ControllerTests;


import TimeManager.services.UserService;
import TimeManager.services.VacationService;
import TimeManager.ui.controllers.AustrianHolidayController;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AustrianHolidayControllerTest {

    @Autowired
    VacationService vacationService;

    @Autowired
    UserService userService;

    AustrianHolidayController austrianHolidayController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializationAustrianHolidayController() {
        this.austrianHolidayController = new AustrianHolidayController();
        this.austrianHolidayController = new AustrianHolidayController(vacationService);
        Assert.assertNotNull("VacationCreationController could not be initialized", austrianHolidayController);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetHoliday(){
        this.austrianHolidayController = new AustrianHolidayController(this.vacationService);
        Map<String, Date> holidays = austrianHolidayController.getHoliday();
        Assertions.assertNotNull(holidays);

        Assertions.assertTrue(holidays.containsKey("Neujahr"));
        Assertions.assertTrue(holidays.containsKey("Heilige Drei Könige"));
        Assertions.assertTrue(holidays.containsKey("Ostermontag"));
        Assertions.assertTrue(holidays.containsKey("Staatsfeiertag"));

        Assertions.assertTrue(holidays.containsKey("Christi Himmelfahrt"));
        Assertions.assertTrue(holidays.containsKey("Pfingstmontag"));
        Assertions.assertTrue(holidays.containsKey("Fronleichnam"));

        Assertions.assertTrue(holidays.containsKey("Mariä Himmelfahrt"));
        Assertions.assertTrue(holidays.containsKey("Nationalfeiertag"));
        Assertions.assertTrue(holidays.containsKey("Allerheilgen"));

        Assertions.assertTrue(holidays.containsKey("Maria Empfängnis"));
        Assertions.assertTrue(holidays.containsKey("Christtag"));
        Assertions.assertTrue(holidays.containsKey("Stefanitag"));

        Assertions.assertEquals(13, holidays.size());

        LocalDate localDate = holidays.get("Neujahr").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(2, localDate.getDayOfMonth());
        Assertions.assertEquals(1, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Heilige Drei Könige").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(7, localDate.getDayOfMonth());
        Assertions.assertEquals(1, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Ostermontag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(14, localDate.getDayOfMonth());
        Assertions.assertEquals(4, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Staatsfeiertag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(2, localDate.getDayOfMonth());
        Assertions.assertEquals(5, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Pfingstmontag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(2, localDate.getDayOfMonth());
        Assertions.assertEquals(6, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Christi Himmelfahrt").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(22, localDate.getDayOfMonth());
        Assertions.assertEquals(5, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Fronleichnam").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(12, localDate.getDayOfMonth());
        Assertions.assertEquals(6, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Mariä Himmelfahrt").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(16, localDate.getDayOfMonth());
        Assertions.assertEquals(8, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Nationalfeiertag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(27, localDate.getDayOfMonth());
        Assertions.assertEquals(10, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Allerheilgen").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(2, localDate.getDayOfMonth());
        Assertions.assertEquals(11, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Maria Empfängnis").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(9, localDate.getDayOfMonth());
        Assertions.assertEquals(12, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Christtag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(26, localDate.getDayOfMonth());
        Assertions.assertEquals(12, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());

        localDate = holidays.get("Stefanitag").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(27, localDate.getDayOfMonth());
        Assertions.assertEquals(12, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testSetHolidays(){
        this.austrianHolidayController = new AustrianHolidayController(this.vacationService);
        Map<String, Date> holidays = new HashMap<>();

        Assertions.assertEquals(13 ,austrianHolidayController.getHoliday().size());

        holidays.put("test1", new Date());
        holidays.put("test2", new Date());
        holidays.put("test3", new Date());
        holidays.put("test4", new Date());

        austrianHolidayController.setHoliday(holidays);


    }
}
