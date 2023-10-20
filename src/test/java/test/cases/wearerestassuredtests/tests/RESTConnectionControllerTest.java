package test.cases.wearerestassuredtests.tests;

import models.models.RequestModel;
import models.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class RESTConnectionControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void ConnectionRequestSent_By_User() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        int initialRequestsCount = receiver.getUserRequests().length;

        RequestModel request = sender.sendRequest(receiver);

        RequestModel[] requestsAfter = receiver.getUserRequests();
        int afterRequestCount = requestsAfter.length;

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        globalRESTAdminUser.disableUser(sender.getId());
        globalRESTAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void ConnectionRequestApproved_By_User() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        RequestModel[] requests = receiver.getUserRequests();
        int previousRequestsCount = requests.length;

        RequestModel sentRequest = sender.sendRequest(receiver);

        RequestModel[] requestsAfter = receiver.getUserRequests();
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestsCount + 1, "Request is not sent.");

        Response approveRequestResponse = receiver.approveRequest(sentRequest);

        assertEquals(approveRequestResponse.body().asString(), String.format("%s approved request of %s",
                receiver.getUsername(), sender.getUsername()), "Request is not approved.");

        RequestModel[] requestsAfterApprove = receiver.getUserRequests();
        int requestsAfterApproveCount = requestsAfterApprove.length;

        assertEquals(requestsAfterApproveCount, previousRequestsCount, "Request is not approved.");

        globalRESTAdminUser.disableUser(sender.getId());
        globalRESTAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void ConnectionCutOff_From_ConnectedUser() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        sender.connectTo(receiver);

        Response disconnectRequestResponse = sender.disconnectFromUser(receiver);

        int statusCode = disconnectRequestResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(disconnectRequestResponse.body().asString(), String.format("%s disconnected from %s",
                receiver.getUsername(), sender.getUsername()), "Disconnection was not done");

        globalRESTAdminUser.disableUser(sender.getId());
        globalRESTAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void RequestReceived_By_User() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        RequestModel[] requests = receiver.getUserRequests();
        int previousRequestCount = requests.length;

        sender.sendRequest(receiver);

        RequestModel[] requestsAfter = receiver.getUserRequests();
        int afterRequestCount = requestsAfter.length;

        assertEquals(afterRequestCount, previousRequestCount + 1, "Request is not approved.");
        assertTrue(requestsAfter.length > 0, "There are no requests");
        for (RequestModel request : requestsAfter) {
            assertNotNull(request, "Request is null");
        }

        globalRESTAdminUser.disableUser(sender.getId());
        globalRESTAdminUser.disableUser(receiver.getId());
    }

}
