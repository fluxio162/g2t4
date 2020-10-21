package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


/**
 * Entity representing vacation.
 *
 */
@Entity
@Table(name = "vacation")
public class Vacation implements Serializable, Persistable<Integer> {

    @Id
    private int vacationId;

    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private Date vacationStart;
    private Date vacationEnd;

    boolean enabled;

    private String status= "Ausstehend";

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
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

    public Date getVacationStart() {
        return vacationStart;
    }

    public void setVacationStart(Date vacationStart) {
        this.vacationStart = vacationStart;
    }

    public Date getVacationEnd() {
        return vacationEnd;
    }

    public void setVacationEnd(Date vacationEnd) {
        this.vacationEnd = vacationEnd;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return vacationId == vacation.vacationId &&
                Objects.equals(createUser, vacation.createUser) &&
                Objects.equals(createDate, vacation.createDate) &&
                Objects.equals(vacationStart, vacation.vacationStart) &&
                Objects.equals(vacationEnd, vacation.vacationEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacationId, createUser, createDate, vacationStart, vacationEnd);
    }

    @Override
    public Integer getId() {
        return vacationId;
    }

    @Override
    public boolean isNew() {
        return (null == this.createDate);
    }
}
