package TimeManager.tests.ControllerTests;

import TimeManager.model.Miniserver;
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
public class MiniserverControllerTest {

    @Autowired
    RoomService roomService;

    @Autowired
    MiniserverService miniserverService;


    MiniserverDetailController miniserverDetailController;

    MiniserverCreationController miniserverCreationController;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testMiniserverDetailControllerUpdateMiniserver() {
        miniserverDetailController = new MiniserverDetailController(miniserverService, roomService);


        miniserverDetailController.setMiniserver(miniserverService.loadMiniserver(1));
        miniserverDetailController.init();

        Assert.assertEquals("Insufficient amount of Rooms returned.", 5, miniserverDetailController.getAvailableRooms().size());
        Assert.assertEquals("Wrong Room attribute returned.", "1", miniserverDetailController.getCurrentRoom());

        miniserverDetailController.setSelectedRoom("2");
        miniserverDetailController.doSaveMiniserver();

        Assert.assertEquals("Miniserver assigned to wrong Room", 2, miniserverService.loadMiniserver(1).getRoom().getRoomId());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testMiniserverDetailControllerDeleteMiniserver() {
        miniserverDetailController = new MiniserverDetailController(miniserverService, roomService);


        miniserverDetailController.setMiniserver(miniserverService.loadMiniserver(1));
        miniserverDetailController.init();

        Assert.assertEquals("Insufficient amount of Rooms returned.", 5, miniserverDetailController.getAvailableRooms().size());
        Assert.assertEquals("Wrong Room attribute returned.", "1", miniserverDetailController.getCurrentRoom());


        miniserverDetailController.doDeleteMiniserver();

        Miniserver freshlyDeletedMiniserver = miniserverService.loadMiniserver(1);
        Assert.assertNotNull("DeleteUser not stored.", freshlyDeletedMiniserver.getDeleteUser());
        Assert.assertNotNull("DeleteDate not stored.", freshlyDeletedMiniserver.getDeleteDate());
        Assert.assertEquals("Wrong number of Miniservers returned.", 4, miniserverService.getAllMiniservers().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testMiniserverCreationControllerCreateMiniserver() {
        miniserverCreationController = new MiniserverCreationController(miniserverService);

        miniserverCreationController.setMiniserver(new Miniserver());
        int id = miniserverCreationController.getMiniserverId();
        miniserverCreationController.setMiniserverId(id);
        miniserverCreationController.doSaveMiniserver();

        Miniserver freshlyCreatedMiniserver = miniserverService.loadMiniserver(id);
        Assert.assertNotNull("New Miniserver could not be loaded from database.", freshlyCreatedMiniserver);
        Assert.assertNotNull("CreateUser not stored.", freshlyCreatedMiniserver.getCreateUser());
        Assert.assertNotNull("CreateDate not stored.", freshlyCreatedMiniserver.getCreateDate());
    }
}
