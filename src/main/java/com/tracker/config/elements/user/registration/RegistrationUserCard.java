package com.tracker.config.elements.user.registration;

import com.tracker.config.validation.username.UserNameConstraint;

/**
 * Created by Perebeinis on 04.05.2018.
 */
public class RegistrationUserCard {

    private String firstName;
    private String lastName;

    @UserNameConstraint
    private String username;

    private String user_email;
    private String user_group;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }
}
