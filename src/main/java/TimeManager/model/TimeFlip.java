package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class TimeFlip implements Persistable<Integer>, Serializable, Comparable<TimeFlip> {

    private static final long serialVersionUID = 1L;

    @Id
    private int timeFlipId;

    private String macAddress;

    @OneToOne(mappedBy = "timeFlip")
    private User user;

    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
    private Map<Integer, String> timeFlipConfiguration;


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

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    public int getTimeFlipId() {
        return timeFlipId;
    }

    public void setTimeFlipId(int timeFlipId) {
        this.timeFlipId = timeFlipId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Integer, String> getTimeFlipConfiguration() {
        if(timeFlipConfiguration == null){
            return new HashMap<Integer, String>();
        }
        return timeFlipConfiguration;
    }

    public void setTimeFlipConfiguration(Map<Integer, String> timeFlipConfiguration) {
        this.timeFlipConfiguration = timeFlipConfiguration;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.timeFlipId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TimeFlip)) {
            return false;
        }
        final TimeFlip other = (TimeFlip) obj;
        return Objects.equals(this.timeFlipId, other.timeFlipId);
    }

    @Override
    public int compareTo(TimeFlip o) {
        if(this.timeFlipId < o.getTimeFlipId()){
            return -1;
        }
        else if (this.timeFlipId > o.getTimeFlipId()){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(timeFlipId);
    }

    @Override
    public Integer getId() { return timeFlipId; }

    public void setId(int id) { setTimeFlipId(id);}

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

}
