package api.controllers;

import api.models.EditPostRequest;
import api.models.PostResponse;
import com.telerikacademy.testframework.utils.Utils;
import api.models.PostRequest;
import io.restassured.response.Response;


import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class PostController extends BaseWeAreApi {

    private static final String postBody = "{\n" +
            "  \"content\": \"" + "%s" + "\",\n" +
            "  \"picture\": \"" + "%s" + "\",\n" +
            "  \"public\": " + "%s" + "\n" +
            "}";

    public static PostResponse createPost(PostRequest post, String cookieValue) {

        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .contentType("application/json")
                .body(post)
                .when()
                .post(API + CREATE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(PostResponse.class);

    }

    public static Response editPost(int postId, EditPostRequest postEditor, String cookieValue) {
        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .contentType("application/json")
                .queryParam("postId", postId)
                .body(postEditor)
                .put(API + EDIT_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();
    }


    public static PostResponse likePost(int postId, String cookieValue) {
        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .queryParam("postId", postId)
                .post(API + LIKE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(PostResponse.class);
    }

//    public static Post[] showProfilePosts(UserRequest user) {
//
//        int index = 0;
//        boolean next = true;
//        String searchParam1 = "";
//        String searchParam2 = user.getPersonalProfile().getFirstName();
//        int size = 1000000;
//
//        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);
//
//        Response response = given()
//                .auth()
//                .form(user.getUsername(), user.getPassword(),
//                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
//                .contentType("application/json")
//                .body(body)
//                .get(String.format(API + USER_POSTS_WITH_ID, user.getId()))
//                .then()
//                .assertThat()
//                .statusCode(SC_OK)
//                .extract().response();
//
//        Post[] userPosts = new Gson().fromJson(response.getBody().asString(), Post[].class);
//
//        return userPosts;
//
//    }

//    public static void deletePost(UserRequest user, int postId) {
//
//        Response response = given()
//                .auth()
//                .form(user.getUsername(), user.getPassword(),
//                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
//                .queryParam("postId", postId)
//                .delete(API + DELETE_POST)
//                .then()
//                .assertThat()
//                .statusCode(SC_OK)
//                .extract().response();
//
//        LOGGER.info(String.format("Post with id %d deleted.", postId));
//
//    }

    public static PostResponse[] getAllPosts(String cookieValue) {
        return given()
                .cookie(Utils.getConfigPropertyByKey("auth.cookieName"), cookieValue)
                .get(API + POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response().as(PostResponse[].class);
    }

//    public static boolean publicPostExists(int postId) {
//
//        PostRequest[] posts = findAllPosts();
//
//        for (PostRequest post : posts) {
//            if (post.getPostId() == postId) {
//                return true;
//            }
//        }
//
//        return false;
//
//    }

//    public static boolean privatePostExists(UserRequest user, int postId) {
//
//        Post[] posts = showProfilePosts(user);
//
//        for (Post post : posts) {
//            if (post.getPostId() == postId) {
//                return true;
//            }
//        }
//
//        return false;
//
//    }

//    public static void assertEditedPublicPost(int postId, String postToBeEditedContent) {
//
//        PostRequest[] foundPosts = findAllPosts();
//
//        for (PostRequest post : foundPosts) {
//            if (post.getPostId() == postId) {
//                assertNotEquals(post.getContent(), postToBeEditedContent,
//                        "Post contents are equal. Post was not edited");
//                break;
//            }
//        }
//    }

//    public static void assertEditedPrivatePost(UserRequest user, int postId, String postToBeEditedContent) {
//
//        Post[] foundPosts = showProfilePosts(user);
//
//        for (Post post : foundPosts) {
//            if (post.getPostId() == postId) {
//                assertNotEquals(post.getContent(), postToBeEditedContent,
//                        "Post contents are equal. Post was not edited");
//                break;
//            }
//        }
//    }

//    public static Comment[] findCommentsOfAPost(PostRequest post) {
//
//        Response response = given()
//                .queryParam("postId", post.getPostId())
//                .get(API + COMMENTS_OF_POST);
//
//        int statusCode = response.getStatusCode();
//        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
//
//        Comment[] postComments = new Gson().fromJson(response.getBody().asString(), Comment[].class);
//
//        return postComments;
//    }
//
//    public static Comment[] findAllCommentsOfAPost(UserRequest user, PostRequest post) {
//
//        Response response = given()
//                .auth()
//                .form(user.getUsername(), user.getPassword(),
//                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
//                .queryParam("postId", post.getPostId())
//                .get(API + COMMENT_BY_POST);
//
//        int statusCode = response.getStatusCode();
//        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
//
//        Comment[] postComments = new Gson().fromJson(response.getBody().asString(), Comment[].class);
//
//        return postComments;
//    }
//
//    static String generatePostBody(boolean visibility) {
//        String postContent = Helpers.generatePostContent();
//        String postPicture = Helpers.generatePicture();
//        return String.format(postBody, postContent, postPicture, visibility);
//    }
}
