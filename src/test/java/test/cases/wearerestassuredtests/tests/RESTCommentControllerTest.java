package test.cases.wearerestassuredtests.tests;

import models.models.CommentModel;
import models.models.PostModel;
import models.models.UserModel;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static models.basemodel.BaseModel.*;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseWeareRestAssuredTest {

    UserModel newUser = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        newUser.register(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpCommentTest() {
        globalRestApiAdminUser.disableUser(newUser.getId());
    }

    @Test
    public void CommentOfPublicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = newUser.createComment(post);
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        newUser.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostCreated_When_ValidDataProvided() {

        UserModel sender = new UserModel();
        sender.register(ROLE_USER.toString());
        UserModel receiver = new UserModel();
        receiver.register(ROLE_USER.toString());

        sender.connectTo(receiver);

        boolean publicVisibility = false;
        PostModel post = sender.createPost(publicVisibility);
        assertTrue(privatePostExists(sender, post.getPostId()), "Post not created.");

        CommentModel comment = receiver.createComment(post);
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        receiver.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        sender.deletePost(post.getPostId());
        assertFalse(privatePostExists(sender, post.getPostId()), "Post was not deleted.");

        globalRestApiAdminUser.disableUser(sender.getId());
        globalRestApiAdminUser.disableUser(receiver.getId());
    }

    @Test
    public void CommentOfPrivatePostNotCreated_When_UsersNotConnected() {

        boolean publicVisibility = false;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        CommentModel comment = newUser.createComment(post);

        assertNull(comment, "Comment was made.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        int commentsCount = 3;
        for (int i = 0; i < commentsCount; i++) {
            CommentModel comment = newUser.createComment(post);
            assertTrue(commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] comments = findAllComments();

        for (CommentModel comment : comments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertTrue(commentExists(comment.getCommentId()), "Comment not created.");
                newUser.deleteComment(comment.getCommentId());
                assertFalse(commentExists(comment.getCommentId()), "Comment not created.");
            }
        }

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void CommentOfPublicPostEdited_By_Author() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = newUser.createComment(post);
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        newUser.editComment(comment);
        newUser.assertEditedComment(post, comment.getCommentId(), contentToBeEdited);

        newUser.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPublicPostEdited_By_AdminUser() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = newUser.createComment(post);
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        globalRestApiAdminUser.editComment(comment);
        globalRestApiAdminUser.assertEditedComment(post, comment.getCommentId(), contentToBeEdited);

        newUser.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostEdited_By_AdminUser() {

        boolean publicVisibility = false;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        CommentModel comment = globalRestApiUser.createComment(post);
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        String contentToBeEdited = comment.getContent();

        globalRestApiAdminUser.editComment(comment);
        globalRestApiUser.assertEditedComment(post, comment.getCommentId(), contentToBeEdited);

        globalRestApiUser.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void CommentOfPublicPostLiked_By_User() {

        UserModel userToLikeComment = new UserModel();
        userToLikeComment.register(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeLiked = newUser.createComment(post);
        assertTrue(commentExists(commentToBeLiked.getCommentId()), "Comment not created.");

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size();

        userToLikeComment.likeComment(commentToBeLiked);

        assertEquals(userToLikeComment.getCommentById(commentToBeLiked.getCommentId()).getLikes().size(),
                likedCommentLikesToHave + 1, "Comment was not liked.");

        newUser.deleteComment(commentToBeLiked.getCommentId());
        assertFalse(commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

        globalRestApiAdminUser.disableUser(userToLikeComment.getId());
    }

    @Test
    public void CommentOfPublicPostDeleted_By_Author() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = newUser.createComment(post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(commentExists(commentToBeDeletedId), "Comment not created.");

        newUser.deleteComment(commentToBeDeletedId);
        assertFalse(commentExists(commentToBeDeletedId), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPublicPostDeleted_By_AdminUser() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = newUser.createComment(post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(commentExists(commentToBeDeletedId), "Comment not created.");

        globalRestApiAdminUser.deleteComment(commentToBeDeletedId);
        assertFalse(commentExists(commentToBeDeletedId), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPrivatePostDeleted_By_AdminUser() {

        globalRestApiUser.connectTo(newUser);

        boolean publicVisibility = false;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        CommentModel commentToBeDeleted = newUser.createComment( post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();
        assertTrue(commentExists(commentToBeDeletedId), "Comment not created.");

        globalRestApiAdminUser.deleteComment(commentToBeDeletedId);
        assertFalse(commentExists(commentToBeDeletedId), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");

        newUser.disconnectFromUser(globalRestApiUser);

    }

    @Test
    public void AllCommentsOfPostListed_When_Required_By_User() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            newUser.createComment(post);
        }

        CommentModel[] postComments = post.findAllCommentsOfAPost(globalRestApiUser);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertTrue(commentExists(comment.getCommentId()), "Comment not created.");
        }

        for (CommentModel comment : postComments) {
            newUser.deleteComment(comment.getCommentId());
            assertFalse(commentExists(comment.getCommentId()), "Comment not deleted.");
        }

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void CommentOfPostFoundById_When_Requested_By_User() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        CommentModel comment = newUser.createComment(post);
        int commentId = comment.getCommentId();
        assertTrue(commentExists(comment.getCommentId()), "Comment not created.");

        CommentModel foundComment = globalRestApiUser.getCommentById(commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        newUser.deleteComment(comment.getCommentId());
        assertFalse(commentExists(comment.getCommentId()), "Comment was not deleted.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

}
