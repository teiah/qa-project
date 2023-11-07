package api.controllers;

import com.google.gson.Gson;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;
import api.models.models.*;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UserController extends BaseWeAreApi {
    private static final String userBody = "{\n" +
            "  \"authorities\": [\n" +
            "    %s\n" +
            "  ],\n" +
            "  \"category\": {\n" +
            "    \"id\": %s,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"confirmPassword\": \"" + "%s" + "\",\n" +
            "  \"email\": \"" + "%s" + "\",\n" +
            "  \"password\": \"" + "%s" + "\",\n" +
            "  \"username\": \"" + "%s" + "\"\n" +
            "}";
    static final String searchUsersBody = "{\n" +
            "  \"index\": %s,\n" +
            "  \"next\": %s,\n" +
            "  \"searchParam1\": \"%s\",\n" +
            "  \"searchParam2\": \"%s\",\n" +
            "  \"size\": %s\n" +
            "}";
    private static final String personalProfileBodyFirstName = "{\n" +
            "\"firstName\": \"%s\",\n" +
            "\"location\": {\n" +
            "}\n" +
            "}";
    private static final String personalProfileBody = "{\n" +
            "\"birthYear\": \"%s\",\n" +
            "\"firstName\": \"%s\",\n" +
            "\"id\": %d,\n" +
            "\"lastName\": \"%s\",\n" +
            "\"location\": {\n" +
            "\"city\": {\n" +
            "\"city\": \"%s\",\n" +
            "\"country\": {},\n" +
            "\"id\": 1\n" +
            "},\n" +
            "\"id\": 0\n" +
            "},\n" +
            "\"memberSince\": \"\",\n" +
            "\"personalReview\": \"%s\",\n" +
            "\"picture\": \"%s\",\n" +
            "\"picturePrivacy\": %s,\n" +
            "\"sex\": \"%s\"\n" +
            "}";
    private static final String expertiseProfileBody = "{\n" +
            "  \"availability\": %.1f,\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"id\": 0,\n" +
            "  \"skill1\": \"%s\",\n" +
            "  \"skill2\": \"%s\",\n" +
            "  \"skill3\": \"%s\",\n" +
            "  \"skill4\": \"%s\",\n" +
            "  \"skill5\": \"%s\"\n" +
            "}";

    public static void register(UserModel user, String authority) {

        user.setEmail(Helpers.generateEmail());
        user.setPassword(Helpers.generatePassword());
        user.setUsername(Helpers.generateUsernameAsImplemented(authority));

        registerAndExtractUser(user, authority);

    }

    public static void register(UserModel user, String username, String password, String email, String authority) {

        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        registerAndExtractUser(user, authority);

    }

    private static void registerAndExtractUser(UserModel user, String authority) {
        int categoryId = 100;
        String categoryName = "All";
        CategoryModel category = new CategoryModel();
        category.setName(categoryName);
        category.setId(categoryId);
        String bodyAuthority = "";

        if (authority.equals(ROLE_ADMIN.toString())) {
            bodyAuthority = String.format("\"%s\", \"%s\"", ROLE_USER, ROLE_ADMIN);
        } else if (authority.equals(ROLE_USER.toString())) {
            bodyAuthority = String.format("\"%s\"", ROLE_USER);
        }

        Response response = given()
                .contentType("application/json")
                .body(String.format(userBody, bodyAuthority, category.getId(), category.getName(), user.getPassword(),
                        user.getEmail(), user.getPassword(), user.getUsername()))
                .post(API + REGISTER_USER)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(response.getBody().asPrettyString());

        user.setUserId(Integer.parseInt(getUserId(response)));

        assertEquals(response.getBody().asString(), String.format("User with name %s and id %d was created",
                user.getUsername(), user.getId()), "User was not registered.");

        extractUser(user);
    }

    private static void extractUser(UserModel user) {

        UserByIdModel userByIdModel = getUserById(user.getUsername(), user.getId()).as(UserByIdModel.class);
        user.setEmail(userByIdModel.getEmail());
        String firstName = Helpers.generateFirstName();
        UserController.setPersonalProfileFirstName(user, firstName);
        UserBySearchModel userBySearchModel = searchUser(user.getId(), user.getPersonalProfile().getFirstName());
        assert userBySearchModel != null;
        user.setExpertiseProfile(userBySearchModel.getExpertiseProfile());
        user.setAccountNonExpired(userBySearchModel.isAccountNonExpired());
        user.setAccountNonLocked(userBySearchModel.isAccountNonLocked());
        user.setCredentialsNonExpired(userBySearchModel.isCredentialsNonExpired());
        user.setEnabled(userBySearchModel.isEnabled());
        List<GrantedAuthorityModel> authorities = new ArrayList<>();
        RoleModel roleModel = new RoleModel();
        roleModel.setAuthority(ROLE_USER.toString());
        authorities.add(roleModel);
        if (userByIdModel.getAuthorities().length == 2) {
            roleModel.setAuthority(ROLE_ADMIN.toString());
            authorities.add(roleModel);
        }
        user.setAuthorities(authorities);

        assertNotNull(user.getExpertiseProfile().getCategory(), "User has no professional category");

    }

    public static void setPersonalProfileFirstName(UserModel user, String firstName) {

        String body = String.format(personalProfileBodyFirstName, firstName);

        Response editProfileResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, user.getId()));

        int statusCode = editProfileResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        user.setPersonalProfile(editProfileResponse.as(PersonalProfileModel.class));
        assertEquals(user.getPersonalProfile().getFirstName(), firstName);
        LOGGER.info(String.format("First name of user %s with id %d was set to %s.", user.getUsername(),
                user.getId(), user.getPersonalProfile().getFirstName()));
    }

    public static void updatePersonalProfile(UserModel user, PersonalProfileModel personalProfileEditData) {

        String birthYear = personalProfileEditData.getBirthYear();
        String firstName = personalProfileEditData.getFirstName();
        String lastName = personalProfileEditData.getLastName();
        String city = personalProfileEditData.getLocation().getCity().getCity();
        String personalReview = personalProfileEditData.getPersonalReview();
        String picture = personalProfileEditData.getPicture();
        boolean picturePrivacy = personalProfileEditData.getPicturePrivacy();
        String sex = personalProfileEditData.getSex();

        String body = String.format(personalProfileBody, birthYear, firstName, user.getId(), lastName, city, personalReview,
                picture, picturePrivacy, sex);

        Response editProfileResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("name", user.getUsername())
                .body(body)
                .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, user.getId()));

        int statusCode = editProfileResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        user.setPersonalProfile(editProfileResponse.as(PersonalProfileModel.class));
        user.getPersonalProfile().setPicture(picture);

        assertUpdatePersonalProfile(user.getPersonalProfile(), personalProfileEditData);

        LOGGER.info(String.format("Personal profile of user %s with id %d was updated", user.getUsername(), user.getId()));
    }

    private static void assertUpdatePersonalProfile(PersonalProfileModel personalProfileAfterEdit, PersonalProfileModel personalProfileEditData) {

        assertEquals(personalProfileAfterEdit.getFirstName(), personalProfileEditData.getFirstName(), "First names do not match.");
        assertEquals(personalProfileAfterEdit.getLastName(), personalProfileEditData.getLastName(), "Last names do not match.");
        assertEquals(personalProfileAfterEdit.getBirthYear(), personalProfileEditData.getBirthYear(),
                "Birth years do not match.");
        assertEquals(personalProfileAfterEdit.getLocation().getCity().getCity(), personalProfileEditData.getLocation().getCity().getCity(), "Cities do not match.");
        assertEquals(personalProfileAfterEdit.getPersonalReview(), personalProfileEditData.getPersonalReview(), "Personal reviews do not match.");
        assertEquals(personalProfileAfterEdit.getPicturePrivacy(), personalProfileEditData.getPicturePrivacy(), "Picture privacies do not match.");
    }

    public static void editExpertiseProfile(UserModel user, ExpertiseProfileModel expertiseProfileEditData) {

        double availability = expertiseProfileEditData.getAvailability();
        int categoryId = expertiseProfileEditData.getCategory().getId();
        String categoryName = expertiseProfileEditData.getCategory().getName();
        String skill1 = expertiseProfileEditData.getSkills().get(0).getSkill();
        String skill2 = expertiseProfileEditData.getSkills().get(1).getSkill();
        String skill3 = expertiseProfileEditData.getSkills().get(2).getSkill();
        String skill4 = expertiseProfileEditData.getSkills().get(3).getSkill();
        String skill5 = expertiseProfileEditData.getSkills().get(4).getSkill();

        String body = String.format(expertiseProfileBody, availability, categoryId, categoryName, skill1, skill2, skill3,
                skill4, skill5);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(String.format(API + UPGRADE_USER_EXPERTISE_WITH_ID, user.getId()))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        user.setExpertiseProfile(response.as(ExpertiseProfileModel.class));
        assertEditExpertiseProfile(user.getExpertiseProfile(), expertiseProfileEditData);

        LOGGER.info(String.format("Expertise profile of user %s with id %d was updated", user.getUsername(), user.getId()));

    }

    private static void assertEditExpertiseProfile(ExpertiseProfileModel userExpertiseProfile, ExpertiseProfileModel expertiseProfile) {

        assertEquals(userExpertiseProfile.getCategory().getId(), expertiseProfile.getCategory().getId(),
                "Category ids do not match.");
        assertEquals(userExpertiseProfile.getCategory().getName(), expertiseProfile.getCategory().getName(),
                "Category names do not match.");
        assertEquals(userExpertiseProfile.getAvailability(), expertiseProfile.getAvailability(),
                "Availabilities do not match.");
        assertEquals(userExpertiseProfile.getSkills().size(), expertiseProfile.getSkills().size(),
                "Availabilities do not match.");

    }

    public static void disableUser(UserModel adminUser, UserModel user) {

        Response response = given()
                .auth()
                .form(adminUser.getUsername(), adminUser.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", false)
                .queryParam("userId", user.getId())
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("User with id %d disabled.", user.getId()));

    }

    public static void enableUser(UserModel adminUser, UserModel userToBeEnabled) {

        Response response = given()
                .auth()
                .form(adminUser.getUsername(), adminUser.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", true)
                .queryParam("userId", userToBeEnabled.getId())
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("User %s with id %d enabled.", userToBeEnabled.getUsername(), userToBeEnabled.getId()));

    }

    private static String getUserId(Response response) {
        return response.body().asString().replaceAll("\\D", "");
    }


    public static Response getUserById(String username, int userId) {

        Response response = given()
                .queryParam("principal", username)
                .get(String.format(API + USER_BY_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(String.format("User with id %d found", userId));

        return response;
    }

    public static UserBySearchModel searchUser(int userId, String firstname) {

        int index = 0;
        boolean next = true;
        String searchParam1 = "";
        String searchParam2 = firstname;
        int size = 1000000;

        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);

        Response response = given()
                .contentType("application/json")
                .body(body)
                .post(API + USERS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        UserBySearchModel[] foundUsers = new Gson().fromJson(response.getBody().asString(), UserBySearchModel[].class);
        for (UserBySearchModel userBySearchModel : foundUsers) {
            if (userBySearchModel.getUserId() == userId) {
                return userBySearchModel;
            }
        }
        return null;
    }

}
