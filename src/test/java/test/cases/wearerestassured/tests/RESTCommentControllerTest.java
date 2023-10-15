package test.cases.wearerestassured.tests;

import test.cases.BaseTestSetup;
import com.telerikacademy.testframework.models.CommentModel;
import com.telerikacademy.testframework.models.PostModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseTestSetup {
    private UserModel commentUser;

    @BeforeClass
    private void setUp() {
        commentUser = weareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        weareApi.disableUser(globalAdminUser, commentUser.getId());
    }

    @Test
    public void user_Can_Create_Comment_Of_A_Public_Post_With_Valid_Data() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = weareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        weareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void user_Can_Create_Comment_Of_A_Private_Post_With_Valid_Data_If_Connected() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        weareApi.connectUsers(commentUser, newUser);

        boolean publicVisibility = false;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = weareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        weareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void user_Cannot_Create_Comment_Of_A_Private_Post_With_Valid_Data() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = false;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = weareApi.createComment(newUser, post);

        assertNull(comment, "Comment was made.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_All_Comments() {

        CommentModel[] comments = weareApi.findAllComments();

        for (CommentModel comment : comments) {
            assertEquals(comment.getClass(), CommentModel.class, "There are no comments found");
            assertNotNull(comment.getCommentId(), "There are no comments found");
        }
    }

    @Test
    public void user_Can_Edit_Comment_Of_A_Post_With_Valid_Data() {

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = weareApi.createComment(commentUser, post);

        Response editedCommentResponse = weareApi.editComment(commentUser, comment);

        int statusCode = editedCommentResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        weareApi.deleteComment(commentUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Edit_Comment_Of_A_Public_Post_With_Valid_Data() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        UserModel newUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = weareApi.createComment(newUser, post);

        Response editedCommentResponse = weareApi.editComment(adminUser, comment);

        int statusCode = editedCommentResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        weareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Edit_Comment_Of_A_Private_Post_With_Valid_Data() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = weareApi.createComment(commentUser, post);

        Response editedCommentResponse = weareApi.editComment(adminUser, comment);

        int statusCode = editedCommentResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        weareApi.deleteComment(adminUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void user_Can_Like_Comment_Of_A_Public_Post() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());
        UserModel userToLikeComment = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeLiked = weareApi.createComment(newUser, post);
        CommentModel likedComment = weareApi.likeComment(userToLikeComment, commentToBeLiked.getCommentId());

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size() + 1;
        assertEquals(likedComment.getLikes().size(), likedCommentLikesToHave, "Comment was not liked.");
        assertEquals(commentToBeLiked.getCommentId(), likedComment.getCommentId(), "Liked comment is different.");

        weareApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(weareApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void user_Can_Delete_Comment_Of_A_Public_Post() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = weareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        weareApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(weareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Comment_Of_A_Public_Post() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = weareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        weareApi.deleteComment(adminUser, commentToBeDeletedId);
        assertFalse(weareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Comment_Of_A_Private_Post() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        weareApi.connectUsers(commentUser, newUser);

        boolean publicVisibility = false;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = weareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        weareApi.deleteComment(adminUser, commentToBeDeletedId);
        assertFalse(weareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_All_Comments_Of_A_Post() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            weareApi.createComment(newUser, post);
        }

        CommentModel[] postComments = weareApi.findAllCommentsOfAPost(commentUser, post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertEquals(comment.getClass(), CommentModel.class, "Wrong type of comment");
            assertNotNull(comment, "Comment is null");
        }

        for (CommentModel comment : postComments) {
            weareApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");
        }

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_A_Comment_By_Id() {

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = weareApi.createComment(newUser, post);
        int commentId = comment.getCommentId();

        CommentModel foundComment = weareApi.getCommentById(commentUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        weareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weareApi.deletePost(commentUser, post.getPostId());
        assertFalse(weareApi.postExists(post.getPostId()), "Post was not deleted.");

    }
}
