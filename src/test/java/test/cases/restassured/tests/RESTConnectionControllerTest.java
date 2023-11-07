package test.cases.restassured.tests;

import api.models.models.RequestModel;
import api.models.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.controllers.Request;
import api.controllers.User;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {

    UserModel receiver = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        User.register(receiver, ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        User.disableUser(globalRestApiAdminUser, receiver);
    }

    @Test
    public void connectionRequestSent_By_User() {

        int initialRequestsCount = 0;
        String[] fields = Request.getUserReceivedRequests(receiver);

        if (fields != null) {
            initialRequestsCount = fields.length;
        }

        RequestModel request = Request.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = Request.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(request.getSender().getId(), globalRestApiUser.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        Request.approveRequest(receiver, request);
        Request.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionRequestApproved_By_User() {

        RequestModel[] requests = Request.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        RequestModel sentRequest = Request.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = Request.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestsCount + 1, "Request is not sent.");

        Response approveRequestResponse = Request.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), globalRestApiUser.getUsername()), "Request is not approved.");

        RequestModel[] requestsAfterApprove = Request.getUserRequests(receiver);
        int requestsAfterApproveCount = requestsAfterApprove.length;

        assertEquals(requestsAfterApproveCount, previousRequestsCount, "Request is not approved.");

        Request.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionCutOff_From_ConnectedUser() {

        Request.connect(globalRestApiUser, receiver);

        Request.disconnect(globalRestApiUser, receiver);

    }

    @Test
    public void requestReceived_By_User() {

        RequestModel[] requests = Request.getUserRequests(receiver);
        int previousRequestCount = requests.length;

        RequestModel request = Request.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = Request.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestCount + 1, "Request is not approved.");
        assertTrue(requestsAfter.length > 0, "There are no requests");

        for (RequestModel requestAfter : requestsAfter) {
            assertNotNull(requestAfter, "Request is null");
        }

        Request.approveRequest(receiver, request);
        Request.disconnect(globalRestApiUser, receiver);

    }

}
