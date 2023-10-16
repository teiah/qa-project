package test.cases.weareseleniumtests.base;

import api.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();
    protected UserModel user;
    protected UserModel adminUser;
    protected String username;
    protected String adminUsername;
    protected String password;
    protected String adminPassword;

    @BeforeClass
    public void setUpSelenium() {
        UserActions.loadBrowser("weare.baseUrl");
        user = WEareApi.registerUser(ROLE_USER.toString());
        username = user.getUsername();
        password = user.getPassword();

        adminUser = this.WEareApi.registerUser(ROLE_ADMIN.toString());
        adminUsername = adminUser.getUsername();
        adminPassword = adminUser.getPassword();
    }


    @AfterClass
    public void tearDownSelenium() {

        UserActions.quitDriver();
    }

}
