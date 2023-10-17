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
    public void userCanSendRequestToAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = WEareApi.getUserRequests(receiver).length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        int currentRequestsCount = WEareApi.getUserRequests(receiver).length;

        Assert.assertEquals(currentRequestsCount, initialRequestsCount + 1, "Request not received");

        WEareApi.disableUser(globalSeleniumAdminUser, sender.getId());
        WEareApi.disableUser(globalSeleniumAdminUser, receiver.getId());
    }

    @Test
    public void userCanApproveRequestFromAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = WEareApi.getUserRequests(receiver).length;

        RequestModel request = WEareApi.sendRequest(sender, receiver);

        int afterRequestCount = WEareApi.getUserRequests(receiver).length;

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

        int currentRequestsCount = WEareApi.getUserRequests(receiver).length;

        Assert.assertEquals(currentRequestsCount, afterRequestCount - 1, "Request not approved");

        requestsListPage.logout();

        WEareApi.disableUser(globalSeleniumAdminUser, sender.getId());
        WEareApi.disableUser(globalSeleniumAdminUser, receiver.getId());

    }

    @Test
    public void userCanDisconnectFromAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = WEareApi.sendRequest(sender, receiver);

        WEareApi.approveRequest(receiver, sendRequest);

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        WEareApi.disableUser(globalSeleniumAdminUser, sender.getId());
        WEareApi.disableUser(globalSeleniumAdminUser, receiver.getId());
    }

    @Test
    public void userCanReceiveUserRequest() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        int initialRequestsCount = WEareApi.getUserRequests(receiver).length;

        RequestModel request = WEareApi.sendRequest(sender, receiver);

        int afterRequestCount = WEareApi.getUserRequests(receiver).length;

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

        WEareApi.disableUser(globalSeleniumAdminUser, sender.getId());
        WEareApi.disableUser(globalSeleniumAdminUser, receiver.getId());

    }

}
