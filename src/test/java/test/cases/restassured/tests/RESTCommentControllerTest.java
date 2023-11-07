package test.cases.restassured.tests;

import api.models.models.CommentModel;
import api.models.models.PostModel;
import api.models.models.UserModel;
import org.testng.annotations.*;
import api.controllers.CommentController;
import api.controllers.PostController;
import api.controllers.ConnectionController;
import api.controllers.UserController;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseWeareRestAssuredTest {

    UserModel newUser = new UserModel();
    PostModel publicPost = new PostModel();
    PostModel privatePost = new PostModel();


    @BeforeClass
    public void setUpCommentTest() {
        UserController.register(newUser, ROLE_USER.toString());
        publicPost = PostController.createPost(globalRestApiUser, true);
        assertTrue(PostController.publicPostExists(publicPost.getPostId()), "Post not created.");
        privatePost = PostController.createPost(globalRestApiUser, false);
        assertTrue(PostController.privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
    }

    @AfterClass
    public void cleanUpCommentTest() {
        PostController.deletePost(globalRestApiUser, publicPost.getPostId());
        PostController.deletePost(globalRestApiUser, privatePost.getPostId());
        UserController.disableUser(globalRestApiAdminUser, newUser);
    }

    @Test
    public void commentOfPublicPostCreated_When_ValidDataProvided() {

        CommentModel comment = CommentController.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        CommentController.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostCreated_When_ValidDataProvided() {

        ConnectionController.connect(globalRestApiUser, newUser);

        CommentModel comment = CommentController.createComment(newUser, privatePost);
        assert comment != null;
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        CommentController.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

        ConnectionController.disconnect(globalRestApiUser, newUser);
    }

    @Test
    public void commentOfPrivatePostNotCreated_When_UsersNotConnected() {

        CommentModel comment = CommentController.createComment(newUser, privatePost);

        assertNull(comment, "Comment was made.");

    }

    @Test
    public void allCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = CommentController.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = CommentController.findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
                CommentController.deleteComment(newUser, comment.getCommentId());
                assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

    }

    @Test
    public void commentOfPublicPostEdited_By_Author() {

        CommentModel comment = CommentController.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentController.editComment(newUser, comment);
        CommentController.assertEditedComment(newUser, publicPost, comment.getCommentId(), contentToBeEdited);

        CommentController.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostEdited_By_AdminUser() {

        CommentModel comment = CommentController.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentController.editComment(globalRestApiAdminUser, comment);
        CommentController.assertEditedComment(globalRestApiAdminUser, publicPost, comment.getCommentId(), contentToBeEdited);

        CommentController.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostEdited_By_AdminUser() {

        CommentModel comment = CommentController.createComment(globalRestApiUser, privatePost);
        assert comment != null;
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentController.editComment(globalRestApiAdminUser, comment);
        CommentController.assertEditedComment(globalRestApiUser, privatePost, comment.getCommentId(), contentToBeEdited);

        CommentController.deleteComment(globalRestApiUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostLiked_By_User() {

        UserModel userToLikeComment = new UserModel();
        UserController.register(userToLikeComment, ROLE_USER.toString());

        CommentModel commentToBeLiked = CommentController.createComment(newUser, publicPost);
        assert commentToBeLiked != null;
        assertTrue(CommentController.commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        CommentController.likeComment(userToLikeComment, commentToBeLiked);

        assertEquals(CommentController.getCommentById(userToLikeComment, commentToBeLiked.getCommentId()).getLikes().size(),
                likedCommentLikesToHave + 1, "Comment was not liked.");

        CommentController.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(CommentController.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        UserController.disableUser(globalRestApiAdminUser, userToLikeComment);
    }

    @Test
    public void commentOfPublicPostDeleted_By_Author() {

        CommentModel commentToBeDeleted = CommentController.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentController.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentController.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(CommentController.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostDeleted_By_AdminUser() {

        CommentModel commentToBeDeleted = CommentController.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentController.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentController.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(CommentController.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostDeleted_By_AdminUser() {

        ConnectionController.connect(globalRestApiUser, newUser);

        CommentModel commentToBeDeleted = CommentController.createComment(newUser, privatePost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentController.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentController.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(CommentController.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        ConnectionController.disconnect(globalRestApiUser, newUser);

    }

    @Test
    public void allCommentsOfPostListed_When_Required_By_User() {

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = CommentController.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
        }

        CommentModel[] postComments = PostController.findAllCommentsOfAPost(globalRestApiUser, publicPost);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            CommentController.deleteComment(newUser, comment.getCommentId());
            assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment not deleted.");
        }

    }

    @Test
    public void commentOfPostFoundById_When_Requested_By_User() {

        CommentModel comment = CommentController.createComment(newUser, publicPost);
        assert comment != null;
        int commentId = comment.getCommentId();
        assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");

        CommentModel foundComment = CommentController.getCommentById(globalRestApiUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        CommentController.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentController.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

}
