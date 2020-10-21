package TimeManager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
@Table(name = "user")
public class User implements Persistable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


    @ManyToOne(optional = true)
    private Team team;

    @ManyToOne(optional = true)
    private Department department;

    @OneToOne
    @JoinColumn(name = "TimeFlip_Id", referencedColumnName = "timeFlipId")
    private TimeFlip timeFlip;

    @ManyToOne(optional = true)
    private Room room;

    @ManyToOne(optional = true)
    private User updateUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    private Date birthDay;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private boolean notification;
    private String notificationCategory;

    boolean enabled;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "User_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;


    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] profilePicture;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
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

    public Date getDeleteDate() {return deleteDate;}

    public void setDeleteDate(Date deleteDate) {this.deleteDate = deleteDate;}

    public User getDeleteUser() {return deleteUser;}

    public void setDeleteUser(User deleteUser) {this.deleteUser = deleteUser;}

    public Team getTeam() {return team;}

    public void setTeam(Team team) {this.team = team;}

    public Department getDepartment() {return department;}

    public void setDepartment(Department department) {this.department = department;}

    public TimeFlip getTimeFlip() {return timeFlip;}

    public void setTimeFlip(TimeFlip timeFlip) {this.timeFlip = timeFlip;}

    public Date getBirthDay() {return birthDay;}

    public void setBirthDay(Date birthDay) {this.birthDay = birthDay;}

    public boolean isNotification() {return notification;}

    public void setNotification(boolean notification) {this.notification = notification;}

    public String getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(String notificationCategory) {
        this.notificationCategory = notificationCategory;
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
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }



}
