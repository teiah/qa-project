package models.basemodel;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.mifmif.common.regex.Generex;
import io.restassured.response.Response;
import models.models.*;
import org.testng.log4testng.Logger;

import java.text.SimpleDateFormat;

import static models.models.UserBySearchModel.searchUsersBody;
import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class BaseModel {

    protected static Faker faker = new Faker();
    protected static final Logger LOGGER = Logger.getLogger(BaseModel.class);

    public String generatePassword() {
        return faker.internet().password(6, 200);
    }

    public String generateEmail() {
        return faker.internet().emailAddress();
    }

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generatePersonalReview() {
        return faker.lorem().characters(0, 250);
    }

    public String generateUsernameAsImplemented(String authority) {

        String regex = "[a-zA-Z]*";
        Generex generex = new Generex(regex);

        if (authority.equals(ROLE_ADMIN.toString())) {
            return "admin" + generex.random(5, 15);
        } else {
            return "user" + generex.random(5, 16);
        }

    }

    public String generateBirthdayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(faker.date().birthday());
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generateCity() {
        return faker.address().city();
    }

    public static String generatePicture() {
        return faker.internet().image();
    }

    public static String generatePostContent() {
        return faker.lorem().characters(0, 1000);
    }

    public static Response getUserById(String username, int userId) {

        Response response = given()
                .queryParam("principal", username)
                .get(String.format(API + USER_BY_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(String.format("User with id %d found", userId));

        return response;
    }

    public static UserBySearchModel searchUser(int userId, String firstname) {

        int index = 0;
        boolean next = true;
        String searchParam1 = "";
        String searchParam2 = firstname;
        int size = 1000000;

        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);

        Response response = given()
                .contentType("application/json")
                .body(body)
                .post(API + USERS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        UserBySearchModel[] foundUsers = new Gson().fromJson(response.getBody().asString(), UserBySearchModel[].class);
        for (UserBySearchModel userBySearchModel : foundUsers) {
            if (userBySearchModel.getUserId() == userId) {
                return userBySearchModel;
            }
        }

        return null;
    }

    public static PostModel[] findAllPosts() {

        Response response = given()
                .queryParam("name", "adminvHQOD")
                .get(API + POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        PostModel[] foundPosts = response.as(PostModel[].class);

        return foundPosts;

    }

    public static boolean publicPostExists(int postId) {

        PostModel[] posts = findAllPosts();

        for (PostModel post : posts) {
            if (post.getPostId() == postId) {
                return true;
            }
        }

        return false;

    }

    public static boolean privatePostExists(UserModel user, int postId) {

        PostModel[] posts = user.showProfilePosts();

        for (PostModel post : posts) {
            if (post.getPostId() == postId) {
                return true;
            }
        }

        return false;

    }

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

    public static SkillModel[] getAllSkills() {

        Response response = given()
                .get(API + FIND_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        SkillModel[] skills = new Gson().fromJson(response.getBody().asString(), SkillModel[].class);

        return skills;

    }

    public void connectUsers(UserModel sender, UserModel receiver) {

        receiver.approveRequest(sender.sendRequest(receiver));

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

    public static boolean skillExists(int skillId) {

        SkillModel[] skills = getAllSkills();

        for (SkillModel skill : skills) {
            if (skill.getSkillId() == skillId) {
                return true;
            }
        }

        return false;

    }
}
