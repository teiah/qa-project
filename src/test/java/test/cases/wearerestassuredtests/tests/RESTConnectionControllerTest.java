package test.cases.wearerestassuredtests.tests;

import api.models.RequestModel;
import api.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;
public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void ConnectionRequestSent_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = weAreApi.getUserRequests(receiver).length;

        RequestModel request = weAreApi.sendRequest(sender, receiver);

        RequestModel[] requestsAfter = weAreApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        weAreApi.disableUser(globalRESTAdminUser, sender.getId());
        weAreApi.disableUser(globalRESTAdminUser, receiver.getId());
    }

    @Test
    public void ConnectionRequestApproved_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = weAreApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        RequestModel sentRequest = weAreApi.sendRequest(sender, receiver);

        RequestModel[] requestsAfter = weAreApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestsCount + 1, "Request is not sent.");

        Response approveRequestResponse = weAreApi.approveRequest(receiver, sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), sender.getUsername()), "Request is not approved.");

        RequestModel[] requestsAfterApprove = weAreApi.getUserRequests(receiver);
        int requestsAfterApproveCount = requestsAfterApprove.length;

        assertEquals(requestsAfterApproveCount, previousRequestsCount, "Request is not approved.");

        weAreApi.disableUser(globalRESTAdminUser, sender.getId());
        weAreApi.disableUser(globalRESTAdminUser, receiver.getId());
    }

    @Test
    public void ConnectionCutOff_From_ConnectedUser() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = weAreApi.sendRequest(sender, receiver);

        weAreApi.approveRequest(receiver, sendRequest);

        Response disconnectRequestResponse = weAreApi.disconnectFromUser(receiver, sender);

        int statusCode = disconnectRequestResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(disconnectRequestResponse.body().asString(), String.format("%s disconnected from %s",
                receiver.getUsername(), sender.getUsername()), "Disconnection was not done");

        weAreApi.disableUser(globalRESTAdminUser, sender.getId());
        weAreApi.disableUser(globalRESTAdminUser, receiver.getId());
    }

    @Test
    public void RequestReceived_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = weAreApi.getUserRequests(receiver);
        int previousRequestCount = requests.length;

        weAreApi.sendRequest(sender, receiver);

        RequestModel[] requestsAfter = weAreApi.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestCount + 1, "Request is not approved.");
        assertTrue(requestsAfter.length > 0, "There are no requests");
        for (RequestModel request : requestsAfter) {
            assertNotNull(request, "Request is null");
        }

        weAreApi.disableUser(globalRESTAdminUser, sender.getId());
        weAreApi.disableUser(globalRESTAdminUser, receiver.getId());
    }

}
