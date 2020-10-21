package TimeManager.tests.ControllerTests;

import TimeManager.model.TimeFlip;
import TimeManager.services.*;
import TimeManager.ui.controllers.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TimeFlipControllerTest {

    @Autowired
    UserService userService;

    @Autowired
    TimeFlipService timeFlipService;


    TimeFlipDetailController timeFlipDetailController;

    TimeFlipCreationController timeFlipCreationController;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTimeFlipDetailControllerUpdateTimeFlip() {
        timeFlipDetailController = new TimeFlipDetailController(timeFlipService, userService);


        timeFlipDetailController.setTimeFlip(timeFlipService.loadTimeFlip(1));
        timeFlipDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, timeFlipDetailController.getAvailableUser().size());
        Assert.assertEquals("Wrong User attribute returned.", "user45", timeFlipDetailController.getCurrentUser());

        timeFlipDetailController.setSelectedUser("user003");
        timeFlipDetailController.doSaveTimeFlip();

        Assert.assertEquals("TimeFlip assigned to wrong User.", "user003", timeFlipService.loadTimeFlip(1).getUser().getUsername());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTimeFlipDetailControllerDeleteTimeFlip() {
        timeFlipDetailController = new TimeFlipDetailController(timeFlipService, userService);


        timeFlipDetailController.setTimeFlip(timeFlipService.loadTimeFlip(1));
        timeFlipDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, timeFlipDetailController.getAvailableUser().size());
        Assert.assertEquals("Wrong User attribute returned.", "user45", timeFlipDetailController.getCurrentUser());


        timeFlipDetailController.doDeleteTimeFlip();

        TimeFlip freshlyDeletedTimeFlip = timeFlipService.loadTimeFlip(1);
        Assert.assertNotNull("DeleteUser not stored.", freshlyDeletedTimeFlip.getDeleteUser());
        Assert.assertNotNull("DeleteDate not stored.", freshlyDeletedTimeFlip.getDeleteDate());
        Assert.assertEquals("Wrong number of TimeFlips returned.", 19, timeFlipService.getAllTimeFlips().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTimeFlipCreationControllerCreateTimeFlip() {
        timeFlipCreationController = new TimeFlipCreationController(timeFlipService, userService);

        timeFlipCreationController.setTimeFlip(new TimeFlip());
        timeFlipCreationController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, timeFlipCreationController.getAvailableUser().size());
        Assert.assertNull("SelectedUser not null.", timeFlipCreationController.getSelectedUser());

        int id =  timeFlipCreationController.getTimeFlipId();
        timeFlipCreationController.setTimeFlipId(id);
        timeFlipCreationController.setSelectedUser("noUser");
        timeFlipCreationController.doSaveTimeFlip();

        TimeFlip freshlyCreatedTimeFlip = timeFlipService.loadTimeFlip(id);
        Assert.assertNotNull("New TimeFlip could not be loaded from database.", freshlyCreatedTimeFlip);
        Assert.assertNotNull("CreateUser not stored.", freshlyCreatedTimeFlip.getCreateUser());
        Assert.assertNotNull("CreateDate not stored.", freshlyCreatedTimeFlip.getCreateDate());
    }

}
