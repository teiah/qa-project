package test.cases.weareseleniumtests.tests;

import models.models.RequestModel;
import models.models.UserModel;
import pages.weare.LoginPage;
import pages.weare.ProfilePage;
import pages.weare.RequestsListPage;
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

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        int initialRequestsCount = receiver.getUserRequests().length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        int currentRequestsCount = receiver.getUserRequests().length;

        Assert.assertEquals(currentRequestsCount, initialRequestsCount + 1, "Request not received");

        globalSeleniumAdminUser.disableUser(sender.getId());
        globalSeleniumAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void ConnectionRequestApproved_By_User() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        int initialRequestsCount = receiver.getUserRequests().length;

        RequestModel request = sender.sendRequest(receiver);

        int afterRequestCount = receiver.getUserRequests().length;

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

        int currentRequestsCount = receiver.getUserRequests().length;

        Assert.assertEquals(currentRequestsCount, afterRequestCount - 1, "Request not approved");

        requestsListPage.logout();

        globalSeleniumAdminUser.disableUser(sender.getId());
        globalSeleniumAdminUser.disableUser(receiver.getId());

    }

    @Test
    public void ConnectionCutOff_From_ConnectedUser() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        RequestModel sendRequest = sender.sendRequest(receiver);

        receiver.approveRequest(sendRequest);

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.disconnect();

        globalSeleniumAdminUser.disableUser(sender.getId());
        globalSeleniumAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void RequestReceived_By_User() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        int initialRequestsCount = receiver.getUserRequests().length;

        RequestModel request = sender.sendRequest(receiver);

        int afterRequestCount = receiver.getUserRequests().length;

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

        globalSeleniumAdminUser.disableUser(sender.getId());
        globalSeleniumAdminUser.disableUser(receiver.getId());

    }

}
