package test.cases.weareseleniumtests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import com.telerikacademy.testframework.pages.weare.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;


public class SeleniumCommentTest extends BaseWeareSeleniumTest {
    Integer postId;
    boolean publicVisibility = true;

    @AfterMethod
    public void cleanUpSeleniumCommentTest() {
        WEareApi.deletePost(globalUser, postId);
    }

    @Test
    public void userCanCreateCommentWithValidInput() {

        PostModel createdPost = WEareApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String commentMessage = helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.createComment(commentMessage);
        postPage.assertPostCommentsCountUpdates("1 Comments");
        postPage.assertPostCommentsAuthorExists(globalUserUsername);
        postPage.assertPostCommentTextExists(commentMessage);

    }

    @Test
    public void userCanEditCommentWithValidInput() {

        PostModel createdPost = WEareApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(globalUser, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String editedCommentMessage = helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.editCommentNavigate();
        postPage.editComment(editedCommentMessage);
        postPage.assertPostCommentEditedTextExists(editedCommentMessage);

    }

    @Test
    public void userCanDeleteOwnComment() {

        PostModel createdPost = WEareApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(globalUser, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.deleteComment();
        postPage.assertPostCommentDeleted();
        postPage.assertDeleteCommentConfirmationTextExists();
        postPage.navigateToPage();
        postPage.assertPostCommentsCountUpdates("0 Comments");


    }

    @Test
    public void userCanLikeComment() {

        PostModel createdPost = WEareApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(globalUser, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.likeComment();

        postPage.assertPostCommentDislikeButtonIsPresent();
        postPage.assertPostCommentLiked();

    }
}
