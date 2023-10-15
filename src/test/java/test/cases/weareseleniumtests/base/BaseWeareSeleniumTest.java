package test.cases.weareseleniumtests.base;

import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    @BeforeClass
    public void beforeTestSetUp() {
        UserActions.loadBrowser("weare.baseUrl");
    }


    @AfterClass
    public void afterTestTearDown() {
        UserActions.quitDriver();
    }

//    protected static void login() {
//        LoginPage loginPage = new LoginPage(actions.getDriver());
//        loginPage.loginUser();
//    }

}
