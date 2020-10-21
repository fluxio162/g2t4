package TimeManager.ui.controllers;

import TimeManager.model.Department;
import TimeManager.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * Controller for the department list view.
 *
 */
@Component
@Scope("view")
public class DepartmentListController implements Serializable {

    @Autowired
    private DepartmentService departmentService;
    private Collection<Department> filteredDepartments;

    public Collection<Department> getDepartments() {
        return departmentService.getAllDepartments();
    }

    public Collection<Department> getFilteredDepartments(){
        return this.filteredDepartments;
    }

    public void setFilteredDepartments(Collection<Department> filteredDepartments){
       this.filteredDepartments = filteredDepartments;
    }
}
