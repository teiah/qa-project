package test.cases.weareselenium.tests;

import com.telerikacademy.testframework.models.UserModel;
import com.github.javafaker.Faker;
import com.telerikacademy.testframework.pages.weare.HomePage;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.RegisterPage;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.weareselenium.base.BaseWeareTest;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class WeareTest extends BaseWeareTest {

    Helpers helpers = new Helpers();

    @Test
    public void user_Can_Register_With_Valid_Credentials() {

        // Generate a random username and password
        String username = helpers.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = helpers.generatePassword();
        String email = helpers.generateEmail();


        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        int userId = Integer.parseInt(registerPage.extractUserId());

        UserModel registeredUser = WEareApi.getUserById(username, userId).as(UserModel.class);

        Assert.assertEquals(registeredUser.getUsername(), username);
        Assert.assertEquals(registeredUser.getEmail(), email);
        Assert.assertEquals(registeredUser.getId(), userId);


    }

    @Test
    public void user_Can_Login_With_Valid_Credentials() {

        // Generate a random username and password
        String username = helpers.generateUsernameAsImplemented(ROLE_USER.toString());
        String password = helpers.generatePassword();
        String email = helpers.generateEmail();


        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, password, email);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();
    }
}
