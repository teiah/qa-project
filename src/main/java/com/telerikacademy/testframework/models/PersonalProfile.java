package com.telerikacademy.testframework.models;

import api.models.models.ApiLocation;
import api.models.models.Post;
import com.telerikacademy.testframework.utils.Gender;
import com.telerikacademy.testframework.utils.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PersonalProfile {

    private String firstName;
    private String lastName;
    private String birthday;
    private Gender gender;
    private Location location;
    private String personalReview;
    private String picture;
    private boolean picturePrivacy;
    private ArrayList<User> friendList;
    private ArrayList<Post> posts;
    private SetOfSkills skills;


    public PersonalProfile(String firstName, String lastName, String birthday, Location location) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthday(birthday);
        setLocation(location);
    }

    public PersonalProfile() {
        friendList = new ArrayList<>();
        posts = new ArrayList<>();
        skills = new SetOfSkills();

    }

    public boolean getPicturePrivacy() {
        return picturePrivacy;
    }


//    public PersonalProfile(String firstName, String lastName, String birthday, Location location) {
//        setFirstName(firstName);
//        setLastName(lastName);
//        setBirthday(birthday);
//        setLocation(location);
//    }

}