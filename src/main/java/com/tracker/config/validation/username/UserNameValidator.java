package com.tracker.config.validation.username;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Perebeinis on 04.05.2018.
 */
public class UserNameValidator implements ConstraintValidator<UserNameConstraint, String> {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Override
    public void initialize(UserNameConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext cxt) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        return userDetails == null;
    }

}