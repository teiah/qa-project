package test.cases.weareseleniumtests.tests;

import models.wearemodels.UserByIdModel;
import models.wearemodels.UserModel;
import pages.wearepages.HomePage;
import pages.wearepages.LoginPage;
import pages.wearepages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static models.wearemodels.UserModel.getUserById;
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
        String username = globalUser.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = globalUser.generatePassword();
        String email = globalUser.generateEmail();

        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        registeredUserId = Integer.parseInt(registerPage.extractUserId());

        UserByIdModel registeredUser = getUserById(username, registeredUserId).as(UserByIdModel.class);

        Assert.assertEquals(registeredUser.getUsername(), username);
        Assert.assertEquals(registeredUser.getEmail(), email);
        Assert.assertEquals(registeredUser.getId(), registeredUserId);
    }

    @Test
    public void UserLoggedIn_When_ValidCredentialsProvided() {

        UserModel registeredUser = new UserModel();
        registeredUser.register(ROLE_USER.toString());
        registeredUserId = registeredUser.getId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(registeredUser.getUsername(), registeredUser.getPassword());

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();
    }

}
