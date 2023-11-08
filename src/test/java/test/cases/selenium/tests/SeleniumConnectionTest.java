//package test.cases.selenium.tests;
//
//import com.telerikacademy.testframework.utils.Helpers;
//import org.testng.annotations.*;
//import pages.weare.LoginPage;
//import pages.weare.ProfilePage;
//import pages.weare.RequestsListPage;
//import com.telerikacademy.testframework.utils.Utils;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//import api.controllers.ConnectionController;
//import api.controllers.UserController;
//import api.models.models.*;
//import test.cases.selenium.base.BaseWeareSeleniumTest;
//
//import static com.telerikacademy.testframework.utils.Authority.ROLE_USER;
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertTrue;
//
//public class SeleniumConnectionTest extends BaseWeareSeleniumTest {
//
//    User receiver = new User();
//
//    @BeforeClass
//    public void setUpCommentTest() {
//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_USER.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        UserController.register(receiver, username, password, email, authority);
//    }
//
//    @AfterClass
//    public void cleanUpCommentTest() {
//        UserController.disableUser(globalSeleniumAdminUser, receiver);
//    }
//
//    @Test
//    public void connectionRequestSent_By_User() {
//
//        int initialRequestsCount = 0;
//        String[] fields = ConnectionController.getUserReceivedRequests(receiver);
//
//        if (fields != null) {
//            initialRequestsCount = fields.length;
//        }
//
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser(globalSeleniumUser.getUsername(), globalSeleniumUser.getPassword());
//
//        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
//        receiverProfilePage.navigateToPage();
//        receiverProfilePage.assertPageNavigated();
//
//        receiverProfilePage.sendRequest();
//
//        Request[] requestsAfter = ConnectionController.getUserRequests(receiver);
//        int afterRequestCount = requestsAfter.length;
//
//        Assert.assertEquals(afterRequestCount, initialRequestsCount + 1, "Request not received");
//
//        Request[] receiverRequests = ConnectionController.getUserRequests(receiver);
//        ConnectionController.approveRequest(receiver, receiverRequests[receiverRequests.length - 1]);
//        ConnectionController.disconnect(globalSeleniumUser, receiver);
//    }
//
//    @Test
//    public void connectionRequestApproved_By_User() {
//
//        int initialRequestsCount = ConnectionController.getUserRequests(receiver).length;
//
//        Request request = ConnectionController.sendRequest(globalSeleniumUser, receiver);
//
//        int afterRequestCount = ConnectionController.getUserRequests(receiver).length;
//
//        assertEquals(request.getSender().getId(), globalSeleniumUser.getId(), "Sender doesn't match the one in the request.");
//        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
//        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");
//
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());
//
//        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
//
//        receiverProfilePage.navigateToPage();
//
//        receiverProfilePage.seeRequests();
//
//        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
//                "weare.requestsListPagePage", receiver.getId());
//
//        requestsListPage.approveRequest(globalSeleniumUser.getPersonalProfile().getFirstName());
//
//        int currentRequestsCount = ConnectionController.getUserRequests(receiver).length;
//
//        Assert.assertEquals(currentRequestsCount, afterRequestCount - 1, "Request not approved");
//
//        requestsListPage.logout();
//
//        ConnectionController.disconnect(globalSeleniumUser, receiver);
//
//    }
//
//    @Test
//    public void connectionCutOff_From_ConnectedUser() {
//
//        Request sendRequest = ConnectionController.sendRequest(globalSeleniumUser, receiver);
//
//        ConnectionController.approveRequest(receiver, sendRequest);
//
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser(globalSeleniumUser.getUsername(), globalSeleniumUser.getPassword());
//
//        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
//        receiverProfilePage.navigateToPage();
//        receiverProfilePage.assertPageNavigated();
//
//        receiverProfilePage.disconnect();
//
//    }
//
//    @Test
//    public void requestReceived_By_User() {
//
//        int initialRequestsCount = ConnectionController.getUserRequests(receiver).length;
//
//        Request request = ConnectionController.sendRequest(globalSeleniumUser, receiver);
//
//        int afterRequestCount = ConnectionController.getUserRequests(receiver).length;
//
//        assertEquals(request.getSender().getId(), globalSeleniumUser.getId(), "Sender doesn't match the one in the request.");
//        assertEquals(request.getReceiver().getId(), receiver.getId(), "Receiver doesn't match the one in the request.");
//        assertEquals(afterRequestCount, initialRequestsCount + 1, "Request is not sent.");
//
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());
//
//        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
//
//        receiverProfilePage.navigateToPage();
//
//        receiverProfilePage.seeRequests();
//
//        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
//                "weare.requestsListPagePage", receiver.getId());
//
//        Assert.assertEquals(afterRequestCount, initialRequestsCount + 1, "Request not approved");
//        assertTrue(actions.isElementVisible(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
//                globalSeleniumUser.getPersonalProfile().getFirstName()));
//        assertTrue(actions.isElementPresent(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
//                globalSeleniumUser.getPersonalProfile().getFirstName()));
//        assertTrue(actions.isElementClickable(Utils.getUIMappingByKey("weare.requestsListPage.requestSenderInfo"),
//                globalSeleniumUser.getPersonalProfile().getFirstName()));
//
//        requestsListPage.logout();
//
//        ConnectionController.approveRequest(receiver, request);
//        ConnectionController.disconnect(globalSeleniumUser, receiver);
//
//    }
//
//}
