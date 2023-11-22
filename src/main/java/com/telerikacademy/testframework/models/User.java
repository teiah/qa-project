package com.telerikacademy.testframework.models;

import com.telerikacademy.testframework.utils.Expertise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private PersonalProfile profile;
    private String username;
    private String email;
    private String password;
    private Expertise category;


    public User(String username, String email, String password, Expertise category) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setCategory(category);
        setPersonalProfile(new PersonalProfile());
    }

    private void setPersonalProfile(PersonalProfile personalProfile) {
    }
}
