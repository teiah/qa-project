package test.cases.weareseleniumtests.base;

import com.telerikacademy.testframework.UserActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;


public class BaseWeareSeleniumTest extends BaseTestSetup {

    protected UserActions actions = new UserActions();

    @BeforeClass
    public void setUpSelenium() {
        UserActions.loadBrowser("weare.baseUrl");
    }


    @AfterClass
    public void tearDownSelenium() {
        UserActions.quitDriver();
    }

}
