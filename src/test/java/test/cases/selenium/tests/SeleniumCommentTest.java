package test.cases.selenium.tests;

import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.*;
import pages.weare.LoginPage;
import pages.weare.PostPage;
import api.controllers.CommentController;
import api.controllers.PostController;
import api.models.models.*;
import test.cases.selenium.base.BaseWeareSeleniumTest;

public class SeleniumCommentTest extends BaseWeareSeleniumTest {
    Integer postId;
    boolean publicVisibility = true;

    @AfterMethod
    public void cleanUpSeleniumCommentTest() {
        PostController.deletePost(globalSeleniumUser, postId);
    }

    @Test
    public void commentOfPublicPostCreated_When_ValidDataProvided() {

        Post createdPost = PostController.createPost(globalSeleniumUser, publicVisibility);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String commentMessage = Helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.createComment(commentMessage);
        postPage.assertPostCommentsCountUpdates("1 Comments");
        postPage.assertPostCommentsAuthorExists(globalUserUsername);
        postPage.assertPostCommentTextExists(commentMessage);

    }

    @Test
    public void commentOfPublicPostEdited_By_Author() {

        Post createdPost = PostController.createPost(globalSeleniumUser, publicVisibility);
        postId = createdPost.getPostId();
        Comment createdComment = CommentController.createComment(globalSeleniumUser, createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String editedCommentMessage = Helpers.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.editCommentNavigate();
        postPage.editComment(editedCommentMessage);
        postPage.assertPostCommentEditedTextExists(editedCommentMessage);

    }

    @Test
    public void commentOfPublicPostDeleted_By_Author() {

        Post createdPost = PostController.createPost(globalSeleniumUser, publicVisibility);
        postId = createdPost.getPostId();
        Comment createdComment = CommentController.createComment(globalSeleniumUser, createdPost);
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
    public void commentOfPublicPostLiked_By_User() {

        Post createdPost = PostController.createPost(globalSeleniumUser, publicVisibility);
        postId = createdPost.getPostId();
        Comment createdComment = CommentController.createComment(globalSeleniumUser, createdPost);
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
