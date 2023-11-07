package test.cases.restassured.tests;

import api.models.models.CommentModel;
import api.models.models.PostModel;
import api.models.models.UserModel;
import org.testng.annotations.*;
import api.Comment;
import api.Post;
import api.User;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void publicPostCreated_When_ValidDataProvided() {

        boolean publicVisibility = true;
        PostModel post = Post.createPost(globalRestApiUser, publicVisibility);

        assertTrue(Post.publicPostExists(post.getPostId()), "Post not created.");

        Post.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(Post.publicPostExists(post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostCreated_When_ValidDataProvided() {

        boolean publicVisibility = false;
        PostModel post = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");

        Post.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(Post.privatePostExists(globalRestApiUser, post.getPostId()), "Post was not deleted.");
    }

    @Test
    public void publicPostEdited_By_Author() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        Post.editPost(globalRestApiUser, postToBeEdited);
        Post.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        Post.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(Post.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostEdited_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        Post.editPost(globalRestApiUser, postToBeEdited);
        Post.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        Post.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(Post.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void publicPostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.publicPostExists(postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        Post.editPost(globalRestApiAdminUser, postToBeEdited);
        Post.assertEditedPublicPost(postToBeEdited.getPostId(), postToBeEditedContent);

        Post.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(Post.publicPostExists(postToBeEdited.getPostId()), "Post was not deleted.");
    }

    @Test
    public void privatePostEdited_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post not created.");

        String postToBeEditedContent = postToBeEdited.getContent();

        Post.editPost(globalRestApiAdminUser, postToBeEdited);
        Post.assertEditedPrivatePost(globalRestApiUser, postToBeEdited.getPostId(), postToBeEditedContent);

        Post.deletePost(globalRestApiUser, postToBeEdited.getPostId());
        assertFalse(Post.privatePostExists(globalRestApiUser, postToBeEdited.getPostId()), "Post was not deleted.");

    }

    @Test
    public void allPostsListed_When_Requested_By_User() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = Post.createPost(globalRestApiUser, publicVisibility);
            assertTrue(Post.publicPostExists(post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }
        publicVisibility = false;
        for (int i = 0; i < postCount; i++) {
            PostModel post = Post.createPost(globalRestApiUser, publicVisibility);
            assertTrue(Post.privatePostExists(globalRestApiUser, post.getPostId()), "Post not created.");
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = Post.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            if (postIds.contains(foundPost.getPostId())) {
                assertNotNull(foundPost, "Post is null");
                Post.deletePost(globalRestApiUser, foundPost.getPostId());
                postIds.remove(foundPost.getPostId());
                assertFalse(Post.publicPostExists(foundPost.getPostId()), "Post was not deleted.");
            }
        }

        assertEquals(postIds.size(), 0, "Some posts were not received");

    }

    @Test
    public void postLiked_By_User() {

        boolean publicVisibility = true;
        PostModel postToBeLiked = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.publicPostExists(postToBeLiked.getPostId()), "Post not created.");


        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;

        Post.likePost(globalRestApiUser, postToBeLiked);

        assertEquals(postToBeLiked.getLikes().size(), likedPostLikesToHave, "Post was not liked.");

        Post.deletePost(globalRestApiUser, postToBeLiked.getPostId());
        assertFalse(Post.publicPostExists(postToBeLiked.getPostId()), "Post was not deleted.");

    }

    @Test
    public void privatePostDeleted_By_Author() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = Post.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(Post.privatePostExists(globalRestApiUser, postId), "Post not created.");

        Post.deletePost(globalRestApiUser, postId);
        assertFalse(Post.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void privatePostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = false;
        PostModel postToBeDeleted = Post.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(Post.privatePostExists(globalRestApiUser, postId), "Post not created.");

        Post.deletePost(globalRestApiAdminUser, postId);
        assertFalse(Post.privatePostExists(globalRestApiUser, postId), "Post was not deleted.");

    }

    @Test
    public void publicPostDeleted_By_AdminUser_When_NotAuthor() {

        boolean publicVisibility = true;
        PostModel postToBeDeleted = Post.createPost(globalRestApiUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();
        assertTrue(Post.publicPostExists(postId), "Post not created.");

        Post.deletePost(globalRestApiAdminUser, postId);
        assertFalse(Post.publicPostExists(postId), "Post was not deleted.");

    }

    @Test
    public void postCommentsListed_When_Requested_By_User() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = new UserModel();
        User.register(newUser, ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = Post.createPost(globalRestApiUser, publicVisibility);
        assertTrue(Post.publicPostExists(post.getPostId()), "Post not created.");

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
            CommentModel comment = Comment.createComment(newUser, post);
            assertTrue(Comment.commentExists(comment.getCommentId()), "Comment not created.");
            commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = Post.findCommentsOfAPost(post);

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            if (commentIds.contains(comment.getCommentId())) {
                assertNotNull(comment, "Comment is null");
                Comment.deleteComment(newUser, comment.getCommentId());
                assertFalse(Comment.commentExists(comment.getCommentId()), "Post was not deleted.");
            }
        }

        Post.deletePost(globalRestApiUser, post.getPostId());
        assertFalse(Post.publicPostExists(post.getPostId()), "Post was not deleted.");

        User.disableUser(globalRestApiAdminUser, newUser);
    }

}
