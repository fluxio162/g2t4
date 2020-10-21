package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Department implements Persistable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String departmentName;

    @ManyToOne(optional = true)
    private User departmentManager;

    @ManyToMany(fetch= FetchType.EAGER)
    private Set<Team> team;

    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @ManyToOne(optional = true)
    private User updateUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;


    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentId) {
        this.departmentName = departmentId;
    }

    public User getDepartmentManager() {
        return departmentManager;
    }

    public void setDepartmentManager(User departmentManager) {
        this.departmentManager = departmentManager;
    }

    public Set<Team> getTeam() {
        return team;
    }

    public void setTeam(Set<Team> team) {
        this.team = team;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public User getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.departmentName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Department)) {
            return false;
        }
        final Department other = ((Department) obj);
        return Objects.equals(this.departmentName, other.departmentName);
    }
    @Override
    public String toString() {
        return departmentName;
    }

    @Override
    public String getId() { return departmentName; }

    public void setId(String id) { setDepartmentName(id);}

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}
