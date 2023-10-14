package test.cases.weareselenium.tests;

import api.WEareApi;
import com.telerikacademy.testframework.models.UserByIdModel;
import com.telerikacademy.testframework.models.UserModel;
import com.telerikacademy.testframework.pages.weare.*;
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

    @Test
    // only text, default visibility private, no image
    public void user_Can_Create_Comment_With_Valid_Input() {
        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        // Generate a random username and password
        String username = user.getUsername();
        String password = user.getPassword();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.clickAddNewPost();

        String postMessage = helpers.generatePostContent();

        CreatePostPage createPostPage = new CreatePostPage(actions.getDriver());
        createPostPage.createPost(postMessage);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.assertPostIsCreated(postMessage);
    }

    @Test
    public void admin_User_Can_Edit_Another_Users_Post(){

    }

}
