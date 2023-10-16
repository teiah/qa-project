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
    public void tearDownSeleniumCommentTest() {
        WEareApi.deletePost(user, postId);
    }

    @Test
    public void userCanCreateCommentWithValidInput() {


        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        String commentMessage = helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.createComment(commentMessage);
        postPage.assertPostCommentsCountUpdates("1 Comments");
        postPage.assertPostCommentsAuthorExists(username);
        postPage.assertPostCommentTextExists(commentMessage);

    }

    @Test
    public void userCanEditCommentWithValidInput() {

        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        postId = createdPost.getPostId();
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

    }

    @Test
    public void userCanDeleteOwnComment() {

        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(user, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

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

        PostModel createdPost = WEareApi.createPost(user, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = WEareApi.createComment(user, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(username, password);

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.likeComment();

        postPage.assertPostCommentDislikeButtonIsPresent();
        postPage.assertPostCommentLiked();

    }
}
