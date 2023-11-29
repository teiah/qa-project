package test.cases.restassured.tests;

import api.models.EditPostRequest;
import api.models.PostRequest;
import api.models.PostResponse;

import api.models.UserRequest;
import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.models.Post;
import com.telerikacademy.testframework.utils.Authority;
import com.telerikacademy.testframework.utils.Helpers;
import factories.PostFactory;
import factories.UserFactory;
import com.telerikacademy.testframework.utils.Visibility;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.*;
import api.controllers.PostController;
import api.controllers.UserController;

import test.cases.restassured.base.BaseWeareRestAssuredTest;

import java.util.Arrays;

import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {
    private String authCookie;
    private User user;
    private Post post;
    private int postId;
    private PostRequest postRequest;
    private PostResponse postResponse;


    @BeforeClass
    public void userSetup() {
        user = UserFactory.createUser();
        UserRequest userRequest = new UserRequest(Authority.ROLE_USER.toString(), user);
        UserController.registerUser(userRequest);
        authCookie = getCookieValue(user);
    }

    @BeforeMethod
    public void postSetup() {
        post = PostFactory.createPost(user, Visibility.PUBLIC);
        postRequest = new PostRequest(post);
        postResponse = PostController.createPost(postRequest, authCookie);
        postId = postResponse.getPostId();
    }

    @Test
    public void createPost() {
        assertEquals(post.getContent(), postResponse.getContent(),
                "The post's content doesn't match the expected content.");
    }

    //
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

        PostController.editPost(postId, postEditRequest, authCookie);

        PostResponse[] posts = PostController.getAllPosts(authCookie);
        Assertions.assertTrue(Arrays.stream(posts)
                        .anyMatch(x -> x.getPostId() == postId && x.getContent().equals(postEditRequest.getContent())),
                "Get All Request's body doesn't contain the new content of the post.");
    }

    //
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
//    @Test
//    public void allPostsListed_When_Requested_By_User() {
//
//        List<Integer> postIds = new ArrayList<>();
//
//        boolean publicVisibility = true;
//
//        int postCount = 5;
//
//        for (int i = 0; i < postCount; i++) {
//            Post post = PostController.createPost(globalRestApiUser, publicVisibility);
//            assertTrue(PostController.publicPostExists(post.getPostId()), "Post not created.");
//            postIds.add(post.getPostId());
//        }
//        publicVisibility = false;
//        for (int i = 0; i < postCount; i++) {
//            Post post = PostController.createPost(globalRestApiUser, publicVisibility);
//            assertTrue(PostController.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");
//            postIds.add(post.getPostId());
//        }
//
//        Post[] foundPosts = PostController.findAllPosts();
//
//        for (Post foundPost : foundPosts) {
//            if (postIds.contains(foundPost.getPostId())) {
//                assertNotNull(foundPost, "Post is null");
//                PostController.deletePost(globalRestApiUser, foundPost.getPostId());
//                postIds.remove(foundPost.getPostId());
//                assertFalse(PostController.publicPostExists(foundPost.getPostId()), "Post was not deleted.");
//            }
//        }
//
//        assertEquals(postIds.size(), 0, "Some posts were not received");
//
//    }
//
    @Test
    public void likePost() {
        PostResponse likedPost = PostController.likePost(postResponse.getPostId(), authCookie);

        assertTrue(Arrays.stream(likedPost.getLikes())
                        .anyMatch(x -> x.getUsername().equals(user.getUsername())),
                "The response body doesn't contain the user with whom the post was liked.");
    }
//
//    @Test
//    public void privatePostDeleted_By_Author() {
//
//        boolean publicVisibility = false;
//
//        Post postToBeDeleted = PostController.createPost(globalRestApiUser, publicVisibility);
//        int postId = postToBeDeleted.getPostId();
//        assertTrue(PostController.privatePostExists(globalRestApiUser, postId), "Post not created.");
//
//        PostController.deletePost(globalRestApiUser, postId);
//        assertFalse(PostController.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");
//
//    }
//
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
//    @Test
//    public void postCommentsListed_When_Requested_By_User() {
//
//        List<Integer> commentIds = new ArrayList<>();
//
//        User newUser = new User();
//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_USER.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        UserController.register(newUser, username, password, email, authority);
//
//        boolean publicVisibility = true;
//
//        Post post = PostController.createPost(globalRestApiUser, publicVisibility);
//        assertTrue(PostController.publicPostExists(post.getPostId()), "Post not created.");
//
//        int commentCount = 3;
//
//        for (int i = 0; i < commentCount; i++) {
//            Comment comment = CommentController.createComment(newUser, post);
//            assertTrue(CommentController.commentExists(comment.getCommentId()), "Comment not created.");
//            commentIds.add(comment.getCommentId());
//        }
//
//        Comment[] postComments = PostController.findCommentsOfAPost(post);
//
//        assertEquals(postComments.length, commentCount, "Wrong post comments count");
//
//        for (Comment comment : postComments) {
//            if (commentIds.contains(comment.getCommentId())) {
//                assertNotNull(comment, "Comment is null");
//                CommentController.deleteComment(newUser, comment.getCommentId());
//                assertFalse(CommentController.commentExists(comment.getCommentId()), "Post was not deleted.");
//            }
//        }
//
//        PostController.deletePost(globalRestApiUser, post.getPostId());
//        assertFalse(PostController.publicPostExists(post.getPostId()), "Post was not deleted.");
//
//        UserController.disableUser(globalRestApiAdminUser, newUser);
//    }
//
}
