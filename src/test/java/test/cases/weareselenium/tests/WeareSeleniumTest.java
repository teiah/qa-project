package test.cases.weareselenium.tests;

import api.WEareApi;
import com.telerikacademy.testframework.models.UserByIdModel;
import com.telerikacademy.testframework.models.UserModel;
import com.telerikacademy.testframework.pages.weare.HomePage;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.RegisterPage;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.weareselenium.base.BaseWeareSeleniumTest;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class WeareSeleniumTest extends BaseWeareSeleniumTest {

    WEareApi weareApi = new WEareApi();
    Helpers helpers = new Helpers();

    @Test
    public void user_Can_Register_With_Valid_Credentials() throws InterruptedException {

        // Generate a random username and password
        String username = helpers.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = helpers.generatePassword();
        String email = helpers.generateEmail();

        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        int userId = Integer.parseInt(registerPage.extractUserId());

        UserByIdModel registeredUser = WEareApi.getUserById(username, userId).as(UserByIdModel.class);

        Assert.assertEquals(registeredUser.getUsername(), username);
        Assert.assertEquals(registeredUser.getEmail(), email);
        Assert.assertEquals(registeredUser.getId(), userId);

        weareApi.disableUser(globalAdminUser, registeredUser.getId());

    }

    @Test
    public void user_Can_Login_With_Valid_Credentials() {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        // Generate a random username and password
        String username = user.getUsername();
        String password = user.getPassword();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();

        weareApi.disableUser(globalAdminUser, user.getId());

        homePage.logout();

    }
}
