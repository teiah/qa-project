package com.telerikacademy.testframework.models;

import api.models.models.Post;
import com.telerikacademy.testframework.utils.Gender;
import com.telerikacademy.testframework.utils.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public class PersonalProfile {

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Gender gender;
    private Location location;
    private String bio;
    private ArrayList<User> friendList;
    private ArrayList<Post> posts;


    public PersonalProfile(String firstName, String lastName, LocalDate birthday, Location location) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthday(birthday);
        setLocation(location);
    }

    public PersonalProfile() {
        friendList = new ArrayList<>();
        posts = new ArrayList<>();

    }
}