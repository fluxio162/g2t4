package TimeManager.ui.controllers;

import TimeManager.model.Department;
import TimeManager.services.DepartmentService;
import TimeManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * Controller for creating new Department.
 */
@Component
@Scope("view")
public class DepartmentCreationController implements Serializable {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    private Department department = new Department();

    private boolean disableSaveButton = true;

    private List<String> availableUser;
    private String selectedUser;

    public DepartmentCreationController(){}

    public DepartmentCreationController(DepartmentService departmentService, UserService userService){
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @PostConstruct
    public void init(){
        availableUser = userService.getAvailableUser();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<String> getAvailableUser() {
        return availableUser;
    }

    public void setAvailableUser(List<String> availableUser) {
        this.availableUser = availableUser;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public boolean isDisableSaveButton() {
        return disableSaveButton;
    }

    public void setDisableSaveButton(boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    /**
     * Action to create the new Department and save persistent.
     */
    public void doSaveDepartment() {
        department = this.departmentService.createDepartment(department, selectedUser);
    }

    /**
     * Enables save button on department_management.xhtml if entered department name is unique in database.
     */
    public void validateDepartmentName() {
        disableSaveButton = departmentService.validateDepartmentName(department.getDepartmentName());
    }
}
