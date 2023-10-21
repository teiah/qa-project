package test.cases.wearerestassuredtests.tests;

import models.models.CommentModel;
import models.models.PostModel;
import models.models.UserModel;
import org.testng.annotations.*;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static models.basemodel.BaseModel.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void PublicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PublicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRestApiUser.editPost(postToBeEdited);
        globalRestApiUser.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        globalRestApiUser.deletePost(postToBeEdited.getPostId());
        assertFalse(publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRestApiUser.editPost(postToBeEdited);
        globalRestApiUser.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        globalRestApiUser.deletePost(postToBeEdited.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PublicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRestApiAdminUser.editPost(postToBeEdited);
        globalRestApiAdminUser.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        globalRestApiUser.deletePost(postToBeEdited.getPostId());
        assertFalse(publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = globalRestApiUser.createPost(publicVisibility);
        assertTrue(privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRestApiAdminUser.editPost(postToBeEdited);
        globalRestApiAdminUser.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        globalRestApiUser.deletePost(postToBeEdited.getPostId());
        assertFalse(privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = globalRestApiUser.createPost(publicVisibility);
            assertTrue(publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = globalRestApiUser.createPost(publicVisibility);
            assertTrue(privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                globalRestApiUser.deletePost(foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void PostLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeLiked.getPostId()), "Post not created.");


        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;

        globalRestApiUser.likePost(postToBeLiked);

        assertEquals(postToBeLiked.getLikes().size(), likedPostLikesToHave, "Post was not liked.");

        globalRestApiUser.deletePost(postToBeLiked.getPostId());
        assertFalse(publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = globalRestApiUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(privatePostExists(globalRestApiUser, postId), "Post not created.");

        globalRestApiUser.deletePost(postId);
        assertFalse(privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = globalRestApiUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(privatePostExists(globalRestApiUser, postId), "Post not created.");

        globalRestApiAdminUser.deletePost(postId);
        assertFalse(privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void PublicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = globalRestApiUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(publicPostExists(postId), "Post not created.");

        globalRestApiAdminUser.deletePost(postId);
        assertFalse(publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void PostCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = new UserModel();
        newUser.register(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = globalRestApiUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = newUser.createComment(post);
            assertTrue(commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = post.findCommentsOfAPost();

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertNotNull(comment, "Comment is null");
                newUser.deleteComment(comment.getCommentId());
                assertFalse(commentExists(comment.getCommentId()), "Post was not deleted.");
            }
        }

        globalRestApiUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

        globalRestApiAdminUser.disableUser(newUser.getId());
    }

}
