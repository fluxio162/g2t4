package TimeManager.ui.controllers;

import TimeManager.model.User;
import TimeManager.services.UserService;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserListController implements Serializable {

    @Autowired
    private UserService userService;

    private Collection<User> filteredUsers;

    /**
     * Instantiates a new controller.
     */
    public UserListController(){
    }

    /**
     * Instantiates a new controller.
     *
     * @param userService the user service
     */
    public UserListController(UserService userService){
        this.userService = userService;
    }

    /**
     * Returns a list of all users.
     *
     * @return users
     */
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Returns a list of filtered users.
     *
     * @return the filtered users
     */
    public Collection<User> getFilteredUsers(){
        return this.filteredUsers;
    }

    /**
     * Sets filtered users.
     *
     * @param filteredUsers the filtered users
     */
    public void setFilteredUsers(Collection<User> filteredUsers){
       this.filteredUsers = filteredUsers;
    }

}
