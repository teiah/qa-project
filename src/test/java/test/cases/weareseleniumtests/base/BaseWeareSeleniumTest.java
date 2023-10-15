package test.cases.weareseleniumtests.base;

import api.WEareApi;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.models.UserModel;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.log4testng.Logger;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;


public class BaseWeareSeleniumTest {

    protected api.WEareApi WEareApi;
    protected UserModel globalAdminUser;
    protected Helpers helpers;
    protected UserActions actions;
    @BeforeClass
    public void setUpSelenium() {
        WEareApi = new WEareApi();
        helpers = new Helpers();
        actions = new UserActions();
        globalAdminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        Logger logger;

        UserActions.loadBrowser("weare.baseUrl");
    }


    @AfterClass
    public void tearDownSelenium() {
        WEareApi.disableUser(globalAdminUser, globalAdminUser.getId());
        UserActions.quitDriver();
    }

//    protected static void login() {
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser();
//    }

}
