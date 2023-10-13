package test.cases.wearerestassured.tests;

import com.telerikacademy.testframework.models.CommentModel;
import com.telerikacademy.testframework.models.PostModel;
import com.telerikacademy.testframework.models.UserModel;
import org.testng.annotations.*;
import test.cases.BaseTestSetup;

import java.util.ArrayList;
import java.util.List;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTPostControllerTest extends BaseTestSetup {

    private UserModel postUser;

    @BeforeClass
    private void setUp() {
        postUser = weareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void clear() {
        weareApi.disableUser(globalAdminUser, postUser.getId());
    }

    @Test
    public void user_Can_Create_Public_Post_With_Valid_Data() {

        boolean publicVisibility = true;
        PostModel post = weareApi.createPost(postUser, publicVisibility);

        weareApi.assertPostCreation(post, publicVisibility);

        weareApi.deletePost(postUser, post.getPostId());
    }

    @Test
    public void user_Can_Create_Private_Post_With_Valid_Data() {

        boolean publicVisibility = false;
        PostModel post = weareApi.createPost(postUser, publicVisibility);

        weareApi.assertPostCreation(post, publicVisibility);

        weareApi.deletePost(postUser, post.getPostId());
    }

    @Test
    public void user_Can_Edit_Own_Public_Post_With_Valid_Data() {

        boolean publicVisibility = true;

        PostModel postToBeEdited = weareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        weareApi.editPost(postUser, postToBeEdited.getPostId());

        weareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weareApi.deletePost(postUser, postToBeEdited.getPostId());
    }

    @Test
    public void user_Can_Edit_Own_Private_Post_With_Valid_Data() {

        boolean publicVisibility = false;

        PostModel postToBeEdited = weareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        weareApi.editPost(postUser, postToBeEdited.getPostId());

        weareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void admin_User_Can_Edit_Public_Post_Of_Another_User_With_Valid_Data() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;

        PostModel postToBeEdited = weareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        weareApi.editPost(adminUser, postToBeEdited.getPostId());

        weareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void admin_User_Can_Edit_Private_Post_Of_Another_User_With_Valid_Data() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;

        PostModel postToBeEdited = weareApi.createPost(postUser, publicVisibility);

        String postToBeEditedContent = postToBeEdited.getContent();

        weareApi.editPost(adminUser, postToBeEdited.getPostId());

        weareApi.assertEditedPost(postToBeEdited.getPostId(), postToBeEditedContent);

        weareApi.deletePost(postUser, postToBeEdited.getPostId());

    }

    @Test
    public void user_Can_Find_All_Posts() {

        List<Integer> postIds = new ArrayList<>();

        boolean publicVisibility = true;

        int postCount = 5;

        for (int i = 0; i < postCount; i++) {
            PostModel post = weareApi.createPost(postUser, publicVisibility);
            postIds.add(post.getPostId());
        }

        PostModel[] foundPosts = weareApi.findAllPosts();

        for (PostModel foundPost : foundPosts) {
            assertEquals(foundPost.getClass(), PostModel.class, "There are no posts found");
            assertNotNull(foundPost.getPostId(), "There are no posts found");

            if (postIds.contains(foundPost.getPostId())) {
                weareApi.deletePost(postUser, foundPost.getPostId());
            }
        }

    }

    @Test
    public void user_Can_Like_Post() {

        boolean publicVisibility = false;
        PostModel postToBeLiked = weareApi.createPost(postUser, publicVisibility);

        PostModel likedPost = weareApi.likePost(postUser, postToBeLiked.getPostId());

        int likedPostLikesToHave = postToBeLiked.getLikes().size() + 1;
        assertEquals(likedPost.getLikes().size(), likedPostLikesToHave, "Post was not liked.");
        assertEquals(postToBeLiked.getPostId(), likedPost.getPostId(), "Liked post is different.");

        weareApi.deletePost(postUser, postToBeLiked.getPostId());

    }

    @Test
    public void user_Can_Delete_Post() {

        boolean publicVisibility = false;

        PostModel postToBeDeleted = weareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        weareApi.deletePost(postUser, postId);

        assertFalse(weareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Private_Post_Of_Another_User() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = false;
        PostModel postToBeDeleted = weareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        weareApi.deletePost(adminUser, postId);

        assertFalse(weareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void admin_User_Can_Delete_Public_Post_Of_Another_User() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        boolean publicVisibility = true;
        PostModel postToBeDeleted = weareApi.createPost(postUser, publicVisibility);
        int postId = postToBeDeleted.getPostId();

        weareApi.deletePost(adminUser, postId);

        assertFalse(weareApi.postExists(postId), "Post was not deleted.");

    }

    @Test
    public void user_Can_Find_All_Comments_Of_A_Post() {

        List<Integer> commentIds = new ArrayList<>();

        UserModel newUser = weareApi.registerUser(ROLE_USER.toString());

        boolean publicVisibility = true;

        PostModel post = weareApi.createPost(postUser, publicVisibility);

        int commentCount = 3;

        for (int i = 0; i < commentCount; i++) {
           CommentModel comment = weareApi.createComment(newUser, post);
           commentIds.add(comment.getCommentId());
        }

        CommentModel[] postComments = weareApi.findCommentsOfAPost(post.getPostId());

        assertEquals(postComments.length, commentCount, "Wrong post comments count");

        for (CommentModel comment : postComments) {
            assertEquals(comment.getClass(), CommentModel.class, "Wrong type of comment");
            assertNotNull(comment, "Comment is null");
            weareApi.deleteComment(newUser, comment.getCommentId());
        }

        weareApi.deletePost(postUser, post.getPostId());

    }

}
