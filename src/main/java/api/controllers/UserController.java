package api.controllers;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.models.models.*;
import com.telerikacademy.testframework.utils.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class UserController extends BaseWeAreApi {
    static final String searchUsersBody = "{\n" +
            "  \"index\": %s,\n" +
            "  \"next\": %s,\n" +
            "  \"searchParam1\": \"%s\",\n" +
            "  \"searchParam2\": \"%s\",\n" +
            "  \"size\": %s\n" +
            "}";


    public static UserResponse registerUser(UserRequest user) {
        Response responseBody = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(API + REGISTER_USER)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(responseBody.getBody().asPrettyString());

        Pattern pattern = Pattern.compile("User with name (\\w+) and id (\\d+) was created");
        Matcher matcher = pattern.matcher(responseBody.getBody().asPrettyString());

        UserResponse userResponse = new UserResponse();
        if (matcher.find()) {
            String name = matcher.group(1);
            int userId = Integer.parseInt(matcher.group(2));
            userResponse.setId(userId);
            userResponse.setUsername(name);
        } else {
            System.out.println("No name and ID found in the log message.");
        }
        return userResponse;
    }


    public static UserResponse getUserById(String principal, int userId) {
        Response response = given()
                .queryParam("principal", principal)
                .get(String.format(API + USER_BY_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(String.format("User with id %d found", userId));

        return new Gson().fromJson(response.getBody().asString(), UserResponse.class);

    }


//        public static void setPersonalProfileFirstName (User user, String firstName){
//
//            String body = String.format(personalProfileBodyFirstName, firstName);
//
//            Response editProfileResponse = given()
//                    .auth()
//                    .form(user.getUsername(), user.getPassword(),
//                            new FormAuthConfig(AUTHENTICATE, "username", "password"))
//                    .contentType("application/json")
//                    .body(body)
//                    .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, user.getId()));
//
//            int statusCode = editProfileResponse.getStatusCode();
//            assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
//            user.setPersonalProfile(editProfileResponse.as(PersonalProfile.class));
//            assertEquals(user.getPersonalProfile().getFirstName(), firstName);
//            LOGGER.info(String.format("First name of user %s with id %d was set to %s.", user.getUsername(),
//                    user.getId(), user.getPersonalProfile().getFirstName()));
//        }

    public static PersonalProfileRequest updatePersonalProfile(
            int userId, PersonalProfileRequest personalProfileData, String cookieValue) {

        Response response = given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .contentType(ContentType.JSON)
                .body(personalProfileData)
                .post(String.format(API + UPGRADE_PERSONAL_PROFILE, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(String.format("Personal profile of user with id %d was updated",
                userId));
        return new Gson().fromJson(response.getBody().asString(), PersonalProfile.class);
    }
//
//        public static void editExpertiseProfile (User user, ExpertiseProfile expertiseProfileEditData){
//
//            double availability = expertiseProfileEditData.getAvailability();
//            int categoryId = expertiseProfileEditData.getCategory().getId();
//            String categoryName = expertiseProfileEditData.getCategory().getName();
//            String skill1 = expertiseProfileEditData.getSkills().get(0).getSkill();
//            String skill2 = expertiseProfileEditData.getSkills().get(1).getSkill();
//            String skill3 = expertiseProfileEditData.getSkills().get(2).getSkill();
//            String skill4 = expertiseProfileEditData.getSkills().get(3).getSkill();
//            String skill5 = expertiseProfileEditData.getSkills().get(4).getSkill();
//
//            String body = String.format(expertiseProfileBody, availability, categoryId, categoryName, skill1, skill2, skill3,
//                    skill4, skill5);
//
//            Response response = given()
//                    .auth()
//                    .form(user.getUsername(), user.getPassword(),
//                            new FormAuthConfig(AUTHENTICATE, "username", "password"))
//                    .contentType("application/json")
//                    .body(body)
//                    .post(String.format(API + UPGRADE_USER_EXPERTISE_WITH_ID, user.getId()))
//                    .then()
//                    .assertThat()
//                    .statusCode(SC_OK)
//                    .extract().response();
//
//            user.setExpertiseProfile(response.as(ExpertiseProfile.class));
//            assertEditExpertiseProfile(user.getExpertiseProfile(), expertiseProfileEditData);
//
//            LOGGER.info(String.format("Expertise profile of user %s with id %d was updated", user.getUsername(), user.getId()));
//
//        }
//
//        private static void assertEditExpertiseProfile (ExpertiseProfile userExpertiseProfile, ExpertiseProfile
//        expertiseProfile){
//
//            assertEquals(userExpertiseProfile.getCategory().getId(), expertiseProfile.getCategory().getId(),
//                    "Category ids do not match.");
//            assertEquals(userExpertiseProfile.getCategory().getName(), expertiseProfile.getCategory().getName(),
//                    "Category names do not match.");
//            assertEquals(userExpertiseProfile.getAvailability(), expertiseProfile.getAvailability(),
//                    "Availabilities do not match.");
//            assertEquals(userExpertiseProfile.getSkills().size(), expertiseProfile.getSkills().size(),
//                    "Availabilities do not match.");
//
//        }
//

//        public static UserBySearch searchUser ( int userId, String firstname){
//
//            int index = 0;
//            boolean next = true;
//            String searchParam1 = "";
//            String searchParam2 = firstname;
//            int size = 1000000;
//
//            String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);
//
//            Response response = given()
//                    .contentType("application/json")
//                    .body(body)
//                    .post(API + USERS);
//
//            int statusCode = response.getStatusCode();
//            assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
//
//            UserBySearch[] foundUsers = new Gson().fromJson(response.getBody().asString(), UserBySearch[].class);
//            for (UserBySearch userBySearchModel : foundUsers) {
//                if (userBySearchModel.getUserId() == userId) {
//                    return userBySearchModel;
//                }
//            }
//            return null;
//        }
    public static Response authUser(String username, String password) {
        return given()
                .multiPart("username", username)
                .multiPart("password", password)
                .post(Utils.getConfigPropertyByKey("weare.auth"));

    }

}

