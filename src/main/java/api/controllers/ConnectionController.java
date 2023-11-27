package api.controllers;

import com.google.gson.Gson;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;
import api.models.Request;
import api.models.UserRequest;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class ConnectionController extends BaseWeAreApi {

    public static final String sendRequestBody = "{\n" +
            "  \"id\": %s,\n" +
            "  \"username\": \"%s\"\n" +
            "}";

    public static Request sendRequest(UserRequest sender, UserRequest receiver) {

        Response response = given()
                .auth()
                .form(sender.getUsername(), sender.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, receiver.getId(), receiver.getUsername()))
                .post(API + REQUEST);

        LOGGER.info(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.body().asString(), String.format("%s send friend request to %s",
                sender.getUsername(), receiver.getUsername()), "Connection request was not send");

        Request request = new Request();
        request.setSender(sender);
        request.setReceiver(receiver);
        String[] fields = getUserReceivedRequests(receiver);
        request.setId(Integer.parseInt(fields[0]));
        request.setTimeStamp(fields[1]);

        return request;

    }

    public static Request[] getUserRequests(UserRequest user) {
        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .get(String.format(API + USER_REQUEST_WITH_ID, user.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        Request[] requests = new Gson().fromJson(response.getBody().asString(), Request[].class);

        return requests;
    }

    public static String[] getUserReceivedRequests(UserRequest receiver) {
        Response response = given()
                .auth()
                .form(receiver.getUsername(), receiver.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .get(String.format(API + USER_REQUEST_WITH_ID, receiver.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        Request[] requests = new Gson().fromJson(response.getBody().asString(), Request[].class);

        if (requests.length > 0) {
            String[] fields = new String[2];

            fields[0] = String.valueOf(requests[0].getId());
            fields[1] = String.valueOf(requests[0].getTimeStamp());

            return fields;
        }

        return null;
    }

    public static Response approveRequest(UserRequest receiver, Request request) {

        Response response = given()
                .auth()
                .form(receiver.getUsername(), receiver.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("requestId", request.getId())
                .post(String.format(API + APPROVE_REQUEST_WITH_ID, receiver.getId()));

        LOGGER.info(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response;
    }

    public static void disconnect(UserRequest sender, UserRequest receiver) {

        Response response = given()
                .auth()
                .form(sender.getUsername(), sender.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, receiver.getId(), receiver.getUsername()))
                .post(API + REQUEST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        assertEquals(response.body().asString(), String.format("%s disconnected from %s",
                sender.getUsername(), receiver.getUsername()), "Disconnection was not done");

        LOGGER.info(response.getBody().asPrettyString());

    }

    public static void connect(UserRequest sender, UserRequest receiver) {

        Request requestModel = sendRequest(sender, receiver);
        approveRequest(receiver, requestModel);

    }

}
