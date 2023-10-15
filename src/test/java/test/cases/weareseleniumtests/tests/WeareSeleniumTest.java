package test.cases.weareseleniumtests.tests;

import api.models.PostModel;
import api.models.UserByIdModel;
import api.models.UserModel;
import com.telerikacademy.testframework.pages.weare.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;


import static com.telerikacademy.testframework.utils.UserRoles.*;

public class WeareSeleniumTest extends BaseWeareSeleniumTest {
    protected UserModel user;
    protected String username;
    protected String password;


    @BeforeClass
    public void setUp() {

        user = WEareApi.registerUser(ROLE_USER.toString());
        username = user.getUsername();
        password = user.getPassword();

    }


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

        this.WEareApi.disableUser(globalAdminUser, registeredUser.getId());
    }

    @Test
    public void user_Can_Login_With_Valid_Credentials() {

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        HomePage homePage = new HomePage(actions.getDriver());
        homePage.assertUserHasLoggedIn();

        this.WEareApi.disableUser(globalAdminUser, user.getId());

        homePage.logout();
    }

    @Test
    // only text, default visibility private, no image
    public void user_Can_Create_Post_With_Valid_Input() {


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


        boolean publicVisibility = true;
        PostModel createdPost = this.WEareApi.createPost(user, publicVisibility);
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

        boolean publicVisibility = true;
        PostModel createdPost = this.WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        UserModel adminUser = this.WEareApi.registerUser(ROLE_ADMIN.toString());
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
        UserModel user = this.WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel createdPost = this.WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        UserModel adminUser = this.WEareApi.registerUser(ROLE_ADMIN.toString());
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

    @Test
    public void userCanCreateCommentWithValidInput() {

        boolean publicVisibility = true;
        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        String commentMessage = helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.createComment(commentMessage);
        WEareApi.deletePost(user, postId);
    }


}
