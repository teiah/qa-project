package test.cases.selenium.base;

import api.models.models.User;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.*;
import api.controllers.UserController;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();
    protected User globalSeleniumUser = new User();
    protected User globalSeleniumAdminUser = new User();
    protected String globalUserUsername;
    protected String globalUserPassword;
    protected String adminUsername;
    protected String adminPassword;

    @BeforeClass
    public void setUpSelenium() {
        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_ADMIN.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserController.register(globalSeleniumAdminUser, username, password, email, authority);
        adminUsername = globalSeleniumAdminUser.getUsername();
        adminPassword = globalSeleniumAdminUser.getPassword();
        UserController.register(globalSeleniumUser, username, password, email, authority);
        globalUserUsername = globalSeleniumUser.getUsername();
        globalUserPassword = globalSeleniumUser.getPassword();
        UserActions.loadBrowser("weare.baseUrl");
    }

    @AfterClass
    public void disableGlobalUser() {
        UserController.disableUser(globalSeleniumAdminUser, globalSeleniumUser);
    }

    @AfterSuite
    public void tearDownSelenium() {
        UserActions.quitDriver();
    }

}
