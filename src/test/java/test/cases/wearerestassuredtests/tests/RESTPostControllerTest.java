package test.cases.wearerestassuredtests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import api.models.UserModel;
import org.testng.annotations.*;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {

    UserModel postUser;

    @BeforeClass
    public void setUpPostTest() {
        postUser = weAreApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpPostTest() {
        weAreApi.disableUser(globalRESTAdminUser, postUser.getId());
    }

    @Test
    public void PublicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        weAreApi.deletePost(postUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.privatePostExists(postUser, post.getPostId()), "Post not created.");

        weAreApi.deletePost(postUser, post.getPostId());
        assertFalse(weAreApi.privatePostExists(postUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PublicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        weAreApi.editPost(postUser, postToBeEdited);
        weAreApi.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weAreApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(weAreApi.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        weAreApi.editPost(postUser, postToBeEdited);
        weAreApi.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        weAreApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(weAreApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PublicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        weAreApi.editPost(globalRESTAdminUser, postToBeEdited);
        weAreApi.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weAreApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(weAreApi.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        weAreApi.editPost(globalRESTAdminUser, postToBeEdited);
        weAreApi.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        weAreApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(weAreApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = weAreApi.createPost(postUser, publicVisibility);
            assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = weAreApi.createPost(postUser, publicVisibility);
            assertTrue(weAreApi.privatePostExists(postUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = weAreApi.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                weAreApi.deletePost(postUser, foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(weAreApi.publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void PostLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.publicPostExists(postToBeLiked.getPostId()), "Post not created.");

        PostModel likedPost = weAreApi.likePost(postUser, postToBeLiked.getPostId());
        assertTrue(weAreApi.publicPostExists(likedPost.getPostId()), "Post not created.");

        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;
        assertEquals(likedPost.getLikes().size(), likedPostLikesToHave, "Post was not liked.");
        assertEquals(postToBeLiked.getPostId(), likedPost.getPostId(), "Liked post is different.");

        weAreApi.deletePost(postUser, postToBeLiked.getPostId());
        assertFalse(weAreApi.publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = weAreApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(weAreApi.privatePostExists(postUser, postId), "Post not created.");

        weAreApi.deletePost(postUser, postId);
        assertFalse(weAreApi.privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = weAreApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(weAreApi.privatePostExists(postUser, postId), "Post not created.");

        weAreApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(weAreApi.privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PublicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = weAreApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(weAreApi.publicPostExists(postId), "Post not created.");

        weAreApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(weAreApi.publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void PostCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = weAreApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = weAreApi.createPost(postUser, publicVisibility);
        assertTrue(weAreApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = weAreApi.createComment(newUser, post);
            assertTrue(weAreApi.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = weAreApi.findCommentsOfAPost(post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertNotNull(comment, "Comment is null");
            weAreApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(weAreApi.commentExists(comment.getCommentId()), "Post was not deleted.");
        }

        weAreApi.deletePost(postUser, post.getPostId());
        assertFalse(weAreApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

}
