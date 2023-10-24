package restassuredapi;

import com.google.gson.Gson;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;
import restassuredapi.models.models.CommentModel;
import restassuredapi.models.models.PostModel;
import restassuredapi.models.models.UserModel;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class CommentApi extends BaseWeAreApi {

    public static final String commentBody = "{\n" +
            "  \"commentId\": 0,\n" +
            "  \"content\": \"%s\",\n" +
            "  \"deletedConfirmed\": %s,\n" +
            "  \"postId\": %s,\n" +
            "  \"userId\": %s\n" +
            "}";

    public static CommentModel[] findAllComments() {

        Response response = given()
                .get(API + COMMENT_ALL)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        CommentModel[] allComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return allComments;

    }

    public static CommentModel createComment(UserModel user, PostModel post) {

        String commentContent = Helpers.generateCommentContent();
        boolean deletedConfirmed = true;
        int postId = post.getPostId();
        int userId = user.getId();

        String body = String.format(commentBody, commentContent, deletedConfirmed, postId, userId);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(API + CREATE_COMMENT);

        int statusCode = response.getStatusCode();

        if (statusCode == 500) {
            return null;
        }

        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.jsonPath().getString("content"), commentContent, "Contents do not match.");
        CommentModel comment = response.as(CommentModel.class);

        comment.setUser(user);
        comment.setPost(post);

        LOGGER.info(String.format("Comment with id %d created.", comment.getCommentId()));

        return comment;

    }

    public static boolean commentExists(int commentId) {

        CommentModel[] comments = findAllComments();

        for (CommentModel comment : comments) {
            if (comment.getCommentId() == commentId) {
                return true;
            }
        }

        return false;

    }

    public static void editComment(UserModel user, CommentModel commentToBeEdited) {

        String commentContent = Helpers.generateCommentContent();

        Response editedCommentResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentToBeEdited.getCommentId())
                .queryParam("content", commentContent)
                .put(API + EDIT_COMMENT)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        commentToBeEdited.setContent(commentContent);

        LOGGER.info(String.format("Comment with id %d edited.", commentToBeEdited.getCommentId()));

    }

    public static void likeComment(UserModel user, CommentModel comment) {
        int previousLikes = CommentApi.getCommentById(user, comment.getCommentId()).getLikes().size();
        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", comment.getCommentId())
                .post(API + LIKE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        assertEquals(CommentApi.getCommentById(user, comment.getCommentId()).getLikes().size(), previousLikes + 1, "Comment not liked.");

    }

    public static CommentModel getCommentById(UserModel user, int commentId) {

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .get(API + COMMENT_SINGLE);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel comment = response.as(CommentModel.class);

        return comment;
    }

    public static void deleteComment(UserModel user, int commentId) {
        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .delete(API + DELETE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("Comment with id %d deleted.", commentId));

    }

    public static void assertEditedComment(UserModel user, PostModel post, int commentId, String contentToBeEdited) {

        CommentModel[] postComments = PostApi.findAllCommentsOfAPost(user, post);

        for (CommentModel postComment : postComments) {
            if (postComment.getCommentId() == commentId) {
                assertNotEquals(postComment.getContent(), contentToBeEdited, "Contents are the same.");
                break;
            }
        }
    }
}
