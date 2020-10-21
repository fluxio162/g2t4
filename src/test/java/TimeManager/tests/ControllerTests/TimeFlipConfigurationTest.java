package TimeManager.tests.ControllerTests;

import TimeManager.services.TimeFlipService;
import TimeManager.services.UserService;
import TimeManager.ui.controllers.TimeFlipConfigurationController;
import TimeManager.ui.controllers.UserCreationController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.dnd.Droppable;
import org.primefaces.event.DragDropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Some very basic tests for {@link UserCreationController}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TimeFlipConfigurationTest {

    @Autowired
    UserService userService;

    @Autowired
    TimeFlipService timeFlipService;


    TimeFlipConfigurationController timeFlipConfigurationController;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testTimeFlipConfiguration() {
        timeFlipConfigurationController = new TimeFlipConfigurationController(timeFlipService);
        timeFlipConfigurationController.setTimeFlip(timeFlipService.loadTimeFlip(1));

        Map<Integer, String> configuration = new HashMap<>(timeFlipConfigurationController.getConfiguration());
        Assert.assertNull("Side 1 already configured.", configuration.get(1));
        Assert.assertNull("Side 2 already configured.", configuration.get(2));
        Assert.assertNull("Side 3 already configured.", configuration.get(3));
        Assert.assertNull("Side 4 already configured.", configuration.get(4));
        Assert.assertNull("Side 5 already configured.", configuration.get(5));
        Assert.assertNull("Side 6 already configured.", configuration.get(6));
        Assert.assertNull("Side 7 already configured.", configuration.get(7));
        Assert.assertNull("Side 8 already configured.", configuration.get(8));
        Assert.assertNull("Side 9 already configured.", configuration.get(9));
        Assert.assertNull("Side 10 already configured.", configuration.get(10));
        Assert.assertNull("Side 11 already configured.", configuration.get(11));
        Assert.assertNull("Side 0 already configured.", configuration.get(0));

        Assert.assertEquals("Insufficient amount of Tasks returned.", 12, timeFlipConfigurationController.getTaskList().size());


        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side1", "Sonstiges"));
        Assert.assertEquals("Too many sides configured", 1, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side2", "Projektmanagement"));
        Assert.assertEquals("Too many sides configured", 2, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side3", "Fortbildung"));
        Assert.assertEquals("Too many sides configured", 3, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side4", "Kundenbesprechung"));
        Assert.assertEquals("Too many sides configured", 4, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side5", "Meeting (intern)"));
        Assert.assertEquals("Too many sides configured", 5, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side6", "Fehleranalyse und -korrektur"));
        Assert.assertEquals("Too many sides configured", 6, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side7", "Dokumentation"));
        Assert.assertEquals("Too many sides configured", 7, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side8", "Testen"));
        Assert.assertEquals("Too many sides configured", 8, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side9", "Implementierung"));
        Assert.assertEquals("Too many sides configured", 9, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side10", "Design"));
        Assert.assertEquals("Too many sides configured", 10, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side11", "Konzeption"));
        Assert.assertEquals("Too many sides configured", 11, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.onTaskDrop(new DragDropEvent(new Droppable(), new AjaxBehavior(), null, "cubeForm:side0", "Pause/Dienstfrei"));
        Assert.assertEquals("Too many sides configured", 12, timeFlipConfigurationController.getSelectedSideList().size());

        timeFlipConfigurationController.doSaveTimeFlip();

        Map<Integer, String> newConfiguration = new HashMap<>(timeFlipService.loadTimeFlip(1).getTimeFlipConfiguration());
        Assert.assertEquals("Wrong configuration for side 1", "Sonstiges", newConfiguration.get(1));
        Assert.assertEquals("Wrong configuration for side 2", "Projektmanagement", newConfiguration.get(2));
        Assert.assertEquals("Wrong configuration for side 3", "Fortbildung", newConfiguration.get(3));
        Assert.assertEquals("Wrong configuration for side 4", "Kundenbesprechung", newConfiguration.get(4));
        Assert.assertEquals("Wrong configuration for side 5", "Meeting (intern)", newConfiguration.get(5));
        Assert.assertEquals("Wrong configuration for side 6", "Fehleranalyse und -korrektur", newConfiguration.get(6));
        Assert.assertEquals("Wrong configuration for side 7", "Dokumentation", newConfiguration.get(7));
        Assert.assertEquals("Wrong configuration for side 8", "Testen", newConfiguration.get(8));
        Assert.assertEquals("Wrong configuration for side 9", "Implementierung", newConfiguration.get(9));
        Assert.assertEquals("Wrong configuration for side 10", "Design", newConfiguration.get(10));
        Assert.assertEquals("Wrong configuration for side 11", "Konzeption", newConfiguration.get(11));
        Assert.assertEquals("Wrong configuration for side 0", "Pause/Dienstfrei", newConfiguration.get(0));

    }

}
