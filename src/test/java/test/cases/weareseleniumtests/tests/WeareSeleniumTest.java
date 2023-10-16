package test.cases.weareseleniumtests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import api.models.RequestModel;
import api.models.UserByIdModel;
import api.models.UserModel;
import com.telerikacademy.testframework.pages.weare.*;
import com.telerikacademy.testframework.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;


import static com.telerikacademy.testframework.utils.UserRoles.*;
import static com.telerikacademy.testframework.utils.Utils.getConfigPropertyByKey;
import static com.telerikacademy.testframework.utils.Utils.getUIMappingByKey;
import static org.testng.AssertJUnit.assertEquals;

public class WeareSeleniumTest extends BaseWeareSeleniumTest {
    protected UserModel user;
    protected UserModel adminUser;
    protected String username;
    protected String adminUsername;
    protected String password;
    protected String adminPassword;


    @BeforeClass
    public void setUp() {

        user = WEareApi.registerUser(ROLE_USER.toString());
        username = user.getUsername();
        password = user.getPassword();

        adminUser = this.WEareApi.registerUser(ROLE_ADMIN.toString());
        adminUsername = adminUser.getUsername();
        adminPassword = adminUser.getPassword();


    }


    @Test
    public void user_Can_Register_With_Valid_Credentials() {

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

        actions.clickElement("//a[text()=\"Home\"]");
        actions.clickElement("//a[text()=\"LOGOUT\"]");

    }

    @Test
    public void admin_User_Can_Edit_Another_Users_Post() {

        boolean publicVisibility = true;
        PostModel createdPost = this.WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(adminUsername, adminPassword);

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

        boolean publicVisibility = true;
        PostModel createdPost = this.WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(adminUsername, adminPassword);

        DeletePostPage deletePostPage = new DeletePostPage(actions.getDriver(), postId);
        deletePostPage.navigateToPage();
        deletePostPage.deletePost();

        Assert.assertTrue(actions.isElementPresent("weare.deletePostPage.deleteConfirmationMessage"),
                "Deletion confirmation is not present");

    }


    @Test
    public void user_Can_Send_Request_To_Another_User() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = WEareApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        RequestModel[] requestsAfter = WEareApi.getUserRequests(receiver);
        int currentRequestsCount = requestsAfter.length;

        Assert.assertEquals(currentRequestsCount, previousRequestsCount + 1, "Request not received");

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());
    }

    @Test
    public void user_Can_Approve_Request_From_Another_User() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel[] requests = WEareApi.getUserRequests(receiver);
        int previousRequestsCount = requests.length;

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        receiverProfilePage.sendRequest();

        RequestModel[] requestsAfter = WEareApi.getUserRequests(receiver);
        int currentRequestsCount = requestsAfter.length;

        Assert.assertEquals(currentRequestsCount, previousRequestsCount + 1, "Request not received");

        receiverProfilePage.logout();

        loginPage.loginUser(receiver.getUsername(), receiver.getPassword());

        receiverProfilePage.navigateToPage();

        receiverProfilePage.seeRequests();

        RequestsListPage requestsListPage = new RequestsListPage(actions.getDriver(),
                "weare.requestsListPagePage", receiver.getId());

        requestsListPage.approveRequest(sender.getPersonalProfile().getFirstName());

        requestsListPage.logout();

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());

    }

    @Test
    public void user_Can_Disconnect_From_Another_User() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        RequestModel sendRequest = WEareApi.sendRequest(sender, receiver);

        WEareApi.approveRequest(receiver, sendRequest);

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(sender.getUsername(), sender.getPassword());

        ProfilePage receiverProfilePage = new ProfilePage(actions.getDriver(), receiver.getId());
        receiverProfilePage.navigateToPage();
        receiverProfilePage.assertPageNavigated();

        WEareApi.disableUser(sender, sender.getId());
        WEareApi.disableUser(receiver, receiver.getId());
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
        postPage.assertPostCommentsCountUpdates("1 Comments");
        postPage.assertPostCommentsAuthorExists(username);
        postPage.assertPostCommentTextExists(commentMessage);
        WEareApi.deletePost(user, postId);
    }

    @Test
    public void userCanEditCommentWithValidInput() {

        boolean publicVisibility = true;
        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(user, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        String editedCommentMessage = helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.editCommentNavigate();
        postPage.editComment(editedCommentMessage);
        postPage.assertPostCommentEditedTextExists(editedCommentMessage);
        WEareApi.deletePost(user, postId);
    }

    @Test
    public void userCanDeleteOwnComment() {
        boolean publicVisibility = true;
        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(user, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.deleteComment();
        // postPage.assertPostCommentDeleted();
        WEareApi.deletePost(user, postId);
    }
    @Test
    public void userCanLikeComment() {
        boolean publicVisibility = true;
        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        Integer postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(user, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.likeComment();
        // postPage.assertPostCommentLiked();
        WEareApi.deletePost(user, postId);
    }
}
