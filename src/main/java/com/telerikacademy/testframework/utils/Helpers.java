package com.telerikacademy.testframework.utils;

import com.github.javafaker.Faker;
import com.mifmif.common.regex.Generex;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static api.utils.JSONRequests.postBody;

public class Helpers {

    Faker faker = new Faker();

    public boolean isRequiredPassword(String password) {
        String regex = "^(?=.*[\\x21-\\x60])[a-z\\x21-\\x60]{8,}$";

        Pattern pattern = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isImplementedPassword(String password) {
        return password.length() < 6;
    }

    public boolean isRequiredUsername(String username) {
        return username.length() >= 2 && username.length() <= 20;
    }

    public boolean isImplementedUsername(String username) {
        String regex = "^[a-zA-Z]*$";

        Pattern pattern = Pattern.compile(regex);

        if (username == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    public boolean isImplementedAndRequiredEmail(String email) {

        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern pattern = Pattern.compile(regex);

        if (email == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public String generateUsernameAsImplemented(String authority) {

        String regex = "[a-zA-Z]*";
        Generex generex = new Generex(regex);

        if (authority.equals(UserRoles.ROLE_ADMIN.toString())) {
            return "admin" + generex.random(5, 15);
        } else {
            return "user" + generex.random(5, 16);
        }

    }

    public String generateUsernameAsRequired(String authority) {

        if (authority.equals(UserRoles.ROLE_ADMIN.toString())) {
            String regex = "^(?=.*[\\x20-\\x7E])[\\x20-\\x7E]{1,15}$";
            return "admin" + faker.regexify(regex);
        } else {
            String regex = "^(?=.*[\\x20-\\x7E])[\\x20-\\x7E]{1,15}$";
            Generex generex = new Generex(regex);
            String result = "user" + faker.regexify(regex);
            System.out.println(result);
            return result;
        }
    }

    public String generatePasswordAsRequired() {

        String regex = "^(?=.*[\\x21-\\x60])[a-z\\x21-\\x60]{8,}$";

        return faker.regexify(regex);

    }

    public String generatePasswordAsImplemented() {
        return faker.internet().password(6, 200);
    }

    public String generateEmail() {
        return faker.internet().emailAddress();
    }

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generateCity() {
        return faker.address().city();
    }

    public String generatePersonalReview() {
        return faker.lorem().characters(0, 250);
    }

    public String generatePicture() {
        return faker.internet().image();
    }

    public String generateBirthdayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(faker.date().birthday());
    }

    public String generateSkill() {
        return faker.lorem().characters(5, 10);
    }

    public String generatePostContent() {
        return faker.lorem().characters(0, 1000);
    }

    public String generatePostBody(boolean visibility) {
        String postContent = generatePostContent();
        String postPicture = generatePicture();
        return String.format(postBody, postContent, postPicture, visibility);
    }

    public String generateCommentContent() {
        return faker.lorem().sentence(5);
    }

    public String getUserId(String text) {
        return text.replaceAll("\\D", "");
    }
}
