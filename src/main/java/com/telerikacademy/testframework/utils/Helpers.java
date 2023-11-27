package com.telerikacademy.testframework.utils;

import com.github.javafaker.Faker;
import com.mifmif.common.regex.Generex;

import java.util.Random;

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

    public static Location generateCity() {
        Location[] locationNames = Location.values();
        Random random = new Random();
        int randomIndex = random.nextInt(locationNames.length);
        return locationNames[randomIndex];
    }

    public static String generatePicture() {
        return faker.internet().image();
    }

    public static String generateSkill() {
        String[] skillNames = {"Programming", "Design", "Communication", "Problem Solving", "Teamwork", "Time Management"};
        Random random = new Random();

//        Expertise[] categories = Expertise.values();
//        Expertise randomCategory = categories[random.nextInt(categories.length)];

        return skillNames[random.nextInt(skillNames.length)];
    }

//    public static Skill generateSkill() {
//        String[] skillNames = {"Programming", "Design", "Communication", "Problem Solving", "Teamwork", "Time Management"};
//        Expertise[] expertiseValues = Expertise.values();
//        Random random = new Random();
//        String randomSkillName = skillNames[random.nextInt(skillNames.length)];
//        Expertise randomCategory = expertiseValues[random.nextInt(expertiseValues.length)];
//
//        return new Skill(randomSkillName, randomCategory);
//    }

    public static Expertise generateExpertise(){
        Expertise[] expertiseNames = Expertise.values();
        Random random = new Random();
        int randomIndex = random.nextInt(expertiseNames.length);
        return expertiseNames[randomIndex];
    }

    public static String generatePostContent() {
        return faker.lorem().characters(0, 1000);
    }

    public static String generateCommentContent() {
        return faker.lorem().sentence(5);
    }

    public static boolean generatePrivacy() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static Gender generateGender() {
        Random random = new Random();
        return Gender.values()[random.nextInt(Gender.values().length)];
    }


    public static int generateAvailability() {
        Random random = new Random();
        int HOURS_MIN = 1;
        int HOURS_MAX = 40;

        return random.nextInt(HOURS_MAX - HOURS_MIN + 1) + HOURS_MIN;
    }
}
