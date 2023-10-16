package test.cases.weareseleniumtests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import com.telerikacademy.testframework.pages.weare.*;
import org.testng.annotations.Test;
import test.cases.weareseleniumtests.base.BaseWeareSeleniumTest;


import static org.testng.AssertJUnit.assertEquals;

public class SeleniumCommentTest extends BaseWeareSeleniumTest {

    @Test
    public void user_Can_Create_Comment_With_Valid_Input() {

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
    public void user_Can_Edit_Comment_With_Valid_Input() {

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
    public void user_Can_Delete_Own_Comment() {
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
    public void user_Can_Like_Comment() {
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
