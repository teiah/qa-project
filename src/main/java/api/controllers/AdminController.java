package api.controllers;

import api.models.models.User;
import io.restassured.response.Response;

import static com.telerikacademy.testframework.utils.Endpoints.ADMIN_STATUS;
import static com.telerikacademy.testframework.utils.Endpoints.AUTHENTICATE;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import static org.testng.Assert.assertEquals;

public class AdminController extends BaseWeAreApi {

    public static void disableUser (User adminUser, User user){

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

    public static void enableUser (User adminUser, User userToBeEnabled){

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



}
