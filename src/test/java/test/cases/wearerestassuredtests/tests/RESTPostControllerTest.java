package test.cases.wearerestassuredtests.tests;

import models.wearemodels.CommentModel;
import models.wearemodels.PostModel;
import models.wearemodels.UserModel;
import org.testng.annotations.*;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static models.BaseModel.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {

    UserModel postUser = new UserModel();

    @BeforeClass
    public void setUpPostTest() {
        postUser.register(ROLE_USER.toString());
    }

    @AfterClass
    public void cleanUpPostTest() {
        globalRESTAdminUser.disableUser(postUser.getId());
    }

    @Test
    public void PublicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = postUser.createPost(publicVisibility);
        assertTrue(publicPostExists(post.getPostId()), "Post not created.");

        postUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = postUser.createPost(publicVisibility);
        assertTrue(privatePostExists(postUser, post.getPostId()), "Post not created.");

        postUser.deletePost(post.getPostId());
        assertFalse(privatePostExists(postUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PublicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = postUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        postUser.editPost(postToBeEdited);
        postUser.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        postUser.deletePost(postToBeEdited.getPostId());
        assertFalse(publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = postUser.createPost(publicVisibility);
        assertTrue(privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        postUser.editPost(postToBeEdited);
        postUser.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        postUser.deletePost(postToBeEdited.getPostId());
        assertFalse(privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PublicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = postUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRESTAdminUser.editPost(postToBeEdited);
        globalRESTAdminUser.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        postUser.deletePost(postToBeEdited.getPostId());
        assertFalse(publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void PrivatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = postUser.createPost(publicVisibility);
        assertTrue(privatePostExists(postUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        globalRESTAdminUser.editPost(postToBeEdited);
        globalRESTAdminUser.assertEditedPrivatePost(postUser, postToBeEdited.getPostId(), postToBeEditedContent);

        postUser.deletePost(postToBeEdited.getPostId());
        assertFalse(privatePostExists(postUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void AllPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = postUser.createPost(publicVisibility);
            assertTrue(publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = postUser.createPost(publicVisibility);
            assertTrue(privatePostExists(postUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                postUser.deletePost(foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void PostLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = postUser.createPost(publicVisibility);
        assertTrue(publicPostExists(postToBeLiked.getPostId()), "Post not created.");


        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;

        postUser.likePost(postToBeLiked);

        assertEquals(postToBeLiked.getLikes().size(), likedPostLikesToHave, "Post was not liked.");

        postUser.deletePost(postToBeLiked.getPostId());
        assertFalse(publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = postUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(privatePostExists(postUser, postId), "Post not created.");

        postUser.deletePost(postId);
        assertFalse(privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PrivatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = postUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(privatePostExists(postUser, postId), "Post not created.");

        globalRESTAdminUser.deletePost(postId);
        assertFalse(privatePostExists(postUser, postId), "Post was not deleted.");

    }

    @Test
    public void PublicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = postUser.createPost(publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(publicPostExists(postId), "Post not created.");

        globalRESTAdminUser.deletePost(postId);
        assertFalse(publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void PostCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = new UserModel();
        newUser.register(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = postUser.createPost(publicVisibility);
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

        postUser.deletePost(post.getPostId());
        assertFalse(publicPostExists(post.getPostId()), "Post was not deleted.");

    }

}
