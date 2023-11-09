package com.telerikacademy.testframework.utils;

import com.github.javafaker.Faker;
import com.mifmif.common.regex.Generex;

import java.text.SimpleDateFormat;

import static com.telerikacademy.testframework.utils.Authority.ROLE_ADMIN;

public class Helpers {

    private static Faker faker = new Faker();

    public static int generateCategoryId() {
        Faker faker = new Faker();
        return faker.number().numberBetween(100, 157);
    }

    public static String generateUsername(String authority) {

        String regex = "[a-zA-Z]*";
        Generex generex = new Generex(regex);

        if (authority.equals(ROLE_ADMIN.toString())) {
            return "admin" + generex.random(5, 15);
        } else {
            return "user" + generex.random(5, 16);
        }
    }

    public static String generatePassword() {
        return faker.internet().password(6, 200);
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generatePersonalReview() {
        return faker.lorem().characters(0, 250);
    }

    public static String generateBirthdayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(faker.date().birthday());
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generatePicture() {
        return faker.internet().image();
    }

    public static String generateSkill() {
        return faker.lorem().characters(5, 10);
    }

    public static String generatePostContent() {
        return faker.lorem().characters(0, 1000);
    }

    public static String generateCommentContent() {
        return faker.lorem().sentence(5);
    }

}
