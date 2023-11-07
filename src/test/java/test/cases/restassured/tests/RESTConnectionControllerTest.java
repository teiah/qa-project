package test.cases.restassured.tests;

import restassuredapi.models.models.RequestModel;
import restassuredapi.models.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import restassuredapi.RequestApi;
import restassuredapi.UserApi;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {

    UserModel receiver = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        UserApi.register(receiver, ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        UserApi.disableUser(globalRestApiAdminUser, receiver);
    }

    @Test
    public void connectionRequestSent_By_User() {

        int initialRequestsCount = 0;
        String[] fields = RequestApi.getUserReceivedRequests(receiver);

        if (fields != null) {
            initialRequestsCount = fields.length;
        }

        RequestModel request = RequestApi.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = RequestApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(request.getSender().getId(), globalRestApiUser.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        RequestApi.approveRequest(receiver, request);
        RequestApi.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionRequestApproved_By_User() {

        RequestModel[] requests = RequestApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        RequestModel sentRequest = RequestApi.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = RequestApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestsCount + 1, "Request is not sent.");

        Response approveRequestResponse = RequestApi.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), globalRestApiUser.getUsername()), "Request is not approved.");

        RequestModel[] requestsAfterApprove = RequestApi.getUserRequests(receiver);
        int requestsAfterApproveCount = requestsAfterApprove.length;

        assertEquals(requestsAfterApproveCount, previousRequestsCount, "Request is not approved.");

        RequestApi.disconnect(globalRestApiUser, receiver);
    }

    @Test
    public void connectionCutOff_From_ConnectedUser() {

        RequestApi.connect(globalRestApiUser, receiver);

        RequestApi.disconnect(globalRestApiUser, receiver);

    }

    @Test
    public void requestReceived_By_User() {

        RequestModel[] requests = RequestApi.getUserRequests(receiver);
        int previousRequestCount = requests.length;

        RequestModel request = RequestApi.sendRequest(globalRestApiUser, receiver);

        RequestModel[] requestsAfter = RequestApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestCount + 1, "Request is not approved.");
        assertTrue(requestsAfter.length > 0, "There are no requests");

        for (RequestModel requestAfter : requestsAfter) {
            assertNotNull(requestAfter, "Request is null");
        }

        RequestApi.approveRequest(receiver, request);
        RequestApi.disconnect(globalRestApiUser, receiver);

    }

}
