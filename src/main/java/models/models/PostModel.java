package models.models;

import models.basemodel.BaseModel;
import com.google.gson.Gson;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;

import java.util.List;
import java.util.Set;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class PostModel extends BaseModel {

    private static final String postBody = "{\n" +
            "  \"content\": \"" + "%s" + "\",\n" +
            "  \"picture\": \"" + "%s" + "\",\n" +
            "  \"public\": " + "%s" + "\n" +
            "}";
    private CategoryModel category;
    private List<CommentModel> comments;
    private String content;
    private String date;
    private boolean isLiked;
    private Set<UserModel> likes;
    private String picture;
    private Integer postId;
    private boolean isPublic;
    private int rank;
    private UserModel user;

    public PostModel() {
    }

    public CategoryModel getCategory() {
        return category;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public Set<UserModel> getLikes() {
        return likes;
    }

    public String getPicture() {
        return picture;
    }

    public Integer getPostId() {
        return postId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getRank() {
        return rank;
    }

    public UserModel getUser() {
        return user;
    }

    public CommentModel[] findCommentsOfAPost() {

        Response response = given()
                .queryParam("postId", this.postId)
                .get(API + COMMENTS_OF_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel[] postComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return postComments;
    }

    public CommentModel[] findAllCommentsOfAPost(UserModel userModel) {

        Response response = given()
                .auth()
                .form(userModel.getUsername(), userModel.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", this.postId)
                .get(API + COMMENT_BY_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel[] postComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return postComments;
    }

    static String generatePostBody(boolean visibility) {
        String postContent = generatePostContent();
        String postPicture = generatePicture();
        return String.format(postBody, postContent, postPicture, visibility);
    }

}
