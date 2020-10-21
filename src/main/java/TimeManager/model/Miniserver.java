package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Miniserver implements Persistable<Integer>, Serializable, Comparable<Miniserver> {

    private static final long serialVersionUID = 1L;

    @Id
    private int miniserverId;

    private String IPAddress;

    @ManyToMany(fetch= FetchType.EAGER)
    private Set<TimeFlip> timeFlip;

    @OneToOne(mappedBy = "miniserver")
    private Room room;

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

    public int getMiniserverId() {
        return miniserverId;
    }

    public void setMiniserverId(int miniserverId) {
        this.miniserverId = miniserverId;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public Set<TimeFlip> getTimeFlip() {
        return timeFlip;
    }

    public void setTimeFlip(Set<TimeFlip> timeFlip) {
        this.timeFlip = timeFlip;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.miniserverId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Miniserver)) {
            return false;
        }
        final Miniserver other = (Miniserver) obj;
        return Objects.equals(this.miniserverId, other.miniserverId);
    }

    @Override
    public int compareTo(Miniserver o) {
        if(this.miniserverId < o.getMiniserverId()){
            return -1;
        }
        else if (this.miniserverId > o.getMiniserverId()){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(miniserverId);
    }

    @Override
    public Integer getId() { return miniserverId; }

    public void setId(int id) { setMiniserverId(id);}

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

}
