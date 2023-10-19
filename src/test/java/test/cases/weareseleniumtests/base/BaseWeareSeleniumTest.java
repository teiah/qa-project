package test.cases.weareseleniumtests.base;

import api.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();
    protected UserModel globalUser;
    protected UserModel globalSeleniumAdminUser;
    protected String globalUserUsername;
    protected String globalUserPassword;
    protected String adminUsername;
    protected String adminPassword;

    @BeforeClass
    public void setUpSelenium() {
        globalSeleniumAdminUser = weAreApi.registerUser(ROLE_ADMIN.toString());
        adminUsername = globalSeleniumAdminUser.getUsername();
        adminPassword = globalSeleniumAdminUser.getPassword();
        globalUser = weAreApi.registerUser(ROLE_USER.toString());
        globalUserUsername = globalUser.getUsername();
        globalUserPassword = globalUser.getPassword();
        UserActions.loadBrowser("weare.baseUrl");
    }

    @AfterClass
    public void disableGlobalUser() {
        weAreApi.disableUser(globalSeleniumAdminUser, globalUser.getId());
    }

    @AfterSuite
    public void tearDownSelenium() {
        UserActions.quitDriver();
    }

}
