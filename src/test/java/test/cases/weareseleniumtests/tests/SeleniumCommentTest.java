package test.cases.weareseleniumtests.tests;

import models.models.CommentModel;
import models.models.PostModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.weare.LoginPage;
import pages.weare.PostPage;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;


public class SeleniumCommentTest extends BaseWeareSeleniumTest {
    Integer postId;
    boolean publicVisibility = true;

    @AfterMethod
    public void cleanUpSeleniumCommentTest() {
        globalUser.deletePost(postId);
    }

    @Test
    public void CommentOfPublicPostCreated_When_ValidDataProvided() {

        PostModel createdPost = globalUser.createPost(publicVisibility);
        postId = createdPost.getPostId();

        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String commentMessage = globalUser.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.createComment(commentMessage);
        postPage.assertPostCommentsCountUpdates("1 Comments");
        postPage.assertPostCommentsAuthorExists(globalUserUsername);
        postPage.assertPostCommentTextExists(commentMessage);

    }

    @Test
    public void CommentOfPublicPostEdited_By_Author() {

        PostModel createdPost = globalUser.createPost(publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = globalUser.createComment(createdPost);
        LoginPage loginPage = new LoginPage(actions.getDriver());
        loginPage.loginUser(globalUserUsername, globalUserPassword);

        String editedCommentMessage = globalUser.generateCommentContent();

        PostPage postPage = new PostPage(actions.getDriver(), postId);
        postPage.navigateToPage();
        postPage.showComment();
        postPage.editCommentNavigate();
        postPage.editComment(editedCommentMessage);
        postPage.assertPostCommentEditedTextExists(editedCommentMessage);

    }

    @Test
    public void CommentOfPublicPostDeleted_By_Author() {

        PostModel createdPost = globalUser.createPost(publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = globalUser.createComment(createdPost);
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

        PostModel createdPost = globalUser.createPost(publicVisibility);
        postId = createdPost.getPostId();
        CommentModel createdComment = globalUser.createComment(createdPost);
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
