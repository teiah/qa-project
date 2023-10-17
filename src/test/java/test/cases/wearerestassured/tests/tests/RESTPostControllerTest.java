package test.cases.wearerestassured.tests.tests;

import api.models.CommentModel;
import api.models.PostModel;
import api.models.UserModel;
import org.testng.annotations.*;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

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
    public void tearDownPostTest() {
        WEareApi.disableUser(globalRESTAdminUser, postUser.getId());
    }

    @Test
    public void userCanCreatePublicPostWithValidData() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);

        WEareApi.assertPostCreation(post, publicVisibility);

        WEareApi.deletePost(postUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanCreatePrivatePostWithValidData() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);

        WEareApi.assertPostCreation(post, publicVisibility);

        WEareApi.deletePost(postUser, post.getPostId());
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanEditOwnPublicPostWithValidData() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.postExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanEditOwnPrivatePostWithValidData() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.postExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void adminUserCanEditPublicPostOfAnotherUserWithValidData() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(globalRESTAdminUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.postExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void adminUserCanEditPrivatePostOfAnotherUserWithValidData() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(globalRESTAdminUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
        assertFalse(WEareApi.postExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void userCanFindAllPosts() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = WEareApi.createPost(postUser, publicVisibility);
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = WEareApi.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                WEareApi.deletePost(postUser, foundPost.getPostId());
                assertFalse(WEareApi.postExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

    }

    @Test
    public void userCanLikePost() {

        boolean publicVisibility = false;
        PostModel postToBeLiked = WEareApi.createPost(postUser, publicVisibility);

        PostModel likedPost = WEareApi.likePost(postUser, postToBeLiked.getPostId());

        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;
        assertEquals(likedPost.getLikes().size(), likedPostLikesToHave, "Post was not liked.");
        assertEquals(postToBeLiked.getPostId(), likedPost.getPostId(), "Liked post is different.");

        WEareApi.deletePost(postUser, postToBeLiked.getPostId());
        assertFalse(WEareApi.postExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void userCanDeletePost() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        WEareApi.deletePost(postUser, postId);
        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeletePrivatePostOfAnotherUser() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        int postCount = WEareApi.findAllPosts().length;
        WEareApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void adminUserCanDeletePublicPostOfAnotherUser() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        WEareApi.deletePost(globalRESTAdminUser, postId);
        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void userCanFindAllCommentsOfAPost() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = WEareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = WEareApi.createPost(postUser, publicVisibility);

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
           CommentModel comment = WEareApi.createComment(newUser, post);
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
        assertFalse(WEareApi.postExists(post.getPostId()), "Post was not deleted.");

    }

}
