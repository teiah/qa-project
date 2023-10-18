package test.cases.wearerestassuredtests.tests;

import api.WEareApi;
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
        commentUser = WEareApi.registerUser(ROLE_USER.toString());
        newUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        WEareApi.disableUser(globalRESTAdminUser, commentUser.getId());
        WEareApi.disableUser(globalRESTAdminUser, newUser.getId());
    }

    @Test
    public void userCanCreateCommentOfAPublicPostWithValidData() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(newUser, post);
        assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }


    @Test
    public void userCanCreateCommentOfAPrivatePostWithValidDataIfConnected() {

        UserModel sender = WEareApi.registerUser(ROLE_USER.toString());
        UserModel receiver = WEareApi.registerUser(ROLE_USER.toString());

        WEareApi.connectUsers(sender, receiver);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(sender, publicVisibility);
        assertTrue(WEareApi.privatePostExists(sender, post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(receiver, post);
        assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");

        WEareApi.deleteComment(receiver, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(sender, post.getPostId());
        assertFalse(WEareApi.privatePostExists(sender, post.getPostId()), "Post was not deleted.");

        WEareApi.disableUser(globalRESTAdminUser, sender.getId());
        WEareApi.disableUser(globalRESTAdminUser, receiver.getId());
    }

    @Test
    public void userCannotCreateCommentOfAPrivatePostWhenNotConnected() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(user, post);

        assertNull(comment, "Comment was made.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");

        WEareApi.disableUser(globalRESTAdminUser, user.getId());
    }

    @Test
    public void userCanFindAllComments() {

        List<Integer> commentIds = new ArrayList<>();

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = WEareApi.createComment(newUser, post);
            assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = WEareApi.findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");
                WEareApi.deleteComment(newUser, comment.getCommentId());
                assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanEditCommentOfAPostWithValid_Data() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(newUser, post);
        assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(newUser, comment);
        WEareApi.assertEditedComment(newUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPublicPostWithValidData() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(newUser, post);
        assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalRESTAdminUser, comment);
        WEareApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPrivatePostWithValidData() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(commentUser, post);
        assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalRESTAdminUser, comment);
        WEareApi.assertEditedComment(commentUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(globalRESTAdminUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanLikeCommentOfAPublicPost() {

        UserModel userToLikeComment = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeLiked = WEareApi.createComment(newUser, post);
        assertTrue(WEareApi.commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        CommentModel likedComment = WEareApi.likeComment(userToLikeComment, commentToBeLiked.getCommentId());
        assertTrue(WEareApi.commentExists(likedComment.getCommentId()), "Comment not created.");

        assertEquals(likedComment.getLikes().size(), likedCommentLikesToHave + 1, "Comment was not liked.");
        assertEquals(commentToBeLiked.getCommentId(), likedComment.getCommentId(), "Liked comment is different.");

        WEareApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(WEareApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanDeleteCommentOfAPublicPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(WEareApi.commentExists(commentToBeDeletedId), "Comment not created.");

        WEareApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPublicPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(WEareApi.commentExists(commentToBeDeletedId), "Comment not created.");

        WEareApi.deleteComment(globalRESTAdminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPrivatePost() {

        WEareApi.connectUsers(commentUser, newUser);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(WEareApi.commentExists(commentToBeDeletedId), "Comment not created.");

        WEareApi.deleteComment(globalRESTAdminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.privatePostExists(commentUser, post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllCommentsOfAPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            WEareApi.createComment(newUser, post);
        }

        CommentModel[] postComments = WEareApi.findAllCommentsOfAPost(commentUser, post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            WEareApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment not deleted.");
        }

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindACommentById() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(commentUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = WEareApi.createComment(newUser, post);
        int commentId = comment.getCommentId();
        assertTrue(WEareApi.commentExists(commentId), "Comment not created.");

        CommentModel foundComment = WEareApi.getCommentById(commentUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(commentUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

}
