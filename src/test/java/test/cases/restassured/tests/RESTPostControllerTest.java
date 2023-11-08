package test.cases.restassured.tests;

import api.models.models.CommentModel;
import api.models.models.PostModel;
import api.models.models.UserModel;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.*;
import api.controllers.CommentController;
import api.controllers.PostController;
import api.controllers.UserController;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void publicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = PostController.createPost(globalRestApiUser, publicVisibility);

        assertTrue(PostController.publicPostExists(post.getPostId()), "Post not created.");

        PostController.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(PostController.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        PostController.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void publicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        PostController.editPost(globalRestApiUser, postToBeEdited);
        PostController.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(PostController.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        PostController.editPost(globalRestApiUser, postToBeEdited);
        PostController.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void publicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        PostController.editPost(globalRestApiAdminUser, postToBeEdited);
        PostController.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(PostController.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        PostController.editPost(globalRestApiAdminUser, postToBeEdited);
        PostController.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void allPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = PostController.createPost(globalRestApiUser, publicVisibility);
            assertTrue(PostController.publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = PostController.createPost(globalRestApiUser, publicVisibility);
            assertTrue(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = PostController.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                PostController.deletePost(globalRestApiUser, foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(PostController.publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void postLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.publicPostExists(postToBeLiked.getPostId()), "Post not created.");


        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;

        PostController.likePost(globalRestApiUser, postToBeLiked);

        assertEquals(postToBeLiked.getLikes().size(), likedPostLikesToHave, "Post was not liked.");

        PostController.deletePost(globalRestApiUser, postToBeLiked.getPostId());
        assertFalse(PostController.publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void privatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(PostController.privatePostExists(globalRestApiUser, postId), "Post not created.");

        PostController.deletePost(globalRestApiUser, postId);
        assertFalse(PostController.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void privatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(PostController.privatePostExists(globalRestApiUser, postId), "Post not created.");

        PostController.deletePost(globalRestApiAdminUser, postId);
        assertFalse(PostController.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void publicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(PostController.publicPostExists(postId), "Post not created.");

        PostController.deletePost(globalRestApiAdminUser, postId);
        assertFalse(PostController.publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void postCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = new UserModel();
        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_USER.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserController.register(newUser, username, password, email, authority);

        boolean publicVisibility = true;

        PostModel post = PostController.createPost(globalRestApiUser, publicVisibility);
        assertTrue(PostController.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = CommentController.createComment(newUser, post);
            assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = PostController.findCommentsOfAPost(post);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertNotNull(comment, "Comment is null");
                CommentController.deleteComment(newUser, comment.getCommentId());
                assertFalse(CommentController.commentExists(comment.getCommentId()), "Post was not deleted.");
            }
        }

        PostController.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(PostController.publicPostExists(post.getPostId()), "Post was not deleted.");

        UserController.disableUser(globalRestApiAdminUser, newUser);
    }

}
