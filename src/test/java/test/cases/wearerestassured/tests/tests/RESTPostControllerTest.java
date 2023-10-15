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

    private UserModel postUser;

    @BeforeClass
    public void setUp() {
        postUser = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        WEareApi.disableUser(globalAdminUser, postUser.getId());
    }

    @Test
    public void user_Can_Create_Public_Post_With_Valid_Data() {

        boolean publicVisibility = true;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);

        WEareApi.assertPostCreation(post, publicVisibility);

        WEareApi.deletePost(postUser, post.getPostId());
    }

    @Test
    public void user_Can_Create_Private_Post_With_Valid_Data() {

        boolean publicVisibility = false;
        PostModel post = WEareApi.createPost(postUser, publicVisibility);

        WEareApi.assertPostCreation(post, publicVisibility);

        WEareApi.deletePost(postUser, post.getPostId());
    }

    @Test
    public void user_Can_Edit_Own_Public_Post_With_Valid_Data() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());
    }

    @Test
    public void user_Can_Edit_Own_Private_Post_With_Valid_Data() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(postUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void admin_User_Can_Edit_Public_Post_Of_Another_User_With_Valid_Data() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(adminUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void admin_User_Can_Edit_Private_Post_Of_Another_User_With_Valid_Data() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;

        PostModel postToBeEdited = WEareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        WEareApi.editPost(adminUser, postToBeEdited.getPostId());

        WEareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        WEareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void user_Can_Find_All_Posts() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = WEareApi.createPost(postUser, publicVisibility);
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = WEareApi.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            assertEquals(foundPost.getClass(), PostModel.class, "There are no posts found");
            assertNotNull(foundPost.getPostId(), "There are no posts found");

            if (postIds.contains(foundPost.getPostId())) {
                WEareApi.deletePost(postUser, foundPost.getPostId());
            }
        }

    }

    @Test
    public void user_Can_Like_Post() {

        boolean publicVisibility = false;
        PostModel postToBeLiked = WEareApi.createPost(postUser, publicVisibility);

        PostModel likedPost = WEareApi.likePost(postUser, postToBeLiked.getPostId());

        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;
        assertEquals(likedPost.getLikes().size(), likedPostLikesToHave, "Post was not liked.");
        assertEquals(postToBeLiked.getPostId(), likedPost.getPostId(), "Liked post is different.");

        WEareApi.deletePost(postUser, postToBeLiked.getPostId());

    }

    @Test
    public void user_Can_Delete_Post() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        WEareApi.deletePost(postUser, postId);

        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Private_Post_Of_Another_User() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        WEareApi.deletePost(adminUser, postId);

        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Public_Post_Of_Another_User() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;
        PostModel postToBeDeleted = WEareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        WEareApi.deletePost(adminUser, postId);

        assertFalse(WEareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_All_Comments_Of_A_Post() {

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
            assertEquals(comment.getClass(), CommentModel.class, "Wrong type of comment");
            assertNotNull(comment, "Comment is null");
            WEareApi.deleteComment(newUser, comment.getCommentId());
        }

        WEareApi.deletePost(postUser, post.getPostId());

    }

}
