package TimeManager.tests.ActivityControllerTests;

import TimeManager.ui.activitycontrollers.AllActivitiesController;
import TimeManager.services.TaskService;
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
public class AllActivitiesControllerTest {
    @Autowired
    TaskService taskService;

    AllActivitiesController allActivitiesController;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testInitializeAllActivitiesController() {
        this.allActivitiesController = new AllActivitiesController();
        this.allActivitiesController = new AllActivitiesController(taskService);
        allActivitiesController.init();
        Assert.assertNotNull("AllActivitiesController could not be initialized", allActivitiesController);
    }

}
