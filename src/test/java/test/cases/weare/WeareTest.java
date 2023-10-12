package test.cases.weare;

import api.UserModel;
import com.github.javafaker.Faker;
import com.telerikacademy.testframework.pages.weare.HomePage;
import com.telerikacademy.testframework.pages.weare.LoginPage;
import com.telerikacademy.testframework.pages.weare.RegisterPage;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeareTest extends BaseWeareTest {


    @Test
    public void user_Can_Register_With_Valid_Credentials() {
        Faker faker = new Faker();

        // Generate a random username and password
        String username = faker.name().firstName() + faker.name().lastName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();


        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, email, password);
        String userId = registerPage.extractUserId();

        UserModel registeredUser = WEareApi.getUserById(userId, username);

        Assert.assertEquals(username, registeredUser.getUsername());
        Assert.assertEquals(email, registeredUser.getEmail());
        Assert.assertEquals(userId, registeredUser.getId());


    }

    @Test
    public void user_Can_Login_With_Valid_Credentials() {
        Faker faker = new Faker();

        // Generate a random username and password
        String username = faker.name().username();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();


        RegisterPage registerPage = new RegisterPage(actions.getDriver());
        registerPage.registerUser(username, password, email);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();
    }
}
