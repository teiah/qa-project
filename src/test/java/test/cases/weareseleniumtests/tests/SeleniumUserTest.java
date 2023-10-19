package test.cases.weareseleniumtests.tests;

import api.models.UserByIdModel;
import api.models.UserModel;
import com.telerikacademy.testframework.pages.weare.HomePage;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;

public class SeleniumUserTest extends BaseWeareSeleniumTest {

    private Integer registeredUserId;

    @AfterMethod
    public void cleanUp() {
        HomePage homePage = new HomePage(actions.getDriver());
        homePage.navigateToPage();
        if (homePage.isLoggedIn()) {
            homePage.logout();
        }
    }

    @Test
    public void UserRegistered_When_ValidDataProvided() {

        // Generate a random username and password
        String username = helpers.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = helpers.generatePassword();
        String email = helpers.generateEmail();

        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        registeredUserId = Integer.parseInt(registerPage.extractUserId());

        UserByIdModel registeredUser = weAreApi.getUserById(username, registeredUserId).as(UserByIdModel.class);

        Assert.assertEquals(registeredUser.getUsername(), username);
        Assert.assertEquals(registeredUser.getEmail(), email);
        Assert.assertEquals(registeredUser.getId(), registeredUserId);
    }

    @Test
    public void UserLoggedIn_When_ValidCredentialsProvided() {

        UserModel registeredUser = weAreApi.registerUser(ROLE_USER.toString());
        registeredUserId = registeredUser.getId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(registeredUser.getUsername(), registeredUser.getPassword());

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();
    }

}
