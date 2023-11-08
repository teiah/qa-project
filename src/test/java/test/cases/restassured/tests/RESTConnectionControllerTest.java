package test.cases.restassured.tests;

import api.models.models.RequestModel;
import api.models.models.UserModel;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.controllers.ConnectionController;
import api.controllers.UserController;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {

    UserModel receiver = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_USER.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserController.register(receiver, username, password, email, authority);
    }

    @AfterClass
    public void cleanUpCommentTest() {
        UserController.disableUser(globalRestApiAdminUser, receiver);
    }

    @Test
    public void connectionRequestSent_By_User() {

        int initialRequestsCount = 0;
        String[] fields = ConnectionController.getUserReceivedRequests(receiver);

        if (fields != null) {
            initialRequestsCount = fields.length;
        }

        RequestModel request = ConnectionController.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = ConnectionController.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(request.getSender().getId(), globalRestApiUser.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        ConnectionController.approveRequest(receiver, request);
        ConnectionController.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionRequestApproved_By_User() {

        RequestModel[] requests = ConnectionController.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        RequestModel sentRequest = ConnectionController.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = ConnectionController.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestsCount + 1, "Request is not sent.");

        Response approveRequestResponse = ConnectionController.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), globalRestApiUser.getUsername()), "Request is not approved.");

        RequestModel[] requestsAfterApprove = ConnectionController.getUserRequests(receiver);
        int requestsAfterApproveCount = requestsAfterApprove.length;

        assertEquals(requestsAfterApproveCount, previousRequestsCount, "Request is not approved.");

        ConnectionController.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionCutOff_From_ConnectedUser() {

        ConnectionController.connect(globalRestApiUser, receiver);

        ConnectionController.disconnect(globalRestApiUser, receiver);

    }

    @Test
    public void requestReceived_By_User() {

        RequestModel[] requests = ConnectionController.getUserRequests(receiver);
        int previousRequestCount = requests.length;

        RequestModel request = ConnectionController.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = ConnectionController.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestCount + 1, "Request is not approved.");
        assertTrue(requestsAfter.length > 0, "There are no requests");

        for (RequestModel requestAfter : requestsAfter) {
            assertNotNull(requestAfter, "Request is null");
        }

        ConnectionController.approveRequest(receiver, request);
        ConnectionController.disconnect(globalRestApiUser, receiver);

    }

}
