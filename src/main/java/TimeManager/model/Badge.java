package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Entity representing a badge.
 */
@Entity
@Table(name = "badge")
public class Badge implements Serializable, Persistable<Integer> {
    private static final long serialVersionUID = 1L;

    @Id
    private int badgeId;

    @ManyToOne(optional = true)
    private User user;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    boolean enabled;

    private float timeSpentOnTask;
    private String badgeTitle;
    private File img;


    public int getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User createUser) {
        this.user = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Date getDeleteDate() { return deleteDate; }

    public void setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate; }

    public User getDeleteUser() { return deleteUser; }

    public void setDeleteUser(User deleteUser) { this.deleteUser = deleteUser; }

    public String getBadgeTitle() { return badgeTitle; }

    public void setBadgeTitle(String badgeTitle) { this.badgeTitle = badgeTitle; }

    public File getImg() { return img; }

    public void setImg(File img) { this.img = img; }

    public float getTimeSpentOnTask() { return timeSpentOnTask; }

    public void setTimeSpentOnTask(float timeSpentOnTask) { this.timeSpentOnTask = timeSpentOnTask; }

    @Override
    public Integer getId() { return badgeId; }

    @Override
    public boolean isNew() { return false; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.badgeId);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Badge badge = (Badge) o;
        return badgeId == badge.badgeId &&
                Objects.equals(user, badge.user) &&
                Objects.equals(createDate, badge.createDate);
    }
}