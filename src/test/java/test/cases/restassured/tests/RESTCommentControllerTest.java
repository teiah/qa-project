package test.cases.restassured.tests;

import restassuredapi.models.models.CommentModel;
import restassuredapi.models.models.PostModel;
import restassuredapi.models.models.UserModel;
import org.testng.annotations.*;
import restassuredapi.Comment;
import restassuredapi.Post;
import restassuredapi.Request;
import restassuredapi.User;
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
        User.register(newUser, ROLE_USER.toString());
        publicPost = Post.createPost(globalRestApiUser, true);
        assertTrue(Post.publicPostExists(publicPost.getPostId()), "Post not created.");
        privatePost = Post.createPost(globalRestApiUser, false);
        assertTrue(Post.privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
    }

    @AfterClass
    public void cleanUpCommentTest() {
        Post.deletePost(globalRestApiUser, publicPost.getPostId());
        Post.deletePost(globalRestApiUser, privatePost.getPostId());
        User.disableUser(globalRestApiAdminUser, newUser);
    }

    @Test
    public void commentOfPublicPostCreated_When_ValidDataProvided() {

        CommentModel comment = Comment.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        Comment.deleteComment(newUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostCreated_When_ValidDataProvided() {

        Request.connect(globalRestApiUser, newUser);

        CommentModel comment = Comment.createComment(newUser, privatePost);
        assert comment != null;
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        Comment.deleteComment(newUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

        Request.disconnect(globalRestApiUser, newUser);
    }

    @Test
    public void commentOfPrivatePostNotCreated_When_UsersNotConnected() {

        CommentModel comment = Comment.createComment(newUser, privatePost);

        assertNull(comment, "Comment was made.");

    }

    @Test
    public void allCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = Comment.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = Comment.findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");
                Comment.deleteComment(newUser, comment.getCommentId());
                assertFalse(Comment.commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

    }

    @Test
    public void commentOfPublicPostEdited_By_Author() {

        CommentModel comment = Comment.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        Comment.editComment(newUser, comment);
        Comment.assertEditedComment(newUser, publicPost, comment.getCommentId(), contentToBeEdited);

        Comment.deleteComment(newUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostEdited_By_AdminUser() {

        CommentModel comment = Comment.createComment(newUser, publicPost);
        assert comment != null;
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        Comment.editComment(globalRestApiAdminUser, comment);
        Comment.assertEditedComment(globalRestApiAdminUser, publicPost, comment.getCommentId(), contentToBeEdited);

        Comment.deleteComment(newUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostEdited_By_AdminUser() {

        CommentModel comment = Comment.createComment(globalRestApiUser, privatePost);
        assert comment != null;
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        Comment.editComment(globalRestApiAdminUser, comment);
        Comment.assertEditedComment(globalRestApiUser, privatePost, comment.getCommentId(), contentToBeEdited);

        Comment.deleteComment(globalRestApiUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostLiked_By_User() {

        UserModel userToLikeComment = new UserModel();
        User.register(userToLikeComment, ROLE_USER.toString());

        CommentModel commentToBeLiked = Comment.createComment(newUser, publicPost);
        assert commentToBeLiked != null;
        assertTrue(Comment.commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        Comment.likeComment(userToLikeComment, commentToBeLiked);

        assertEquals(Comment.getCommentById(userToLikeComment, commentToBeLiked.getCommentId()).getLikes().size(),
                likedCommentLikesToHave + 1, "Comment was not liked.");

        Comment.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(Comment.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        User.disableUser(globalRestApiAdminUser, userToLikeComment);
    }

    @Test
    public void commentOfPublicPostDeleted_By_Author() {

        CommentModel commentToBeDeleted = Comment.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(Comment.commentExists(commentToBeDeletedId), "Comment not created.");

        Comment.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(Comment.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPublicPostDeleted_By_AdminUser() {

        CommentModel commentToBeDeleted = Comment.createComment(newUser, publicPost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(Comment.commentExists(commentToBeDeletedId), "Comment not created.");

        Comment.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(Comment.commentExists(commentToBeDeletedId), "Comment was not deleted.");

    }

    @Test
    public void commentOfPrivatePostDeleted_By_AdminUser() {

        Request.connect(globalRestApiUser, newUser);

        CommentModel commentToBeDeleted = Comment.createComment(newUser, privatePost);
        assert commentToBeDeleted != null;
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(Comment.commentExists(commentToBeDeletedId), "Comment not created.");

        Comment.deleteComment(globalRestApiAdminUser, commentToBeDeletedId);
        assertFalse(Comment.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        Request.disconnect(globalRestApiUser, newUser);

    }

    @Test
    public void allCommentsOfPostListed_When_Required_By_User() {

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = Comment.createComment(newUser, publicPost);
            assert comment != null;
            assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");
        }

        CommentModel[] postComments = Post.findAllCommentsOfAPost(globalRestApiUser, publicPost);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            Comment.deleteComment(newUser, comment.getCommentId());
            assertFalse(Comment.commentExists(comment.getCommentId()), "Comment not deleted.");
        }

    }

    @Test
    public void commentOfPostFoundById_When_Requested_By_User() {

        CommentModel comment = Comment.createComment(newUser, publicPost);
        assert comment != null;
        int commentId = comment.getCommentId();
        assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");

        CommentModel foundComment = Comment.getCommentById(globalRestApiUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        Comment.deleteComment(newUser, comment.getCommentId());
        assertFalse(Comment.commentExists(comment.getCommentId()), "Comment was not deleted.");

    }

}
