package test.cases.wearerestassured.tests.tests;

import com.telerikacademy.testframework.models.RequestModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test.cases.BaseTestSetup;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;
public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {


    @Test
    public void userCanSendRequestToAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel request = WEareApi.sendRequest(sender, receiver);

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanApproveRequestFromAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel sentRequest = WEareApi.sendRequest(sender, receiver);

        Response approveRequestResponse = WEareApi.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), sender.getUsername()), "Request is not approved.");

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanDisconnectFromAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = WEareApi.sendRequest(sender, receiver);

        WEareApi.approveRequest(receiver, sendRequest);

        Response disconnectRequestResponse = WEareApi.disconnectFromUser(receiver, sender);

        int statusCode = disconnectRequestResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(disconnectRequestResponse.body().asString(), String.format("%s disconnected from %s",
                receiver.getUsername(), sender.getUsername()), "Disconnection was not done");

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanGetUserRequests() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        WEareApi.sendRequest(sender, receiver);

        RequestModel[] requests = WEareApi.getUserRequests(receiver);

        assertTrue(requests.length > 0, "There are no requests");
        for (RequestModel request : requests) {
            assertEquals(request.getClass(), RequestModel.class, "Wrong type of request");
            assertNotNull(request, "Request is null");
        }

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
    }
}
