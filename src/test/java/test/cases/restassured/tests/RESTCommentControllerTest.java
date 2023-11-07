package test.cases.restassured.tests;

import restassuredapi.models.models.CommentModel;
import restassuredapi.models.models.PostModel;
import restassuredapi.models.models.UserModel;
import org.testng.annotations.*;
import restassuredapi.CommentApi;
import restassuredapi.PostApi;
import restassuredapi.RequestApi;
import restassuredapi.UserApi;
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
        UserApi.register(newUser, ROLE_USER.toString());
        publicPost = PostApi.createPost(globalRestApiUser, true);
        assertTrue(PostApi.publicPostExists(publicPost.getPostId()), "Post not created.");
        privatePost = PostApi.createPost(globalRestApiUser, false);
        assertTrue(PostApi.privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
    }

    @AfterClass
    public void cleanUpCommentTest() {
        PostApi.deletePost(globalRestApiUser, publicPost.getPostId());
        PostApi.deletePost(globalRestApiUser, privatePost.getPostId());
        UserApi.disableUser(globalRestApiAdminUser, newUser);
    }

    @Test
    public void commentOfPublicPostCreated_When_ValidDataProvided() {

        CommentModel comment = CommentApi.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        CommentApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostCreated_When_ValidDataProvided() {

        RequestApi.connect(globalRestApiUser, newUser);

        CommentModel comment = CommentApi.createComment(newUser, privatePost);
        assert comment != null;
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        CommentApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        RequestApi.disconnect(globalRestApiUser, newUser);
    }

    @Test
    public void commentOfPrivatePostNotCreated_When_UsersNotConnected() {

        CommentModel comment = CommentApi.createComment(newUser, privatePost);

        assertNull(comment, "Comment was made.");

    }

    @Test
    public void allCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = CommentApi.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = CommentApi.findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");
                CommentApi.deleteComment(newUser, comment.getCommentId());
                assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

    }

    @Test
    public void commentOfPublicPostEdited_By_Author() {

        CommentModel comment = CommentApi.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentApi.editComment(newUser, comment);
        CommentApi.assertEditedComment(newUser, publicPost, comment.getCommentId(), contentToBeEdited);

        CommentApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostEdited_By_AdminUser() {

        CommentModel comment = CommentApi.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentApi.editComment(globalRestApiAdminUser, comment);
        CommentApi.assertEditedComment(globalRestApiAdminUser, publicPost, comment.getCommentId(), contentToBeEdited);

        CommentApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostEdited_By_AdminUser() {

        CommentModel comment = CommentApi.createComment(globalRestApiUser, privatePost);
        assert comment != null;
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        CommentApi.editComment(globalRestApiAdminUser, comment);
        CommentApi.assertEditedComment(globalRestApiUser, privatePost, comment.getCommentId(), contentToBeEdited);

        CommentApi.deleteComment(globalRestApiUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostLiked_By_User() {

        UserModel userToLikeComment = new UserModel();
        UserApi.register(userToLikeComment, ROLE_USER.toString());

        CommentModel commentToBeLiked = CommentApi.createComment(newUser, publicPost);
        assert commentToBeLiked != null;
        assertTrue(CommentApi.commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        CommentApi.likeComment(userToLikeComment, commentToBeLiked);

        assertEquals(CommentApi.getCommentById(userToLikeComment, commentToBeLiked.getCommentId()).getLikes().size(),
                likedCommentLikesToHave + 1, "Comment was not liked.");

        CommentApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(CommentApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        UserApi.disableUser(globalRestApiAdminUser, userToLikeComment);
    }

    @Test
    public void commentOfPublicPostDeleted_By_Author() {

        CommentModel commentToBeDeleted = CommentApi.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentApi.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(CommentApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostDeleted_By_AdminUser() {

        CommentModel commentToBeDeleted = CommentApi.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentApi.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentApi.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(CommentApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostDeleted_By_AdminUser() {

        RequestApi.connect(globalRestApiUser, newUser);

        CommentModel commentToBeDeleted = CommentApi.createComment(newUser, privatePost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(CommentApi.commentExists(commentToBeDeletedId), "Comment not created.");

        CommentApi.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(CommentApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        RequestApi.disconnect(globalRestApiUser, newUser);

    }

    @Test
    public void allCommentsOfPostListed_When_Required_By_User() {

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = CommentApi.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");
        }

        CommentModel[] postComments = PostApi.findAllCommentsOfAPost(globalRestApiUser, publicPost);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            CommentApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment not deleted.");
        }

    }

    @Test
    public void commentOfPostFoundById_When_Requested_By_User() {

        CommentModel comment = CommentApi.createComment(newUser, publicPost);
        assert comment != null;
        int commentId = comment.getCommentId();
        assertTrue(CommentApi.commentExists(comment.getCommentId()), "Comment not created.");

        CommentModel foundComment = CommentApi.getCommentById(globalRestApiUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        CommentApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(CommentApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

}
