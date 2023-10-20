package test.cases.weareseleniumtests.tests;

import models.models.PostModel;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.weare.*;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;

import static models.models.UserModel.generatePostContent;

public class SeleniumPostTest extends BaseWeareSeleniumTest {

    // Post to be cleaned after each test
    private Integer postId;

    @AfterMethod
    public void cleanUp(ITestResult result) {
        HomePage homePage = new HomePage(actions.getDriver());
        homePage.navigateToPage();
        if (homePage.isLoggedIn()) {
            homePage.logout();
        }

        if (postId != null) {
            globalUser.deletePost(postId);
            postId = null;
        }
    }

    @Test
    // only text, default visibility private, no image
    public void PostCreated_When_ValidDataProvided() {


        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String postMessage = generatePostContent();

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
    public void PostLiked_By_User() {

        PostModel createdPost = globalUser.createPost(true);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        LatestPostsPage latestPostsPage = new LatestPostsPage(actions.getDriver());
        latestPostsPage.navigateToPage();
        latestPostsPage.clickLikeButton(postId);

        latestPostsPage.assertPostIsLiked(postId);

    }

    @Test
    public void PostEdited_By_AdminUser_When_NotAuthor() {

        PostModel createdPost = globalUser.createPost(true);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(adminUsername, adminPassword);

        EditPostPage editPostPage = new EditPostPage(actions.getDriver(), postId);
        editPostPage.navigateToPage();
        String message = generatePostContent();
        editPostPage.editPostVisibility();
        editPostPage.editPostMessage(message);
        editPostPage.savePostChanges();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        Assert.assertTrue(postPage.messageIs(message), "Post message is not changed to " + message);
    }

    @Test
    public void PostDeleted_By_AdminUser_When_NotAuthor() {

        PostModel createdPost = globalUser.createPost(true);
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
