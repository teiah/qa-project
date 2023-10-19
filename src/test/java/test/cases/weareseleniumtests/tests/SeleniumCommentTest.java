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
        weAreApi.deletePost(globalUser, postId);
    }

    @Test
    public void CommentOfPublicPostCreated_When_ValidDataProvided() {

        PostModel createdPost = weAreApi.createPost(globalUser, publicVisibility);
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
    public void CommentOfPublicPostEdited_By_Author() {

        PostModel createdPost = weAreApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = weAreApi.createComment(globalUser, createdPost);
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
    public void CommentOfPublicPostDeleted_By_Author() {

        PostModel createdPost = weAreApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = weAreApi.createComment(globalUser, createdPost);
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
    public void CommentOfPublicPostLiked_By_User() {

        PostModel createdPost = weAreApi.createPost(globalUser, publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = weAreApi.createComment(globalUser, createdPost);
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
