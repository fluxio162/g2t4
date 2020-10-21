package TimeManager.tests.ControllerTests;

import TimeManager.model.Department;
import TimeManager.model.Miniserver;
import TimeManager.services.*;
import TimeManager.ui.controllers.DepartmentCreationController;
import TimeManager.ui.controllers.DepartmentDetailController;
import TimeManager.ui.controllers.MiniserverCreationController;
import TimeManager.ui.controllers.MiniserverDetailController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DepartmentControllerTest {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;


    DepartmentDetailController departmentDetailController;

    DepartmentCreationController departmentCreationController;


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDepartmentDetailControllerUpdateDepartment() {
        departmentDetailController = new DepartmentDetailController(departmentService, userService, teamService);


        departmentDetailController.setDepartment(departmentService.loadDepartment("3"));
        departmentDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, departmentDetailController.getAvailableUser().size());
        Assert.assertEquals("Insufficient amount of Teams returned.", 5, departmentDetailController.getAvailableTeams().size());
        Assert.assertEquals("Wrong DepartmentLeader returned", "user005", departmentDetailController.getCurrentUser());

        departmentDetailController.setDepartmentTeams(Collections.singletonList("Team 5"));
        departmentDetailController.setSelectedUser("user003");
        departmentDetailController.doSaveDepartment();

        Department freshlyLoadedDepartment = departmentService.loadDepartment("3");
        Assert.assertEquals("Wrong DepartmentLeader assigned.", "user003", freshlyLoadedDepartment.getDepartmentManager().getUsername());
        Assert.assertTrue("Team 5 not in Department 3", freshlyLoadedDepartment.getTeam().contains(teamService.loadTeam("Team 5")));
        Assert.assertNotNull("UpdateUser not stored.", freshlyLoadedDepartment.getUpdateUser());
        Assert.assertNotNull("UpdateDate not stored.", freshlyLoadedDepartment.getUpdateDate());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDepartmentDetailControllerDeleteDepartment() {
        departmentDetailController = new DepartmentDetailController(departmentService, userService, teamService);


        departmentDetailController.setDepartment(departmentService.loadDepartment("5"));
        departmentDetailController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, departmentDetailController.getAvailableUser().size());
        Assert.assertEquals("Insufficient amount of Teams returned.", 5, departmentDetailController.getAvailableTeams().size());
        Assert.assertEquals("Wrong DepartmentLeader returned", "user007", departmentDetailController.getCurrentUser());


        departmentDetailController.doDeleteDepartment();

        Department freshlyDeletedDepartment = departmentService.loadDepartment("5");
        Assert.assertNotNull("DeleteUser not stored.", freshlyDeletedDepartment.getDeleteUser());
        Assert.assertNotNull("DeleteDate not stored.", freshlyDeletedDepartment.getDeleteDate());
        Assert.assertEquals("Wrong number of Departments returned.", 4, departmentService.getAllDepartments().size());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDepartmentCreationControllerCreateDepartment() {
        departmentCreationController = new DepartmentCreationController(departmentService, userService);

        departmentCreationController.init();

        Assert.assertEquals("Insufficient amount of User returned.", 52, departmentCreationController.getAvailableUser().size());

        Department toBeCreatedDepartment = new Department();
        toBeCreatedDepartment.setDepartmentName("Absolutely unique Department Name");

        departmentCreationController.setDepartment(toBeCreatedDepartment);
        departmentCreationController.validateDepartmentName();

        Assert.assertFalse("Department Name validation failed.", departmentCreationController.isDisableSaveButton());

        departmentCreationController.setSelectedUser("user003");
        departmentCreationController.doSaveDepartment();

        Department freshlyCreatedDepartment = departmentService.loadDepartment("Absolutely unique Department Name");
        Assert.assertNotNull("New Department could not be loaded from database.", freshlyCreatedDepartment);
        Assert.assertNotNull("CreateUser not stored.", freshlyCreatedDepartment.getCreateUser());
        Assert.assertNotNull("CreateDate not stored.", freshlyCreatedDepartment.getCreateDate());
    }
}
