package TimeManager.tests.ServiceTests;

import TimeManager.model.User;
import TimeManager.model.Vacation;
import TimeManager.services.VacationService;
import TimeManager.services.UserService;
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
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VacationServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    VacationService vacationService;

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
    public Vacation createTestVacation(User testUser, int dayStart, int monthStart, int yearStart, int dayEnd, int monthEnd, int yearEnd, Date createDate){
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
        testVacation.setCreateDate(createDate);

        return testVacation;
    }



    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "EMPLOYEE"})
    public void getAllVacation() {
        User user1 = userService.loadUser("user45");
        Collection<Vacation> allVacations = this.vacationService.getAllVacation();
        int size = allVacations.size();
        int loopSize = 0;

        for(Vacation vacation: allVacations){
            Assertions.assertNotNull(vacation);
            loopSize++;
        }

        Assertions.assertEquals(size, loopSize);

        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user1, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user1, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user1, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user1, 9, 7,2019, 18, 7, 2019);


        this.vacationService.saveVacation(testVacation1);
        this.vacationService.saveVacation(testVacation2);
        this.vacationService.saveVacation(testVacation3);
        this.vacationService.saveVacation(testVacation4);
        this.vacationService.saveVacation(testVacation5);
        this.vacationService.saveVacation(testVacation6);
        this.vacationService.saveVacation(testVacation7);
        this.vacationService.saveVacation(testVacation8);
        this.vacationService.saveVacation(testVacation9);
        this.vacationService.saveVacation(testVacation10);

        Collection<Vacation> allVacations1 = this.vacationService.getAllVacation();
        int size1 = allVacations1.size();
        int loopSize1 = 0;


        for(Vacation vacation:allVacations1){
            Assertions.assertNotNull(vacation);
            loopSize1++;
        }
        Assertions.assertEquals(size1, loopSize1);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "testUser", authorities = {"ADMIN", "EMPLOYEE"})
    public void getAllFromUser() {

        User user1 = userService.loadUser("user45");
        User user2 = userService.loadUser("user46");
        User user3 = userService.loadUser("user47");

        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user2, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user2, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user2, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user3, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user3, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user3, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user3, 9, 7,2019, 18, 7, 2019);

        this.vacationService.saveVacation(testVacation1);
        this.vacationService.saveVacation(testVacation2);
        this.vacationService.saveVacation(testVacation3);
        this.vacationService.saveVacation(testVacation4);
        this.vacationService.saveVacation(testVacation5);
        this.vacationService.saveVacation(testVacation6);
        this.vacationService.saveVacation(testVacation7);
        this.vacationService.saveVacation(testVacation8);
        this.vacationService.saveVacation(testVacation9);
        this.vacationService.saveVacation(testVacation10);


        Collection<Vacation> vacationUser1 = this.vacationService.getAllFromUser(user1);

        for (Vacation tmpVacation : vacationUser1) {
            Assertions.assertNotNull(tmpVacation);
            Assertions.assertEquals(user1, tmpVacation.getCreateUser());
        }

        Collection<Vacation> vacationUser2 = this.vacationService.getAllFromUser(user2);

        for (Vacation tmpVacation : vacationUser2) {
            Assertions.assertNotNull(tmpVacation);
            Assertions.assertEquals(user2, tmpVacation.getCreateUser());
        }

        Collection<Vacation> vacationUser3 = this.vacationService.getAllFromUser(user3);

        for (Vacation tmpVacation : vacationUser3) {
            Assertions.assertNotNull(tmpVacation);
            Assertions.assertEquals(user3, tmpVacation.getCreateUser());
        }

    }

    @Test
    public void loadVacation() {


    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE", "ADMIN"})
    public void saveVacation() {
        User user1 = userService.loadUser("employee");
        User user2 = userService.loadUser("user50");

        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user1, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user1, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user1, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user1, 9, 7,2019, 18, 7, 2019);

        this.vacationService.saveVacation(testVacation1);
        this.vacationService.saveVacation(testVacation2);
        this.vacationService.saveVacation(testVacation3);
        this.vacationService.saveVacation(testVacation4);
        this.vacationService.saveVacation(testVacation5);
        this.vacationService.saveVacation(testVacation6);
        this.vacationService.saveVacation(testVacation7);
        this.vacationService.saveVacation(testVacation8);
        this.vacationService.saveVacation(testVacation9);
        this.vacationService.saveVacation(testVacation10);


        Assertions.assertEquals(testVacation1.getVacationId(), vacationService.loadVacation(testVacation1.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation1.getCreateUser(), vacationService.loadVacation(testVacation1.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation1.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation1.getCreateDate().getTime(), vacationService.loadVacation(testVacation1.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation1.getVacationStart().getTime(), vacationService.loadVacation(testVacation1.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation1.getVacationEnd().getTime(), vacationService.loadVacation(testVacation1.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation1, vacationService.loadVacation(testVacation1.getVacationId()));


        Assertions.assertEquals(testVacation2.getVacationId(), vacationService.loadVacation(testVacation2.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation2.getCreateUser(), vacationService.loadVacation(testVacation2.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation2.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation2.getCreateDate().getTime(), vacationService.loadVacation(testVacation2.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation2.getVacationStart().getTime(), vacationService.loadVacation(testVacation2.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation2.getVacationEnd().getTime(), vacationService.loadVacation(testVacation2.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation2, vacationService.loadVacation(testVacation2.getVacationId()));


        Assertions.assertEquals(testVacation3.getVacationId(), vacationService.loadVacation(testVacation3.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation3.getCreateUser(), vacationService.loadVacation(testVacation3.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation3.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation3.getCreateDate().getTime(), vacationService.loadVacation(testVacation3.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation3.getVacationStart().getTime(), vacationService.loadVacation(testVacation3.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation3.getVacationEnd().getTime(), vacationService.loadVacation(testVacation3.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation3, vacationService.loadVacation(testVacation3.getVacationId()));


        Assertions.assertEquals(testVacation4.getVacationId(), vacationService.loadVacation(testVacation4.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation4.getCreateUser(), vacationService.loadVacation(testVacation4.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation4.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation4.getCreateDate().getTime(), vacationService.loadVacation(testVacation4.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation4.getVacationStart().getTime(), vacationService.loadVacation(testVacation4.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation4.getVacationEnd().getTime(), vacationService.loadVacation(testVacation4.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation4, vacationService.loadVacation(testVacation4.getVacationId()));


        Assertions.assertEquals(testVacation5.getVacationId(), vacationService.loadVacation(testVacation5.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation5.getCreateUser(), vacationService.loadVacation(testVacation5.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation5.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation5.getCreateDate().getTime(), vacationService.loadVacation(testVacation5.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation5.getVacationStart().getTime(), vacationService.loadVacation(testVacation5.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation5.getVacationEnd().getTime(), vacationService.loadVacation(testVacation5.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation5, vacationService.loadVacation(testVacation5.getVacationId()));


        Assertions.assertEquals(testVacation6.getVacationId(), vacationService.loadVacation(testVacation6.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation6.getCreateUser(), vacationService.loadVacation(testVacation6.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation6.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation6.getCreateDate().getTime(), vacationService.loadVacation(testVacation6.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation6.getVacationStart().getTime(), vacationService.loadVacation(testVacation6.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation6.getVacationEnd().getTime(), vacationService.loadVacation(testVacation6.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation6, vacationService.loadVacation(testVacation6.getVacationId()));


        Assertions.assertEquals(testVacation7.getVacationId(), vacationService.loadVacation(testVacation7.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation7.getCreateUser(), vacationService.loadVacation(testVacation7.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation7.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation7.getCreateDate().getTime(), vacationService.loadVacation(testVacation7.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation7.getVacationStart().getTime(), vacationService.loadVacation(testVacation7.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation7.getVacationEnd().getTime(), vacationService.loadVacation(testVacation7.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation7, vacationService.loadVacation(testVacation7.getVacationId()));



        Assertions.assertEquals(testVacation8.getVacationId(), vacationService.loadVacation(testVacation8.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation8.getCreateUser(), vacationService.loadVacation(testVacation8.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation8.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation8.getCreateDate().getTime(), vacationService.loadVacation(testVacation8.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation8.getVacationStart().getTime(), vacationService.loadVacation(testVacation8.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation8.getVacationEnd().getTime(), vacationService.loadVacation(testVacation8.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation8, vacationService.loadVacation(testVacation8.getVacationId()));


        Assertions.assertEquals(testVacation9.getVacationId(), vacationService.loadVacation(testVacation9.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation9.getCreateUser(), vacationService.loadVacation(testVacation9.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation9.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation9.getCreateDate().getTime(), vacationService.loadVacation(testVacation9.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation9.getVacationStart().getTime(), vacationService.loadVacation(testVacation9.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation9.getVacationEnd().getTime(), vacationService.loadVacation(testVacation9.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation9, vacationService.loadVacation(testVacation9.getVacationId()));


        Assertions.assertEquals(testVacation10.getVacationId(), vacationService.loadVacation(testVacation10.getVacationId()).getVacationId());
        Assertions.assertEquals(testVacation10.getCreateUser(), vacationService.loadVacation(testVacation10.getVacationId()).getCreateUser());
        Assertions.assertTrue(vacationService.loadVacation(testVacation10.getVacationId()).isEnabled());
        Assertions.assertEquals(testVacation10.getCreateDate().getTime(), vacationService.loadVacation(testVacation10.getVacationId()).getCreateDate().getTime());
        Assertions.assertEquals(testVacation10.getVacationStart().getTime(), vacationService.loadVacation(testVacation10.getVacationId()).getVacationStart().getTime());
        Assertions.assertEquals(testVacation10.getVacationEnd().getTime(), vacationService.loadVacation(testVacation10.getVacationId()).getVacationEnd().getTime());
        Assertions.assertEquals(testVacation10, vacationService.loadVacation(testVacation10.getVacationId()));

        Vacation testVacation13 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019, null);
        Vacation testVacation14 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019, null);
        Vacation testVacation15 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020, null);

        this.vacationService.saveVacation(testVacation13);
        this.vacationService.saveVacation(testVacation14);
        this.vacationService.saveVacation(testVacation15);

        Assertions.assertNotNull(this.vacationService.loadVacation(testVacation13.getVacationId()));
        Assertions.assertNotNull(this.vacationService.loadVacation(testVacation14.getVacationId()));
        Assertions.assertNotNull(this.vacationService.loadVacation(testVacation15.getVacationId()));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE", "ADMIN"})
    public void deleteVacation() {
        User user1 = userService.loadUser("employee");

        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user1, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user1, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user1, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user1, 9, 7,2019, 18, 7, 2019);

        Assertions.assertNotNull(testVacation1);
        Assertions.assertNotNull(testVacation2);
        Assertions.assertNotNull(testVacation3);
        Assertions.assertNotNull(testVacation4);
        Assertions.assertNotNull(testVacation5);
        Assertions.assertNotNull(testVacation6);
        Assertions.assertNotNull(testVacation7);
        Assertions.assertNotNull(testVacation8);
        Assertions.assertNotNull(testVacation9);
        Assertions.assertNotNull(testVacation10);


        this.vacationService.deleteVacation(testVacation1);
        this.vacationService.deleteVacation(testVacation2);
        this.vacationService.deleteVacation(testVacation3);
        this.vacationService.deleteVacation(testVacation4);
        this.vacationService.deleteVacation(testVacation5);
        this.vacationService.deleteVacation(testVacation6);
        this.vacationService.deleteVacation(testVacation7);
        this.vacationService.deleteVacation(testVacation8);
        this.vacationService.deleteVacation(testVacation9);
        this.vacationService.deleteVacation(testVacation10);

        Assertions.assertNull(this.vacationService.loadVacation(testVacation1.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation2.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation3.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation4.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation5.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation6.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation7.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation8.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation9.getVacationId()));
        Assertions.assertNull(this.vacationService.loadVacation(testVacation10.getVacationId()));
    }

    @Test
    public void getAustrianHolidayCalender() {
        Map<String, Date> holidays = this.vacationService.getAustrianHolidayCalender();
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
    public void getEasterMondayDate() {
        LocalDate localDate;

        localDate = this.vacationService.getEasterMondayDate(2020).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(14, localDate.getDayOfMonth());
        Assertions.assertEquals(4, localDate.getMonthValue());
        Assertions.assertEquals(2020, localDate.getYear());


        localDate = this.vacationService.getEasterMondayDate(2021).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(6, localDate.getDayOfMonth());
        Assertions.assertEquals(4, localDate.getMonthValue());
        Assertions.assertEquals(2021, localDate.getYear());


        localDate = this.vacationService.getEasterMondayDate(2022).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(19, localDate.getDayOfMonth());
        Assertions.assertEquals(4, localDate.getMonthValue());
        Assertions.assertEquals(2022, localDate.getYear());


        localDate = this.vacationService.getEasterMondayDate(2027).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(30, localDate.getDayOfMonth());
        Assertions.assertEquals(3, localDate.getMonthValue());
        Assertions.assertEquals(2027, localDate.getYear());
}


    @DirtiesContext
    @Test
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE", "ADMIN"})
    public void createVacation() {
        User user1 = userService.loadUser("employee");
        Date testDate = new Date();
        Vacation vacation01 = new Vacation();
        vacation01.setVacationId(this.vacationService.generateVacationId());
        vacation01.setDeleteDate(testDate);
        vacation01.setDeleteUser(user1);
        vacation01.setStatus("hello world");


        Assertions.assertNotNull(vacation01.getDeleteDate());
        Assertions.assertNotNull(vacation01.getDeleteUser());
        Assertions.assertNotNull(vacation01.getStatus());

        Assertions.assertEquals(user1, vacation01.getDeleteUser());
        Assertions.assertEquals(testDate, vacation01.getDeleteDate());
        Assertions.assertEquals("hello world", vacation01.getStatus());


        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user1, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user1, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user1, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user1, 9, 7,2019, 18, 7, 2019);

        Assertions.assertNotNull(testVacation1);
        Assertions.assertNotNull(testVacation2);
        Assertions.assertNotNull(testVacation3);
        Assertions.assertNotNull(testVacation4);
        Assertions.assertNotNull(testVacation5);
        Assertions.assertNotNull(testVacation6);
        Assertions.assertNotNull(testVacation7);
        Assertions.assertNotNull(testVacation8);
        Assertions.assertNotNull(testVacation9);
        Assertions.assertNotNull(testVacation10);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart = calendar.getTime();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd = calendar.getTime();
        Vacation vacation = this.vacationService.createVacation(Calendar.getInstance(), vacationStart, vacationEnd);

        Assertions.assertNotNull(vacation);
        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), null,null));
        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart,null));
        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), null, vacationEnd));
        Assertions.assertNotNull(vacation.getVacationId());
        Assertions.assertNotNull(vacation.getVacationEnd());
        Assertions.assertNotNull(vacation.getVacationStart());
        Assertions.assertNotNull(vacation.getCreateUser());
        Assertions.assertTrue(vacation.isEnabled());


        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        calendar.set(Calendar.YEAR, 1934);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart1 = calendar.getTime();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.set(Calendar.YEAR, 1934);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd1 = calendar.getTime();

        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart1, vacationEnd1));


        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart2 = calendar.getTime();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.set(Calendar.YEAR, 1956);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd2 = calendar.getTime();

        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart2, vacationEnd2));


        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 28);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart3 = calendar.getTime();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd3 = calendar.getTime();

        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart3, vacationEnd3));


        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 28);
        calendar.set(Calendar.YEAR, 1945);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationStart4 = calendar.getTime();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        calendar.set(Calendar.YEAR, 2042);
        calendar.set(Calendar.HOUR, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        Date vacationEnd4 = calendar.getTime();

        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart4, vacationEnd4));

        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        calendar.set(Calendar.YEAR, 2019);


        Date vacationStart5 = calendar.getTime();

        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        calendar.set(Calendar.YEAR, 2019);

        Date vacationEnd5 = calendar.getTime();

        Assertions.assertNull(this.vacationService.createVacation(Calendar.getInstance(), vacationStart5, vacationEnd5));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "testUser", authorities = {"EMPLOYEE", "ADMIN"})
    public void generateVacationId() {
        User user1 = userService.loadUser("user50");
        List<Vacation> listVacation = new ArrayList<>();
        List<Integer> listId = new ArrayList<>();

        Vacation testVacation1 = createTestVacation(user1, 16, 7,2019, 25, 7, 2019);
        Vacation testVacation2 = createTestVacation(user1, 26, 9,2019, 3, 10, 2019);
        Vacation testVacation3 = createTestVacation(user1, 2, 4,2019, 6, 4, 2019);
        Vacation testVacation4 = createTestVacation(user1, 1, 12,2019, 10, 12, 2019);
        Vacation testVacation5 = createTestVacation(user1, 7, 8,2019, 14, 8, 2019);
        Vacation testVacation6 = createTestVacation(user1, 29, 11,2019, 8, 11, 2019);
        Vacation testVacation7 = createTestVacation(user1, 13, 3,2019, 26, 3, 2019);
        Vacation testVacation8 = createTestVacation(user1, 2, 2,2020, 5, 2, 2020);
        Vacation testVacation9 = createTestVacation(user1, 7, 1,2019, 17, 1, 2019);
        Vacation testVacation10 = createTestVacation(user1, 9, 7,2019, 18, 7, 2019);

        listVacation.add(testVacation1);
        listVacation.add(testVacation2);
        listVacation.add(testVacation3);
        listVacation.add(testVacation4);
        listVacation.add(testVacation5);
        listVacation.add(testVacation6);
        listVacation.add(testVacation7);
        listVacation.add(testVacation8);
        listVacation.add(testVacation9);
        listVacation.add(testVacation10);

        for(Vacation vacation: listVacation){
            Assertions.assertFalse(listId.contains(vacation.getVacationId()));
            listId.add(vacation.getVacationId());
        }


    }

    @Test
    public void getHolidayList() {
        this.vacationService.getAustrianHolidayCalender();
        List<Date> holidayList = this.vacationService.getHolidayList();
        Assertions.assertNotNull(holidayList);

        for(Date date: holidayList){
            Assertions.assertNotNull(date);
        }
    }

    @Test
    public void setHolidayList() {
        List<Date> list = new ArrayList<>();

        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();

        list.add(date1);
        list.add(date2);
        list.add(date3);

        this.vacationService.setHolidayList(list);

        for(Date date : this.vacationService.getHolidayList()){
            Assertions.assertTrue(list.contains(date));
        }
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUnauthorizedLoadVacation(){
        User user = this.userService.loadUser("admin");

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> this.vacationService.loadVacation(12));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUnauthorizedSaveVacation(){
        User user = this.userService.loadUser("admin");

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> this.vacationService.saveVacation(null));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUnauthorizedCreateVacation(){
        User user = this.userService.loadUser("admin");

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> this.vacationService.createVacation(Calendar.getInstance(), null,null));

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUnauthorizedDeleteVacation(){
        User user = this.userService.loadUser("admin");

        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> this.vacationService.deleteVacation(null));
    }
}