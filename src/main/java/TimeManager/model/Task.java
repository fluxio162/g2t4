package TimeManager.model;

import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "task")
public class Task implements Persistable<Integer>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int taskId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date taskStart;
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskEnd;

    @ElementCollection(targetClass = TaskCategory.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Task_TaskCategory")
    @Enumerated(EnumType.STRING)
    private Set<TaskCategory> taskCategory;

    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @ManyToOne(optional = true)
    private User updateUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    boolean enabled;

    @ManyToOne(optional = true)
    private User deleteUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Date getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Date taskStart) {
        this.taskStart = taskStart;
    }

    public Date getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Date taskEnd) {
        this.taskEnd = taskEnd;
    }

    public Set<TaskCategory> getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(Set<TaskCategory> taskCategory) {
        this.taskCategory = taskCategory;
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

    public User getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.taskId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Task)) {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.taskId, other.taskId);
    }

    @Override
    public String toString() {
        return "at.qe.sepm.skeleton.model.User[ id=" + taskId + " ]";
    }

    @Override
    public Integer getId() {
        return getTaskId();
    }

    public void setId(int id) {
        setTaskId(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}
