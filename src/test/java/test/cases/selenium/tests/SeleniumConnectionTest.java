package test.cases.selenium.tests;

import org.testng.annotations.*;
import pages.weare.LoginPage;
import pages.weare.ProfilePage;
import pages.weare.RequestsListPage;
import com.telerikacademy.testframework.utils.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;
import api.Request;
import api.User;
import api.models.models.*;
import test.cases.selenium.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SeleniumConnectionTest extends BaseWeareSeleniumTest {

    UserModel receiver = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        User.register(receiver, ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        User.disableUser(globalSeleniumAdminUser, receiver);
    }

    @Test
    public void connectionRequestSent_By_User() {

        int initialRequestsCount = 0;
        String[] fields = Request.getUserReceivedRequests(receiver);

        if (fields != null) {
            initialRequestsCount = fields.length;
        }

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalSeleniumUser.getUsername(), globalSeleniumUser.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        RequestModel[] requestsAfter = Request.getUserRequests(receiver);
        int afterRequestCount = requestsAfter.length;

        Assert.assertEquals(afterRequestCount, initialRequestsCount + 1, "Request not received");

        RequestModel[] receiverRequests = Request.getUserRequests(receiver);
        Request.approveRequest(receiver, receiverRequests[receiverRequests.length - 1]);
        Request.disconnect(globalSeleniumUser, receiver);
    }

    @Test
    public void connectionRequestApproved_By_User() {

        int initialRequestsCount = Request.getUserRequests(receiver).length;

        RequestModel request = Request.sendRequest(globalSeleniumUser, receiver);

        int afterRequestCount = Request.getUserRequests(receiver).length;

        assertEquals(request.getSender().getId(), globalSeleniumUser.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());

        receiverProfilePage.navigateToPage();

        receiverProfilePage.seeRequests();

        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
                "weare.requestsListPagePage", receiver.getId());

        requestsListPage.approveRequest(globalSeleniumUser.getPersonalProfile().getFirstName());

        int currentRequestsCount = Request.getUserRequests(receiver).length;

        Assert.assertEquals(currentRequestsCount, afterRequestCount - 1, "Request not approved");

        requestsListPage.logout();

        Request.disconnect(globalSeleniumUser, receiver);

    }

    @Test
    public void connectionCutOff_From_ConnectedUser() {

        RequestModel sendRequest = Request.sendRequest(globalSeleniumUser, receiver);

        Request.approveRequest(receiver, sendRequest);

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalSeleniumUser.getUsername(), globalSeleniumUser.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.disconnect();

    }

    @Test
    public void requestReceived_By_User() {

        int initialRequestsCount = Request.getUserRequests(receiver).length;

        RequestModel request = Request.sendRequest(globalSeleniumUser, receiver);

        int afterRequestCount = Request.getUserRequests(receiver).length;

        assertEquals(request.getSender().getId(), globalSeleniumUser.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());

        receiverProfilePage.navigateToPage();

        receiverProfilePage.seeRequests();

        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
                "weare.requestsListPagePage", receiver.getId());

        Assert.assertEquals(afterRequestCount, initialRequestsCount + 1, "Request not approved");
        assertTrue(actions.isElementVisible(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
                globalSeleniumUser.getPersonalProfile().getFirstName()));
        assertTrue(actions.isElementPresent(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
                globalSeleniumUser.getPersonalProfile().getFirstName()));
        assertTrue(actions.isElementClickable(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
                globalSeleniumUser.getPersonalProfile().getFirstName()));

        requestsListPage.logout();

        Request.approveRequest(receiver, request);
        Request.disconnect(globalSeleniumUser, receiver);

    }

}
