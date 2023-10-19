package test.cases.wearerestassuredtests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import api.models.UserModel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseWeareRestAssuredTest {

    UserModel commentUser;
    UserModel newUser;

    @BeforeClass
    public void setUpCommentTest() {
        commentUser = weAreApi.registerUser(ROLE_USER.toString());
        newUser = weAreApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        weAreApi.disableUser(globalRESTAdminUser, commentUser.getId());
        weAreApi.disableUser(globalRESTAdminUser, newUser.getId());
    }

    @Test
    public void CommentOfPublicPostCreated_When_ValidDataProvided() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(newUser, post);
        assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");

        weAreApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostCreated_When_ValidDataProvided() {

        UserModel postCreator = weAreApi.registerUser(ROLE_USER.toString());
        UserModel commentCreator = weAreApi.registerUser(ROLE_USER.toString());

        weAreApi.connectUsers(postCreator, commentCreator);

        PostModel post = weAreApi.createPost(postCreator, false);
        assertTrue(weAreApi.privatePostExists(postCreator, post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(commentCreator, post);
        assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");

        weAreApi.deleteComment(commentCreator, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(postCreator, post.getPostId());
        assertFalse(weAreApi.privatePostExists(postCreator, post.getPostId()), "Post was not deleted.");

        weAreApi.disableUser(globalRESTAdminUser, postCreator.getId());
        weAreApi.disableUser(globalRESTAdminUser, commentCreator.getId());
    }

    @Test
    public void CommentOfPrivatePostNotCreated_When_UsersNotConnected() {

        UserModel user = weAreApi.registerUser(ROLE_USER.toString());

        PostModel post = weAreApi.createPost(commentUser, false);
        assertTrue(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(user, post);

        assertNull(comment, "Comment was made.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");

        weAreApi.disableUser(globalRESTAdminUser, user.getId());
    }

    @Test
    public void AllCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = weAreApi.createComment(newUser, post);
            assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = weAreApi.findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");
                weAreApi.deleteComment(newUser, comment.getCommentId());
                assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void CommentOfPublicPostEdited_By_Author() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(newUser, post);
        assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        weAreApi.editComment(newUser, comment);
        weAreApi.assertEditedComment(newUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        weAreApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPublicPostEdited_By_AdminUser() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(newUser, post);
        assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        weAreApi.editComment(globalRESTAdminUser, comment);
        weAreApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        weAreApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostEdited_By_AdminUser() {

        PostModel post = weAreApi.createPost(commentUser, false);
        assertTrue(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(commentUser, post);
        assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        weAreApi.editComment(globalRESTAdminUser, comment);
        weAreApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        weAreApi.deleteComment(globalRESTAdminUser, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void CommentOfPublicPostLiked_By_User() {

        UserModel userToLikeComment = weAreApi.registerUser(ROLE_USER.toString());

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeLiked = weAreApi.createComment(newUser, post);
        assertTrue(weAreApi.commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        CommentModel likedComment = weAreApi.likeComment(userToLikeComment, commentToBeLiked.getCommentId());
        assertTrue(weAreApi.commentExists(likedComment.getCommentId()), "Comment not created.");

        assertEquals(likedComment.getLikes().size(), likedCommentLikesToHave + 1, "Comment was not liked.");
        assertEquals(commentToBeLiked.getCommentId(), likedComment.getCommentId(), "Liked comment is different.");

        weAreApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(weAreApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void CommentOfPublicPostDeleted_By_Author() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = weAreApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(weAreApi.commentExists(commentToBeDeletedId), "Comment not created.");

        weAreApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(weAreApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPublicPostDeleted_By_AdminUser() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = weAreApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(weAreApi.commentExists(commentToBeDeletedId), "Comment not created.");

        weAreApi.deleteComment(globalRESTAdminUser, commentToBeDeletedId);
        assertFalse(weAreApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostDeleted_By_AdminUser() {

        weAreApi.connectUsers(commentUser, newUser);

        PostModel post = weAreApi.createPost(commentUser, false);
        assertTrue(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = weAreApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(weAreApi.commentExists(commentToBeDeletedId), "Comment not created.");

        weAreApi.deleteComment(globalRESTAdminUser, commentToBeDeletedId);
        assertFalse(weAreApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllCommentsOfPostListed_When_Required_By_User() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            weAreApi.createComment(newUser, post);
        }

        CommentModel[] postComments = weAreApi.findAllCommentsOfAPost(commentUser, post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            weAreApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment not deleted.");
        }

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPostFoundById_When_Requested_By_User() {

        PostModel post = weAreApi.createPost(commentUser, true);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = weAreApi.createComment(newUser, post);
        int commentId = comment.getCommentId();
        assertTrue(weAreApi.commentExists(commentId), "Comment not created.");

        CommentModel foundComment = weAreApi.getCommentById(commentUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        weAreApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(weAreApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        weAreApi.deletePost(commentUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

}
