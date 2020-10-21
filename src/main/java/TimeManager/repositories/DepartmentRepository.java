package TimeManager.repositories;

import TimeManager.model.Team;
import TimeManager.model.Department;
import TimeManager.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing {@link Department} entities.
 */
public interface DepartmentRepository extends AbstractRepository<Department,Integer> {

    /**
     * Find first department by department name.
     *
     * @param departmentName the department name
     * @return the department
     */
    Department findFirstByDepartmentName(String departmentName);

    /**
     * Find all departments.
     *
     * @return the list of departments
     */
    @Query("SELECT d FROM Department d WHERE d.enabled = true")
    List<Department> findAll();

    /**
     * Find department by its manager.
     *
     * @param departmentManager the department manager
     * @return the department
     */
    @Query("SELECT d FROM Department d WHERE d.departmentManager = :departmentManager AND d.enabled = true")
    Department findByDepartmentManager(@Param("departmentManager") User departmentManager);

    /**
     * Find department for a given team.
     *
     * @param team the team
     * @return the department
     */
    @Query("SELECT d FROM Department d WHERE :team MEMBER OF d.team AND d.enabled = true")
    Department findByTeam(@Param("team") Team team);

}
