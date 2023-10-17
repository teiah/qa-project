package test.cases.weareseleniumtests.tests;

import api.models.RequestModel;
import api.models.UserModel;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.ProfilePage;
import com.telerikacademy.testframework.pages.weare.RequestsListPage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.assertEquals;

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

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
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

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());

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

        WEareApi.disableUser(globalAdminUser, sender.getId());
        WEareApi.disableUser(globalAdminUser, receiver.getId());
    }

    @Test
    public void userCanReceiveUserRequest() {


    }

}
