package TimeManager.tests.ServiceTests;

import TimeManager.model.Department;
import TimeManager.model.User;
import TimeManager.services.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

/**
 * Some very basic tests for {@link DepartmentService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DepartmentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TimeFlipService timeFlipService;


    @Test
    @WithMockUser(username = "testUser", authorities = {"ADMIN"})
    public void testDataInitialization() {
        Assert.assertEquals("Insufficient amount of departments initialized for test data source", 5, departmentService.getAllDepartments().size());
        for (Department department : departmentService.getAllDepartments()) {
            if ("1".equals(department.getDepartmentName())) {
                Assert.assertNotNull("Department \"1\" does not have a createUser defined", department.getCreateUser());
                Assert.assertNotNull("Department \"1\" does not have a createDate defined", department.getCreateDate());
                Assert.assertNull("Department \"1\" has a updateUser defined", department.getUpdateUser());
                Assert.assertNull("Department \"1\" has a updateDate defined", department.getUpdateDate());
            } else if ("3".equals(department.getDepartmentName())) {
                Assert.assertNotNull("Department \"3\" does not have a createUser defined", department.getCreateUser());
                Assert.assertNotNull("Department \"3\" does not have a createDate defined", department.getCreateDate());
                Assert.assertNull("Department \"3\" has a updateUser defined", department.getUpdateUser());
                Assert.assertNull("Department \"3\" has a updateDate defined", department.getUpdateDate());
            } else if ("5".equals(department.getDepartmentName())) {
                Assert.assertNotNull("Department \"5\" does not have a createUser defined", department.getCreateUser());
                Assert.assertNotNull("Department \"5\" does not have a createDate defined", department.getCreateDate());
                Assert.assertNull("Department \"5\" has a updateUser defined", department.getUpdateUser());
                Assert.assertNull("Department \"5\" has a updateDate defined", department.getUpdateDate());
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteDepartment() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        Department toBeDeletedDepartment = departmentService.loadDepartment("1");
        Assert.assertNotNull("Department 1 could not be loaded from test data source", toBeDeletedDepartment);

        departmentService.deleteDepartment(toBeDeletedDepartment);

        Assert.assertEquals("No Department has been deleted after calling DepartmentService.deleteDepartment", 4, departmentService.getAllDepartments().size());
        Assert.assertEquals("admin is not deleteUser", adminUser, toBeDeletedDepartment.getDeleteUser());
        Assert.assertNotNull("DeleteDate is not set", toBeDeletedDepartment.getDeleteDate());
        Assert.assertFalse("Department 1 is still enabled", toBeDeletedDepartment.isEnabled());
        Assert.assertNull("Department 1 hast still DepartmentManager assigned.", toBeDeletedDepartment.getDepartmentManager());
        Assert.assertTrue("Department 1 hast still Teams assigned.", toBeDeletedDepartment.getTeam().isEmpty());
        Assert.assertNull("user003 is still in Department 1", userService.loadUser("user003").getDepartment());
        Assert.assertNull("user030 is still in Department 1", userService.loadUser("user030").getDepartment());
        Assert.assertNull("user61 is still in Department 1", userService.loadUser("user61").getDepartment());

        for (Department remainingDepartment : departmentService.getAllDepartments()) {
            Assert.assertNotEquals("Deleted Department 1 could still be loaded from test data source via DepartmentService.getAllDepartments", toBeDeletedDepartment.getDepartmentName(), remainingDepartment.getDepartmentName());
        }
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateDepartment() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Department toBeUpdatedDepartment2 = departmentService.loadDepartment("2");
        Assert.assertNotNull("Department 2 could not be loaded from test data source", toBeUpdatedDepartment2);

        Assert.assertNull("Department 2 has a updateUser defined", toBeUpdatedDepartment2.getUpdateUser());
        Assert.assertNull("Department 2 has a updateDate defined", toBeUpdatedDepartment2.getUpdateDate());



        departmentService.updateDepartment(toBeUpdatedDepartment2, "user009", Collections.singletonList("Team 3"));

        Department freshlyLoadedDepartment2 = departmentService.loadDepartment("2");
        Assert.assertNotNull("Department 2 could not be loaded from test data source after being saved", freshlyLoadedDepartment2);
        Assert.assertNotNull("Department 2 does not have a updateUser defined after being saved", freshlyLoadedDepartment2.getUpdateUser());
        Assert.assertEquals("Department 2 has wrong updateUser set", adminUser, freshlyLoadedDepartment2.getUpdateUser());
        Assert.assertNotNull("Department 2 does not have a updateDate defined after being saved", freshlyLoadedDepartment2.getUpdateDate());
        Assert.assertTrue("Department 2 does not have Team 3 assigned.", freshlyLoadedDepartment2.getTeam().contains(teamService.loadTeam("Team 3")));
        Assert.assertEquals("Department 2 has wrong department manager assigned.", "user009", freshlyLoadedDepartment2.getDepartmentManager().getUsername());
        Assert.assertFalse("Department 3 still hast Team 3 assigned.", departmentService.loadDepartment("3").getTeam().contains(teamService.loadTeam("Team 3")));


        Department toBeUpdatedDepartment4 = departmentService.loadDepartment("4");
        Assert.assertNotNull("Department 4 could not be loaded from test data source", toBeUpdatedDepartment4);

        Assert.assertNull("Department 4 has a updateUser defined", toBeUpdatedDepartment4.getUpdateUser());
        Assert.assertNull("Department 4 has a updateDate defined", toBeUpdatedDepartment4.getUpdateDate());

        departmentService.updateDepartment(toBeUpdatedDepartment4, "user008", new ArrayList<String>(Arrays.asList("Team 4", "Team 5")));

        Department freshlyLoadedDepartment4 = departmentService.loadDepartment("4");
        Assert.assertNotNull("Department 4 could not be loaded from test data source after being saved", freshlyLoadedDepartment4);
        Assert.assertNotNull("Department 4 does not have a updateUser defined after being saved", freshlyLoadedDepartment4.getUpdateUser());
        Assert.assertEquals("Department 4 has wrong updateUser set", adminUser, freshlyLoadedDepartment4.getUpdateUser());
        Assert.assertNotNull("Department 4 does not have a updateDate defined after being saved", freshlyLoadedDepartment4.getUpdateDate());
        Assert.assertTrue("Department 4 does not have Team 4 assigned.", freshlyLoadedDepartment4.getTeam().contains(teamService.loadTeam("Team 4")));
        Assert.assertTrue("Department 4 does not have Team 5 assigned.", freshlyLoadedDepartment4.getTeam().contains(teamService.loadTeam("Team 5")));
        Assert.assertEquals("Department 2 has wrong department manager assigned.", "user008", freshlyLoadedDepartment4.getDepartmentManager().getUsername());
        Assert.assertFalse("Department 5 still has Team 5 assigned.", departmentService.loadDepartment("5").getTeam().contains(teamService.loadTeam("Team 5")));

        toBeUpdatedDepartment4 = departmentService.loadDepartment("4");
        Assert.assertNotNull("Department 4 could not be loaded from test data source", toBeUpdatedDepartment4);

        departmentService.updateDepartment(toBeUpdatedDepartment4, "user008", Collections.singletonList("Team 4"));

        freshlyLoadedDepartment4 = departmentService.loadDepartment("4");
        Assert.assertNotNull("Department 4 could not be loaded from test data source after being saved", freshlyLoadedDepartment4);
        Assert.assertNotNull("Department 4 does not have a updateUser defined after being saved", freshlyLoadedDepartment4.getUpdateUser());
        Assert.assertEquals("Department 4 has wrong updateUser set", adminUser, freshlyLoadedDepartment4.getUpdateUser());
        Assert.assertNotNull("Department 4 does not have a updateDate defined after being saved", freshlyLoadedDepartment4.getUpdateDate());
        Assert.assertTrue("Department 4 does not have Team 4 assigned.", freshlyLoadedDepartment4.getTeam().contains(teamService.loadTeam("Team 4")));
        Assert.assertEquals("Department 2 has wrong department manager assigned.", "user008", freshlyLoadedDepartment4.getDepartmentManager().getUsername());
        Assert.assertFalse("Department 4 still has Team 5 assigned.", freshlyLoadedDepartment4.getTeam().contains(teamService.loadTeam("Team 5")));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateDepartment() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Department toBeCreatedDepartment = new Department();
        toBeCreatedDepartment.setDepartmentName("Department 10");
        departmentService.createDepartment(toBeCreatedDepartment, "user010");

        Department freshlyCreatedDepartment = departmentService.loadDepartment("Department 10");
        Assert.assertNotNull("New Department could not be loaded from test data source after being saved", freshlyCreatedDepartment);
        Assert.assertNotNull("Department 10 does not have a createUser defined after being saved", freshlyCreatedDepartment.getCreateUser());
        Assert.assertEquals("Department 10 has wrong createUser set", adminUser, freshlyCreatedDepartment.getCreateUser());
        Assert.assertNotNull("Department 10 does not have a createDate defined after being saved", freshlyCreatedDepartment.getCreateDate());
        Assert.assertEquals("Department 10 has wrong department manager assigned.", "user010", freshlyCreatedDepartment.getDepartmentManager().getUsername());
        Assert.assertEquals("Department 10 has wrong department name stored.", "Department 10", freshlyCreatedDepartment.getDepartmentName());
        Assert.assertTrue("Department 10 is not enabled.", freshlyCreatedDepartment.isEnabled());
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateDepartmentWithAlreadyAssignedDepartmentManager() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Department toBeCreatedDepartment = new Department();
        toBeCreatedDepartment.setDepartmentName("Department 10");
        departmentService.createDepartment(toBeCreatedDepartment, "user003");

        Department freshlyCreatedDepartment = departmentService.loadDepartment("Department 10");
        Assert.assertNotNull("New Department could not be loaded from test data source after being saved", freshlyCreatedDepartment);
        Assert.assertNotNull("Department 10 does not have a createUser defined after being saved", freshlyCreatedDepartment.getCreateUser());
        Assert.assertEquals("Department 10 has wrong createUser set", adminUser, freshlyCreatedDepartment.getCreateUser());
        Assert.assertNotNull("Department 10 does not have a createDate defined after being saved", freshlyCreatedDepartment.getCreateDate());
        Assert.assertEquals("Department 10 has wrong department manager assigned.", "user003", freshlyCreatedDepartment.getDepartmentManager().getUsername());
        Assert.assertEquals("Department 10 has wrong department name stored.", "Department 10", freshlyCreatedDepartment.getDepartmentName());
        Assert.assertTrue("Department 10 is not enabled.", freshlyCreatedDepartment.isEnabled());
        Assert.assertNull("User003 is still assigned to Department 1", departmentService.loadDepartment("1").getDepartmentManager());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingDepartmentWithEmptyDepartmentName() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Department toBeCreatedDepartment = new Department();
       departmentService.createDepartment(toBeCreatedDepartment, "user011");
        Assert.assertNull("Department could be loaded from database", departmentService.loadDepartment(toBeCreatedDepartment.getDepartmentName()));
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreatingDepartmentWithAlreadyExistingDepartmentName() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Department toBeCreatedDepartment = new Department();
        toBeCreatedDepartment.setDepartmentName("1");
        departmentService.createDepartment(toBeCreatedDepartment, "user011");
        Assert.assertNotEquals("Department 1 has new department manager assigned.", "user011", departmentService.loadDepartment("1").getDepartmentManager().getUsername());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadDepartments() {
        for (Department department : departmentService.getAllDepartments()) {
            Assert.fail("Call to departmentService.getAllDepartments should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadDepartment() {
        for (Department department : departmentService.getAllDepartments()) {
            Assert.fail("Call to departmentService.getAllDepartments should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadDepartments() {
        Department department = departmentService.loadDepartment("admin");
        Assert.fail("Call to departmentService.loadDepartment should not work without proper authorization for other users than the authenticated one");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadDepartment() {
        Department department = departmentService.loadDepartment("1");
        Assert.assertEquals("Call to departmentService.loadDepartment returned wrong Department", "1", department.getDepartmentName());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveDepartment() {
        departmentService.saveDepartment(new Department());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteUDepartment() {
        departmentService.deleteDepartment(new Department());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedCreateDepartment() {
        departmentService.createDepartment(new Department(), null);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedUpdateDepartment() {
        departmentService.updateDepartment(new Department(), "user015", null);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindDepartmentByDepartmentManager() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong Department returned", "user003", departmentService.findDepartmentByDepartmentManager(userService.loadUser("user003")).getDepartmentManager().getUsername());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testFindAvailableDepartments() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        Assert.assertEquals("Wrong number of Departments returned", 5, departmentService.getAvailableDepartments().size());
    }

    @Test
    public void testValidateDepartmentName(){
        Assert.assertTrue("Empty DepartmentName is valid.", departmentService.validateDepartmentName(""));
        Assert.assertTrue("Already used Department Name is valid.", departmentService.validateDepartmentName("1"));
        Assert.assertFalse("Valid DepartmentName is not valid.", departmentService.validateDepartmentName("Department 91"));
    }
}
