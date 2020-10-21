package TimeManager.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Room implements Persistable<Integer>, Serializable, Comparable<Room> {

    private static final long serialVersionUID = 1L;

    @Id
    private int roomId;


    @OneToOne
    @JoinColumn(name = "Miniserver_Id", referencedColumnName = "miniserverId")
    private Miniserver miniserver;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public Miniserver getMiniserver() {
        return miniserver;
    }

    public void setMiniserver(Miniserver miniserver) {
        this.miniserver = miniserver;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.roomId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Room)) {
            return false;
        }
        final Room other = (Room) obj;
        return Objects.equals(this.roomId, other.roomId);
    }

    @Override
    public int compareTo(Room o) {
        if(this.roomId < o.getRoomId()){
            return -1;
        }
        else if (this.roomId > o.getRoomId()){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(roomId);
    }

    @Override
    public Integer getId() { return roomId; }

    public void setId(int id) { setRoomId(id);}

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}

