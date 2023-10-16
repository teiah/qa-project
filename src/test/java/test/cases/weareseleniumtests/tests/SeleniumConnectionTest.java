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

public class SeleniumConnectionTest extends BaseWeareSeleniumTest {

    @Test
    public void userCanSendRequestToAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = WEareApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        RequestModel[] requestsAfter = WEareApi.getUserRequests(receiver);
        int currentRequestsCount = requestsAfter.length;

        Assert.assertEquals(currentRequestsCount, previousRequestsCount + 1, "Request not received");

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());
    }

    @Test
    public void userCanApproveRequestFromAnotherUser() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = WEareApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        RequestModel[] requestsAfter = WEareApi.getUserRequests(receiver);
        int currentRequestsCount = requestsAfter.length;

        Assert.assertEquals(currentRequestsCount, previousRequestsCount + 1, "Request not received");

        receiverProfilePage.logout();

        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());

        receiverProfilePage.navigateToPage();

        receiverProfilePage.seeRequests();

        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
                "weare.requestsListPagePage", receiver.getId());

        requestsListPage.approveRequest(sender.getPersonalProfile().getFirstName());

        requestsListPage.logout();

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());

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

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());
    }

}
