package test.cases.restassured.tests;

import api.models.*;

import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.models.Post;
import com.telerikacademy.testframework.utils.Authority;
import com.telerikacademy.testframework.utils.Helpers;
import factories.PostFactory;
import factories.UserFactory;
import com.telerikacademy.testframework.utils.Visibility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.*;
import api.controllers.PostController;
import api.controllers.UserController;

import test.cases.restassured.base.BaseWeareRestAssuredTest;

import java.util.Arrays;
import java.util.Objects;

import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {
    private String cookieValue;
    private User user;
    private UserResponse registeredUser;
    private Post post;
    private int postId;
    private PostRequest postRequest;
    private PostResponse postResponse;

    private boolean deleted = false;


    @BeforeClass
    public void userSetup() {
        user = UserFactory.createUser();
        UserRequest userRequest = new UserRequest(Authority.ROLE_USER.toString(), user);
        registeredUser = UserController.registerUser(userRequest);
        cookieValue = getCookieValue(user);
    }

    @BeforeMethod
    public void postSetup() {
        post = PostFactory.createPost(user, Visibility.PUBLIC);
        postRequest = new PostRequest(post);
        postResponse = PostController.createPost(postRequest, cookieValue);
        postId = postResponse.getPostId();
    }

    @Test
    public void createPost() {
        assertEquals(post.getContent(), postResponse.getContent(),
                "The post's content doesn't match the expected content.");
    }


    //    @Test
//    public void privatePostCreated_When_ValidDataProvided() {
//
//        boolean publicVisibility = false;
//        Post post = PostController.createPost(globalRestApiUser, publicVisibility);
//        assertTrue(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");
//
//        PostController.deletePost(globalRestApiUser, post.getPostId());
//        assertFalse(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
//    }
//
    @Test
    public void editPublicPost() {
        EditPostRequest postEditRequest = new EditPostRequest(Helpers.generatePostContent());

        PostController.editPost(postId, postEditRequest, cookieValue);

        PostResponse[] posts = PostController.getAllPosts(cookieValue);
        Assertions.assertTrue(Arrays.stream(posts)
                        .anyMatch(x -> x.getPostId() == postId && x.getContent().equals(postEditRequest.getContent())),
                "Get All Request's body doesn't contain the new content of the post.");
    }


    //    @Test
//    public void privatePostEdited_By_Author() {
//
//        boolean publicVisibility = false;
//
//        Post postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
//        assertTrue(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");
//
//        String postToBeEditedContent = postToBeEdited.getContent();
//
//        PostController.editPost(globalRestApiUser, postToBeEdited);
//        PostController.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);
//
//        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
//        assertFalse(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");
//
//    }
//
//    @Test
//    public void publicPostEdited_By_AdminUser_When_NotAuthor() {
//
//        boolean publicVisibility = true;
//
//        Post postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
//        assertTrue(PostController.publicPostExists(postToBeEdited.getPostId()), "Post not created.");
//
//        String postToBeEditedContent = postToBeEdited.getContent();
//
//        PostController.editPost(globalRestApiAdminUser, postToBeEdited);
//        PostController.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);
//
//        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
//        assertFalse(PostController.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
//    }
//
//    @Test
//    public void privatePostEdited_By_AdminUser_When_NotAuthor() {
//
//        boolean publicVisibility = false;
//
//        Post postToBeEdited = PostController.createPost(globalRestApiUser, publicVisibility);
//        assertTrue(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");
//
//        String postToBeEditedContent = postToBeEdited.getContent();
//
//        PostController.editPost(globalRestApiAdminUser, postToBeEdited);
//        PostController.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);
//
//        PostController.deletePost(globalRestApiUser, postToBeEdited.getPostId());
//        assertFalse(PostController.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");
//
//    }
//
    @Test
    public void getProfilePosts() {
        AllUsersRequest profilePostsRequest = new AllUsersRequest(0, true, "", "", 10);

        PostResponse[] posts = PostController.getAllProfilePosts(profilePostsRequest, registeredUser.getId(), cookieValue);

        assertTrue(Arrays.stream(posts)
                .anyMatch(x -> x.getPostId() == postResponse.getPostId()
                        && Objects.equals(x.getContent(), postResponse.getContent())));
    }

    @Test
    public void likePost() {
        PostResponse likedPost = PostController.likePost(postResponse.getPostId(), cookieValue);

        assertTrue(Arrays.stream(likedPost.getLikes())
                        .anyMatch(x -> x.getUsername().equals(user.getUsername())),
                "The response body doesn't contain the user with whom the post was liked.");
    }

    @Test
    public void deletePost() {
        PostController.deletePost(postResponse.getPostId(), cookieValue);
        deleted = true;

        PostResponse[] posts = PostController.getAllPosts(cookieValue);
        Assertions.assertFalse(Arrays.stream(posts)
                        .anyMatch(x -> x.getPostId() == postId && x.getContent().equals(postResponse.getContent())),
                "The deletion was unsuccessful, Get All Request's body still contains the post.");
    }


    //    @Test
//    public void privatePostDeleted_By_AdminUser_When_NotAuthor() {
//
//        boolean publicVisibility = false;
//        Post postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
//        int postId = postToBeDeleted.getPostId();
//        assertTrue(PostController.privatePostExists(globalRestApiUser, postId), "Post not created.");
//
//        PostController.deletePost(globalRestApiAdminUser, postId);
//        assertFalse(PostController.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");
//
//    }
//
//    @Test
//    public void publicPostDeleted_By_AdminUser_When_NotAuthor() {
//
//        boolean publicVisibility = true;
//        Post postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
//        int postId = postToBeDeleted.getPostId();
//        assertTrue(PostController.publicPostExists(postId), "Post not created.");
//
//        PostController.deletePost(globalRestApiAdminUser, postId);
//        assertFalse(PostController.publicPostExists(postId), "Post was not deleted.");
//
//    }
//
    @Test
    public void postCommentsListed_When_Requested_By_User() {
//        TODO: fix commentController first
    }

    @AfterEach
    public void cleanup() {
        if (!deleted) PostController.deletePost(postId, cookieValue);
//        TODO: delete user too
    }

}
