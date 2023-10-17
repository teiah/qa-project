package test.cases.wearerestassured.tests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import api.models.UserModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_USER;
import static org.testng.Assert.*;

public class RESTCommentControllerTest extends BaseWeareRestAssuredTest {

    UserModel newUser = new UserModel();

    @BeforeClass
    public void setUpCommentTest() {
        newUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @Test
    public void userCanCreateCommentOfAPublicPostWithValidData() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }


    @Test
    public void userCanCreateCommentOfAPrivatePostWithValidDataIfConnected() {

        WEareApi.connectUsers(globalUser, newUser);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNotNull(comment, "Comment was not made.");
        assertEquals(comment.getPost().getPostId(), post.getPostId(), "Comment is not made for the required post.");
        assertEquals(comment.getUser().getId(), newUser.getId(), "Comment is not made by the required user.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCannotCreateCommentOfAPrivatePostWithValid_Data() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);

        assertNull(comment, "Comment was made.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllComments() {

        CommentModel[] comments = WEareApi.findAllComments();

        for (CommentModel comment : comments) {
            assertNotNull(comment.getCommentId(), "There are no comments found");
        }
    }

    @Test
    public void userCanEditCommentOfAPostWithValid_Data() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(globalUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalUser, comment);

        WEareApi.assertEditedComment(globalUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(globalUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPublicPostWithValidData() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(newUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalAdminUser, comment);

        WEareApi.assertEditedComment(globalUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanEditCommentOfAPrivatePostWithValid_Data() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);
        CommentModel comment = WEareApi.createComment(globalUser, post);

        String contentToBeEdited = comment.getContent();

        WEareApi.editComment(globalAdminUser, comment);

        WEareApi.assertEditedComment(globalUser, post.getPostId(), comment.getCommentId(), contentToBeEdited);

        WEareApi.deleteComment(globalAdminUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanLikeCommentOfAPublicPost() {

        UserModel userToLikeComment = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel commentToBeLiked = WEareApi.createComment(newUser, post);
        CommentModel likedComment = WEareApi.likeComment(userToLikeComment, commentToBeLiked.getCommentId());

        int likedCommentLikesToHave = commentToBeLiked.getLikes().size() + 1;
        assertEquals(likedComment.getLikes().size(), likedCommentLikesToHave, "Comment was not liked.");
        assertEquals(commentToBeLiked.getCommentId(), likedComment.getCommentId(), "Liked comment is different.");

        WEareApi.deleteComment(newUser, commentToBeLiked.getCommentId());
        assertFalse(WEareApi.commentExists(commentToBeLiked.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanDeleteCommentOfAPublicPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(newUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPublicPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);
        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(globalAdminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeleteCommentOfAPrivatePost() {

        WEareApi.connectUsers(globalUser, newUser);

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel commentToBeDeleted = WEareApi.createComment(newUser, post);

        int commentToBeDeletedId = commentToBeDeleted.getCommentId();

        WEareApi.deleteComment(globalAdminUser, commentToBeDeletedId);
        assertFalse(WEareApi.commentExists(commentToBeDeletedId), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllCommentsOfAPost() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            WEareApi.createComment(newUser, post);
        }

        CommentModel[] postComments = WEareApi.findAllCommentsOfAPost(globalUser, post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertNotNull(comment, "Comment is null");
        }

        for (CommentModel comment : postComments) {
            WEareApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");
        }

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanFindACommentById() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(globalUser, publicVisibility);

        CommentModel comment = WEareApi.createComment(newUser, post);
        int commentId = comment.getCommentId();

        CommentModel foundComment = WEareApi.getCommentById(globalUser, commentId);
        assertEquals(foundComment.getCommentId(), commentId, "Comments do not match.");

        WEareApi.deleteComment(newUser, comment.getCommentId());
        assertFalse(WEareApi.commentExists(comment.getCommentId()), "Comment was not deleted.");

        WEareApi.deletePost(globalUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

}
