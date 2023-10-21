package test.cases.weareseleniumtests.base;

import models.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();
    protected UserModel globalSeleniumUser;
    protected UserModel globalSeleniumAdminUser;
    protected String globalUserUsername;
    protected String globalUserPassword;
    protected String adminUsername;
    protected String adminPassword;

    @BeforeClass
    public void setUpSelenium() {
        globalSeleniumAdminUser = new UserModel();
        globalSeleniumAdminUser.register(ROLE_ADMIN.toString());
        adminUsername = globalSeleniumAdminUser.getUsername();
        adminPassword = globalSeleniumAdminUser.getPassword();
        globalSeleniumUser = new UserModel();
        globalSeleniumUser.register(ROLE_USER.toString());
        globalUserUsername = globalSeleniumUser.getUsername();
        globalUserPassword = globalSeleniumUser.getPassword();
        UserActions.loadBrowser("weare.baseUrl");
    }

    @AfterClass
    public void disableGlobalUser() {
        globalSeleniumAdminUser.disableUser(globalSeleniumUser.getId());
    }

    @AfterSuite
    public void tearDownSelenium() {
        UserActions.quitDriver();
    }

}
