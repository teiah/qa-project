package api.controllers;

import api.models.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.telerikacademy.testframework.utils.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class UserController extends BaseWeAreApi {

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
        return given()
                .queryParam("principal", principal)
                .get(String.format(API + USER_BY_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(UserResponse.class);
    }

    public static PersonalProfileRequest updatePersonalProfile(
            int userId, PersonalProfileRequest personalProfileData, String cookieValue) {

        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .contentType(ContentType.JSON)
                .body(personalProfileData)
                .post(String.format(API + UPGRADE_PERSONAL_PROFILE, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(PersonalProfileRequest.class);
    }

    public static ExpertiseProfileResponse editExpertiseProfile(
            int userId, ExpertiseProfileRequest expertiseProfileData, String cookieValue) {

        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .contentType("application/json")
                .body(expertiseProfileData)
                .post(String.format(API + UPGRADE_USER_EXPERTISE_WITH_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(ExpertiseProfileResponse.class);
    }

    public static AllUsersResponse[] getUsers(AllUsersRequest request) {
        return given()
                .contentType("application/json")
                .body(request)
                .post(API + USERS)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(AllUsersResponse[].class);
    }

    public static Response authUser(String username, String password) {
        return given()
                .multiPart("username", username)
                .multiPart("password", password)
                .post(Utils.getConfigPropertyByKey("weare.auth"));
    }

}