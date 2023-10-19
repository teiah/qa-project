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
        postUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpPostTest() {
        WEareApi.disableUser(globalRESTAdminUser, postUser.getId());
    }

    @Test
    public void PublicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        WEareApi.deletePost(postUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(postUser, post.getPostId()), "Post not created.");

        WEareApi.deletePost(postUser, post.getPostId());
        assertFalse(WEareApi.privatePostExists(postUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PublicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited);
        WEareApi.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited);
        WEareApi.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PublicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(globalRESTAdminUser, postToBeEdited);
        WEareApi.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(globalRESTAdminUser, postToBeEdited);
        WEareApi.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = WEareApi.createPost(postUser, publicVisibility);
            assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = WEareApi.createPost(postUser, publicVisibility);
            assertTrue(WEareApi.privatePostExists(postUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = WEareApi.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                WEareApi.deletePost(postUser, foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(WEareApi.publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void PostLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(postToBeLiked.getPostId()), "Post not created.");

        PostModel likedPost = WEareApi.likePost(postUser, postToBeLiked.getPostId());
        assertTrue(WEareApi.publicPostExists(likedPost.getPostId()), "Post not created.");

        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;
        assertEquals(likedPost.getLikes().size(), likedPostLikesToHave, "Post was not liked.");
        assertEquals(postToBeLiked.getPostId(), likedPost.getPostId(), "Liked post is different.");

        WEareApi.deletePost(postUser, postToBeLiked.getPostId());
        assertFalse(WEareApi.publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(WEareApi.privatePostExists(postUser, postId), "Post not created.");

        WEareApi.deletePost(postUser, postId);
        assertFalse(WEareApi.privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(WEareApi.privatePostExists(postUser, postId), "Post not created.");

        WEareApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(WEareApi.privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PublicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(WEareApi.publicPostExists(postId), "Post not created.");

        WEareApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(WEareApi.publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void PostCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = WEareApi.createPost(postUser, publicVisibility);
        assertTrue(WEareApi.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = WEareApi.createComment(newUser, post);
            assertTrue(WEareApi.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = WEareApi.findCommentsOfAPost(post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertNotNull(comment, "Comment is null");
            WEareApi.deleteComment(newUser, comment.getCommentId());
            assertFalse(WEareApi.commentExists(comment.getCommentId()), "Post was not deleted.");
        }

        WEareApi.deletePost(postUser, post.getPostId());
        assertFalse(WEareApi.publicPostExists(post.getPostId()), "Post was not deleted.");

    }

}
