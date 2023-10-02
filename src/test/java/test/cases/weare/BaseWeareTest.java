package test.cases.weare;

import api.WEareApi;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseWeareTest {

    protected static WEareApi WEareApi = new WEareApi();
    protected static UserActions actions = new UserActions();

    @BeforeClass
    public static void beforeTestSetUp() {
        UserActions.loadBrowser("weare.homePage");
        login();
    }

    @AfterClass
    public static void afterTestTearDown() {
        UserActions.quitDriver();
    }

    protected static void login() {
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser();
    }

}
