package test.cases.selenium.base;

import api.models.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.*;
import api.controllers.User;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();
    protected UserModel globalSeleniumUser = new UserModel();
    protected UserModel globalSeleniumAdminUser = new UserModel();
    protected String globalUserUsername;
    protected String globalUserPassword;
    protected String adminUsername;
    protected String adminPassword;

    @BeforeClass
    public void setUpSelenium() {
        User.register(globalSeleniumAdminUser, ROLE_ADMIN.toString());
        adminUsername = globalSeleniumAdminUser.getUsername();
        adminPassword = globalSeleniumAdminUser.getPassword();
        User.register(globalSeleniumUser, ROLE_USER.toString());
        globalUserUsername = globalSeleniumUser.getUsername();
        globalUserPassword = globalSeleniumUser.getPassword();
        UserActions.loadBrowser("weare.baseUrl");
    }

    @AfterClass
    public void disableGlobalUser() {
        User.disableUser(globalSeleniumAdminUser, globalSeleniumUser);
    }

    @AfterSuite
    public void tearDownSelenium() {
        UserActions.quitDriver();
    }

}
