package test.cases.selenium.tests;

import com.telerikacademy.testframework.utils.Helpers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.weare.*;
import api.controllers.PostController;
import api.models.models.Post;
import test.cases.selenium.base.BaseWeareSeleniumTest;

public class SeleniumPostTest extends BaseWeareSeleniumTest {

    private Integer postId;

    @AfterMethod
    public void cleanUp(ITestResult result) {
        HomePage homePage = new HomePage(actions.getDriver());
        homePage.navigateToPage();
        if (homePage.isLoggedIn()) {
            homePage.logout();
        }

        if (postId != null) {
            PostController.deletePost(globalSeleniumUser, postId);
            postId = null;
        }
    }

    @Test
    // only text, default visibility private, no image
    public void postCreated_When_ValidDataProvided() {

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String postMessage = Helpers.generatePostContent();

        CreatePostPage createPostPage = new CreatePostPage(actions.getDriver());
        createPostPage.navigateToPage();
        createPostPage.createPost(postMessage);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.assertPageNavigated();
        latestPostsPage.assertPostIsCreated(postMessage);

        postId = latestPostsPage.extractPostId();

        Assert.assertNotNull(postId, "Unable to find latest post id");

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.assertPageNavigated();
    }

    @Test
    public void postLiked_By_User() {

        Post createdPost = PostController.createPost(globalSeleniumUser, true);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.navigateToPage();
        latestPostsPage.clickLikeButton(postId);

        latestPostsPage.assertPostIsLiked(postId);

    }

    @Test
    public void postEdited_By_AdminUser_When_NotAuthor() {

        Post createdPost = PostController.createPost(globalSeleniumUser, true);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(adminUsername, adminPassword);

        EditPostPage editPostPage = new EditPostPage(actions.getDriver(), postId);
        editPostPage.navigateToPage();
        String message = Helpers.generatePostContent();
        editPostPage.editPostVisibility();
        editPostPage.editPostMessage(message);
        editPostPage.savePostChanges();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        Assert.assertTrue(postPage.messageIs(message), "Post message is not changed to " + message);
    }

    @Test
    public void postDeleted_By_AdminUser_When_NotAuthor() {

        Post createdPost = PostController.createPost(globalSeleniumUser, true);
        Integer postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(adminUsername, adminPassword);

        DeletePostPage deletePostPage = new DeletePostPage(actions.getDriver(), postId);
        deletePostPage.navigateToPage();
        deletePostPage.deletePost();
        Assert.assertTrue(actions.isElementPresent("weare.deletePostPage.deleteConfirmationMessage"),
                "Deletion confirmation is not present");

    }

}
