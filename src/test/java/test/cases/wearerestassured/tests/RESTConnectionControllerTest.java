package test.cases.wearerestassured.tests;

import com.telerikacademy.testframework.models.RequestModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;
public class RESTConnectionControllerTest extends BaseTestSetup {


    @Test
    public void userCanSendRequestToAnotherUser() {

        UserModel sender = weareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weareApi.registerUser(ROLE_USER.toString());

        RequestModel request = weareApi.sendRequest(sender, receiver);

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");

        weareApi.disableUser(globalAdminUser, sender.getId());
        weareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanApproveRequestFromAnotherUser() {

        UserModel sender = weareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weareApi.registerUser(ROLE_USER.toString());

        RequestModel sentRequest = weareApi.sendRequest(sender, receiver);

        Response approveRequestResponse = weareApi.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), sender.getUsername()), "Request is not approved.");

        weareApi.disableUser(globalAdminUser, sender.getId());
        weareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanDisconnectFromAnotherUser() {

        UserModel sender = weareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weareApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = weareApi.sendRequest(sender, receiver);

        weareApi.approveRequest(receiver, sendRequest);

        Response disconnectRequestResponse = weareApi.disconnectFromUser(receiver, sender);

        int statusCode = disconnectRequestResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(disconnectRequestResponse.body().asString(), String.format("%s disconnected from %s",
                receiver.getUsername(), sender.getUsername()), "Disconnection was not done");

        weareApi.disableUser(globalAdminUser, sender.getId());
        weareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanGetUserRequests() {

        UserModel sender = weareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weareApi.registerUser(ROLE_USER.toString());

        weareApi.sendRequest(sender, receiver);

        RequestModel[] requests = weareApi.getUserRequests(receiver);

        assertTrue(requests.length > 0, "There are no requests");
        for (RequestModel request : requests) {
            assertEquals(request.getClass(), RequestModel.class, "Wrong type of request");
            assertNotNull(request, "Request is null");
        }

        weareApi.disableUser(globalAdminUser, sender.getId());
        weareApi.disableUser(globalAdminUser, receiver.getId());
    }
}
