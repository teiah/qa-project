package test.cases.weareseleniumtests.tests;

import api.models.UserByIdModel;
import com.telerikacademy.testframework.pages.weare.HomePage;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;

public class SeleniumUserTest extends BaseWeareSeleniumTest {

    @Test
    public void userCanRegisterWithValidCredentials() {

        // Generate a random username and password
        String username = helpers.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = helpers.generatePasswordAsImplemented();
        String email = helpers.generateEmail();

        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        int userId = Integer.parseInt(registerPage.extractUserId());

        UserByIdModel registeredUser = WEareApi.getUserById(username, userId).as(UserByIdModel.class);

        Assert.assertEquals(registeredUser.getUsername(), username);
        Assert.assertEquals(registeredUser.getEmail(), email);
        Assert.assertEquals(registeredUser.getId(), userId);

        this.WEareApi.disableUser(globalAdminUser, registeredUser.getId());
    }

    @Test
    public void userCanLoginWithValidCredentials() {

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();

        this.WEareApi.disableUser(globalAdminUser, user.getId());

        homePage.logout();
    }

}
