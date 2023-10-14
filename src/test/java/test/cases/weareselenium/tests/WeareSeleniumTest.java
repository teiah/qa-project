package test.cases.weareselenium.tests;

import api.WEareApi;
import com.telerikacademy.testframework.models.PostModel;
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
    public void user_Can_Register_With_Valid_Credentials() {

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
    public void user_Can_Create_Post_With_Valid_Input() {
        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        String username = user.getUsername();
        String password = user.getPassword();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        String postMessage = helpers.generatePostContent();

        CreatePostPage createPostPage = new CreatePostPage(actions.getDriver());
        createPostPage.navigateToPage();
        createPostPage.createPost(postMessage);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.assertPageNavigated();
        latestPostsPage.assertPostIsCreated(postMessage);
    }

    @Test
    public void user_Can_Like_Post() {
        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        String username = user.getUsername();
        String password = user.getPassword();

        boolean publicVisibility = true;
        PostModel createdPost = weareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.navigateToPage();
        latestPostsPage.clickLikeButton(postId);

        latestPostsPage.assertPostIsLiked(postId);
    }


    @Test
    public void admin_User_Can_Edit_Another_Users_Post() {
        UserModel user = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel createdPost = weareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        String username = adminUser.getUsername();
        String password = adminUser.getPassword();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        EditPostPage editPostPage = new EditPostPage(actions.getDriver(), postId);
        editPostPage.navigateToPage();
        String message = "vse taq";
        editPostPage.editPostVisibility();
        editPostPage.editPostMessage(message);
        editPostPage.savePostChanges();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        Assert.assertTrue(postPage.messageIs(message), "Post message is not changed to " + message);
    }


    @Test
    public void admin_User_Can_Delete_Another_Users_Post() {
        UserModel user = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel createdPost = weareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        String username = adminUser.getUsername();
        String password = adminUser.getPassword();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        DeletePostPage deletePostPage = new DeletePostPage(actions.getDriver(), postId);
        deletePostPage.navigateToPage();
        deletePostPage.deletePost();

        Assert.assertTrue(actions.isElementPresent("weare.deletePostPage.deleteConfirmationMessage"),
                "Deletion confirmation is not present");

    }

}
