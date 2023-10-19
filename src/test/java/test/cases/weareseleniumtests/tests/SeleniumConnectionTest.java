package test.cases.weareseleniumtests.tests;

import api.models.RequestModel;
import api.models.UserModel;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.ProfilePage;
import com.telerikacademy.testframework.pages.weare.RequestsListPage;
import com.telerikacademy.testframework.utils.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SeleniumConnectionTest extends BaseWeareSeleniumTest {

    @Test
    public void ConnectionRequestSent_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = weAreApi.getUserRequests(receiver).length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        int currentRequestsCount = weAreApi.getUserRequests(receiver).length;

        Assert.assertEquals(currentRequestsCount, initialRequestsCount + 1, "Request not received");

        weAreApi.disableUser(globalSeleniumAdminUser, sender.getId());
        weAreApi.disableUser(globalSeleniumAdminUser, receiver.getId());
    }

    @Test
    public void ConnectionRequestApproved_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = weAreApi.getUserRequests(receiver).length;

        RequestModel request = weAreApi.sendRequest(sender, receiver);

        int afterRequestCount = weAreApi.getUserRequests(receiver).length;

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());

        receiverProfilePage.navigateToPage();

        receiverProfilePage.seeRequests();

        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
                "weare.requestsListPagePage", receiver.getId());

        requestsListPage.approveRequest(sender.getPersonalProfile().getFirstName());

        int currentRequestsCount = weAreApi.getUserRequests(receiver).length;

        Assert.assertEquals(currentRequestsCount, afterRequestCount - 1, "Request not approved");

        requestsListPage.logout();

        weAreApi.disableUser(globalSeleniumAdminUser, sender.getId());
        weAreApi.disableUser(globalSeleniumAdminUser, receiver.getId());

    }

    @Test
    public void ConnectionCutOff_From_ConnectedUser() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = weAreApi.sendRequest(sender, receiver);

        weAreApi.approveRequest(receiver, sendRequest);

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.disconnect();

        weAreApi.disableUser(globalSeleniumAdminUser, sender.getId());
        weAreApi.disableUser(globalSeleniumAdminUser, receiver.getId());
    }

    @Test
    public void RequestReceived_By_User() {

        UserModel sender = weAreApi.registerUser(ROLE_USER.toString());
        UserModel receiver = weAreApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = weAreApi.getUserRequests(receiver).length;

        RequestModel request = weAreApi.sendRequest(sender, receiver);

        int afterRequestCount = weAreApi.getUserRequests(receiver).length;

        assertEquals(request.getSender().getId(), sender.getId(), "Sender doesn't match the one in the request.");
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
                sender.getPersonalProfile().getFirstName()));
        assertTrue(actions.isElementPresent(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
                sender.getPersonalProfile().getFirstName()));
        assertTrue(actions.isElementClickable(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
                sender.getPersonalProfile().getFirstName()));

        requestsListPage.logout();

        weAreApi.disableUser(globalSeleniumAdminUser, sender.getId());
        weAreApi.disableUser(globalSeleniumAdminUser, receiver.getId());

    }

}
