package test.cases.wearerestassured.tests.tests;

import api.WEareApi;
import api.models.CommentModel;
import api.models.PostModel;
import api.models.UserModel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseWeareRestAssuredTest {
    private UserModel commentUser;

    @BeforeClass
    public void setUp() {
        commentUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        WEareApi.disableUser(globalAdminUser, commentUser.getId());
    }

    @Test
    public void userCanCreateCommentOfAPublicPostWithValidData() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanCreateCommentOfAPrivatePostWithValidDataIfConnected() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        WEareApi.connectUsers(commentUser, newUser);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCannotCreateCommentOfAPrivatePostWithValid_Data() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNull(comment, "Comment was made.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllComments() {

        CommentModel[] comments = WEareApi.findAllComments();

        for (CommentModel comment : comments) {
            assertEquals(comment.getClass(), CommentModel.class, "There are no comments found");
            assertNotNull(comment.getCommentId(), "There are no comments found");
        }
    }

    @Test
    public void userCanEditCommentOfAPostWith_Valid_Data() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(commentUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(commentUser, comment);

        WEareApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(commentUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPublicPostWithValidData() {

        UserModel newUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(newUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalAdminUser, comment);

        WEareApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPrivatePostWithValid_Data() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(commentUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(adminUser, comment);

        WEareApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(adminUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanLikeCommentOfAPublicPost() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());
        UserModel userToLikeComment = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeLiked = WEareApi.createComment(newUser, post);
        CommentModel likedComment = WEareApi.likeComment(userToLikeComment, commentToBeLiked.getCommentId());

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size() + 1;
        assertEquals(likedComment.getLikes().size(), likedCommentLikesToHave, "Comment was not liked.");
        assertEquals(commentToBeLiked.getCommentId(), likedComment.getCommentId(), "Liked comment is different.");

        WEareApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(WEareApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanDeleteCommentOfAPublicPost() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPublicPost() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(adminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPrivatePost() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        WEareApi.connectUsers(commentUser, newUser);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(adminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllCommentsOfA_Post() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            WEareApi.createComment(newUser, post);
        }

        CommentModel[] postComments = WEareApi.findAllCommentsOfAPost(commentUser, post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertNotNull(comment, "Comment is null");
        }

        for (CommentModel comment : postComments) {
            WEareApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");
        }

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_A_Comment_By_Id() {

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);
        int commentId = comment.getCommentId();

        CommentModel foundComment = WEareApi.getCommentById(commentUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

}
