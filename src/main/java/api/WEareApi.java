package api;

import com.google.gson.Gson;
import com.telerikacademy.testframework.PropertiesManager;
import com.telerikacademy.testframework.models.*;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.log4testng.Logger;

import java.sql.*;

import static com.telerikacademy.testframework.utils.Constants.*;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static com.telerikacademy.testframework.utils.JSONRequests.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class WEareApi {

    private final Helpers helpers = new Helpers();
    Logger logger;

    public RequestSpecification getRestAssured(String... args) {
        Gson deserializer = new Gson();
        String baseUri = PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
                .getProperty("weare.baseUrl") + PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
                .getProperty("weare.api");

        if (args.length > 0) {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .baseUri(baseUri)
                    .auth()
                    .form(args[0], args[1],
                            new FormAuthConfig(AUTHENTICATE, "username", "password"));
        } else {
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .baseUri(baseUri);
        }

    }

    public UserModel registerUser(String authority) {

        String email = helpers.generateEmail();
        String password = helpers.generatePassword();
        String username = helpers.generateUsernameAsImplemented(authority);
        int categoryId = 100;
        String categoryName = "All";
        CategoryModel category = new CategoryModel();
        category.setName(categoryName);
        category.setId(categoryId);

        if (authority.equals(ROLE_ADMIN.toString())) {
            authority = String.format("\"%s\", \"%s\"", ROLE_USER, ROLE_ADMIN);
        } else if (authority.equals(ROLE_USER.toString())) {
            authority = String.format("\"%s\"", ROLE_USER);
        }

        Response response = given()
                .contentType("application/json")
                .body(String.format(userBody, authority, category.getId(), category.getName(), password, email, password, username))
                .post(API + REGISTER_USER);

        System.out.println(response.getBody().asPrettyString());

        int userId = Integer.parseInt(getUserId(response));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.body().asString(), String.format("User with name %s and id %s was created",
                username, userId), "User was not registered");

//        UserModel user = getUserById(username, userId).as(UserModel.class);
        UserModel user = new UserModel();
        extractUserModel(user, category, username, password, email, userId);
        assertNotNull(user.getExpertiseProfile().getCategory(), "User has no professional category");

        return user;

    }

    protected void extractUserModel(UserModel user, CategoryModel category, String username, String password, String email, int userId) {

        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        GrantedAuthorityModel authorityModel = new GrantedAuthorityModel();
        authorityModel.setAuthority(ROLE_USER.toString());
        user.getAuthorities().add(authorityModel);
        if (username.contains("admin")) {
            GrantedAuthorityModel adminAuthorityModel = new GrantedAuthorityModel();
            adminAuthorityModel.setAuthority(ROLE_ADMIN.toString());
            user.getAuthorities().add(adminAuthorityModel);
        }
        user.getExpertiseProfile().getCategory().setName(category.getName());
        user.getExpertiseProfile().getCategory().setId(category.getId());

    }

    public PersonalProfileModel editPersonalProfile(UserModel user) {

        String birthYear = helpers.generateBirthdayDate();
        String firstName = helpers.generateFirstName();
        int id = user.getId();
        String lastName = helpers.generateLastName();
        String city = helpers.generateCity();
        String personalReview = helpers.generatePersonalReview();
        String picture = helpers.generatePicture();
        boolean picturePrivacy = true;
        String sex = "MALE";

        String body = String.format(personalProfileBody, birthYear, firstName, id, lastName, city, personalReview, picture,
                picturePrivacy, sex);

        Response editProfileResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("name", user.getUsername())
                .body(body)
                .post(String.format(API + UPGRADE_USER_PERSONAL_WITH_ID, user.getId()));

        int statusCode = editProfileResponse.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        user.setPersonalProfile(editProfileResponse.as(PersonalProfileModel.class));

        assertEditPersonalProfile(editProfileResponse, user.getPersonalProfile());

        return user.getPersonalProfile();

    }

    public ExpertiseProfileModel editExpertiseProfile(UserModel user) {

        int availability = 8;
        int categoryId = 100;
        String categoryName = "All";
        String skill1 = helpers.generateSkill();
        String skill2 = helpers.generateSkill();
        String skill3 = helpers.generateSkill();
        String skill4 = helpers.generateSkill();
        String skill5 = helpers.generateSkill();

        String body = String.format(expertiseProfileBpdy, availability, categoryId, categoryName, skill1, skill2, skill3,
                skill4, skill5);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(String.format(API + UPGRADE_USER_EXPERTISE_WITH_ID, user.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        System.out.println(response.getBody().asPrettyString());

        return response.as(ExpertiseProfileModel.class);
    }

    public UserModel searchUser(String firstname) {

        int index = 0;
        boolean next = true;
        String searchParam1 = "";
        String searchParam2 = firstname;
        int size = 3;

        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);

        Response response = given()
                .contentType("application/json")
                .queryParam("searchParam1", "")
                .queryParam("searchParam2", firstname)
                .body(body)
                .post(API + USERS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        UserModel[] userResponses = new Gson().fromJson(response.getBody().asString(), UserModel[].class);

        return userResponses[0];

    }

    public void disableUser(UserModel adminUser, UserModel userToBeDisabled) {

        Response response = given()
                .auth()
                .form(adminUser.getUsername(), adminUser.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", false)
                .queryParam("userId", userToBeDisabled.getId())
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        System.out.printf("User %s with id %d disabled.\n", userToBeDisabled.getUsername(), userToBeDisabled.getId());
    }

    public void enableUser(UserModel adminUser, UserModel userToBeEnabled) {

        Response response = given()
                .auth()
                .form(adminUser.getUsername(), adminUser.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", true)
                .queryParam("userId", userToBeEnabled.getId())
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        System.out.printf("User %s with id %d enabled.\n", userToBeEnabled.getUsername(), userToBeEnabled.getId());

    }

    public void deleteUser(int userId) throws SQLException {

        String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 0;";
        String deleteUser = "DELETE FROM users WHERE user_id=?;";
        String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 1;";

//        String deleteUserRequestQuery = "DELETE FROM requests WHERE sender_user_id=1367;";
//        String deleteUserCommentsQuery = "DELETE FROM comments_table WHERE user_id=1367;";
//        String deleteUserPostsQuery = "DELETE FROM posts_Table WHERE user_id=1367";
//        String deleteUserQuery = "DELETE FROM users WHERE user_id=1367";

        Connection con = getConnection();


        try {

            Statement statementDisable = con.createStatement();
            statementDisable.executeUpdate(disableForeignKeyChecks);
            PreparedStatement myStmt = con.prepareStatement(deleteUser);
            myStmt.setInt(1, userId);
            myStmt.executeUpdate(deleteUser);
            Statement statementEnable = con.createStatement();
            statementEnable.executeUpdate(enableForeignKeyChecks);

            System.out.printf("User with id %d was deleted.%n", userId);

            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:mysql://84.21.205.241:33061/telerik_project",
                "telerik", "nakovEtap");

        System.out.println("Connected to database");

        return conn;
    }

    protected UserModel readUserById(String username, int userId) {

        Response response = given()
//                .auth()
//                .form(user.getUsername(), user.getPassword(),
//                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("principal", username)
                .get(String.format(API + USER_BY_ID, userId))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .response();

        System.out.println(response.getBody().asPrettyString());

        UserModel user = response.as(UserModel.class);

        return user;
//        System.out.println(response.getBody().asPrettyString());
    }

    public Response getUserById(String username, int userId) {

        Response response = given()
                .queryParam("principal", username)
                .get(String.format(API + USER_BY_ID, userId));

        System.out.println(response.getBody().asPrettyString());

        return response;
    }

    protected String getUserId(Response response) {
        return response.body().asString().replaceAll("\\D", "");
    }

    public PostModel[] findAllPosts() {

        Response response = given()
                .get(API + POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        PostModel[] foundPosts = new Gson().fromJson(response.getBody().asString(), PostModel[].class);

        return foundPosts;

    }

    public PostModel[] showProfilePosts(UserModel user) {

        int index = 0;
        boolean next = true;
        String searchParam1 = "";
        String searchParam2 = user.getPersonalProfile().getFirstName();
        int size = 3;

        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .get(String.format(API + USER_POSTS_WITH_ID, user.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        PostModel[] userPosts = new Gson().fromJson(response.getBody().asString(), PostModel[].class);

        return userPosts;

    }

    public PostModel createPost(UserModel user, boolean publicVisibility) {

        PostModel post = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(helpers.generatePostBody(publicVisibility))
                .when()
                .post(API + CREATE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .response()
                .as(PostModel.class);

        if (publicVisibility) {
            System.out.printf("Public post with id %d created by user %s.\n", post.getPostId(), user.getUsername());
        } else {
            System.out.printf("Private post with id %d created by user %s.\n", post.getPostId(), user.getUsername());
        }

        return post;
    }

    public void editPost(UserModel user, int postId) {

        boolean visibility = true;

        Response editedPostResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("postId", postId)
                .body(String.format(helpers.generatePostBody(visibility)))
                .put(API + EDIT_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        System.out.printf("Post with id %d edited.\n", postId);

    }

    public PostModel likePost(UserModel user, int postId) {
        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", postId)
                .post(API + LIKE_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(PostModel.class);

    }

    public void deletePost(UserModel user, int postId) {

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", postId)
                .delete(API + DELETE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        System.out.printf("Post with id %d deleted.\n", postId);

    }

    public boolean postExists(int postId) {

        PostModel[] posts = findAllPosts();

        for (PostModel post : posts) {
            if (post.getPostId() == postId) {
                return true;
            }
        }

        return false;

    }

    public CommentModel[] findCommentsOfAPost(int postId) {

        Response response = given()
                .queryParam("postId", postId)
                .get(API + COMMENTS_OF_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel[] postComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return postComments;
    }

    public CommentModel[] findAllComments() {

        Response response = given()
                .get(API + COMMENT_ALL)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        CommentModel[] allComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return allComments;

    }

    public CommentModel createComment(UserModel user, PostModel post) {

        String commentContent = helpers.generateCommentContent();
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
                .when()
                .post(API + CREATE_COMMENT);

        int statusCode = response.getStatusCode();

        if (statusCode == 500) {
            return null;
        }

        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel comment = response.as(CommentModel.class);

        comment.setUser(user);
        comment.setPost(post);

        System.out.printf("Comment with id %d created.\n", comment.getCommentId());

        return comment;

    }

    public Response editComment(UserModel user, CommentModel commentToBeEdited) {

        String commentContent = helpers.generateCommentContent();

        Response editedCommentResponse = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentToBeEdited.getCommentId())
                .queryParam("content", commentContent)
                .put(API + EDIT_COMMENT);

        return editedCommentResponse;
    }

    public CommentModel likeComment(UserModel user, int commentId) {

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .post(API + LIKE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(CommentModel.class);

    }

    public void deleteComment(UserModel user, int commentId) {
        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .delete(API + DELETE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        System.out.printf("Comment with id %d deleted.\n", commentId);

    }

    public boolean commentExists(int commentId) {

        CommentModel[] comments = findAllComments();

        for (CommentModel comment : comments) {
            if (comment.getCommentId() == commentId) {
                return true;
            }
        }

        return false;

    }

    public CommentModel[] findAllCommentsOfAPost(UserModel user, int postId) {

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", postId)
                .get(API + COMMENT_BY_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel[] postComments = new Gson().fromJson(response.getBody().asString(), CommentModel[].class);

        return postComments;
    }

    public CommentModel getCommentById(UserModel user, int commentId) {

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

    public RequestModel sendRequest(UserModel sender, UserModel receiver) {

        Response response = given()
                .auth()
                .form(sender.getUsername(), sender.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, receiver.getId(), receiver.getUsername()))
                .post(API + REQUEST);

        System.out.println(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.body().asString(), String.format("%s send friend request to %s",
                sender.getUsername(), receiver.getUsername()), "Connection request was not send");

        RequestModel request = new RequestModel();
        request.setSender(sender);
        request.setReceiver(receiver);
        String[] fields = getRequestBySenderAndReceiver(sender, receiver);
        request.setId(Integer.parseInt(fields[0]));
        request.setTimeStamp(fields[1]);

        return request;

    }

    public RequestModel[] getUserRequests(UserModel receiver) {
        Response response = given()
                .auth()
                .form(receiver.getUsername(), receiver.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .get(String.format(API + USER_REQUEST_WITH_ID, receiver.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        RequestModel[] requests = new Gson().fromJson(response.getBody().asString(), RequestModel[].class);

        return requests;
    }

    protected String[] getRequestBySenderAndReceiver(UserModel sender, UserModel receiver) {
        Response response = given()
                .auth()
                .form(receiver.getUsername(), receiver.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .get(String.format(API + USER_REQUEST_WITH_ID, receiver.getId()));

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        RequestModel[] requests = new Gson().fromJson(response.getBody().asString(), RequestModel[].class);

        String[] fields = new String[2];

        fields[0] = String.valueOf(requests[0].getId());
        fields[1] = String.valueOf(requests[0].getTimeStamp());

        return fields;
    }

    public Response approveRequest(UserModel receiver, RequestModel request) {

        Response response = given()
                .auth()
                .form(receiver.getUsername(), receiver.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("requestId", request.getId())
                .post(String.format(API + APPROVE_REQUEST_WITH_ID, receiver.getId()));

        System.out.println(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response;
    }

    public Response disconnectFromUser(UserModel sender, UserModel receiver) {

        Response response = given()
                .auth()
                .form(sender.getUsername(), sender.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, receiver.getId(), receiver.getUsername()))
                .post(API + REQUEST);

        System.out.println(response.getBody().asPrettyString());

        return response;

    }

    public void connectUsers(UserModel sender, UserModel receiver) {

        approveRequest(receiver, sendRequest(sender, receiver));

    }

    public SkillModel[] getAllSkills() {

        Response response = given()
                .get(API + FIND_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        SkillModel[] skills = new Gson().fromJson(response.getBody().asString(), SkillModel[].class);

        return skills;

    }

    public SkillModel createSkill(UserModel user) {

        int categoryId = 100;
        String categoryName = "All";
        String skillService = helpers.generateSkill();

        String body = String.format(skillBody, categoryId, categoryName, skillService);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(API + CREATE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        SkillModel skill = response.as(SkillModel.class);

        return skill;

    }

    public void deleteSkill(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .put(API + DELETE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

    }

    public SkillModel getSkillById(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .get(API + GET_ONE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(SkillModel.class);

    }

    public boolean skillExists(int skillId) {

        SkillModel[] skills = getAllSkills();

        for (SkillModel skill : skills) {
            if (skill.getSkillId() == skillId) {
                return true;
            }
        }

        return false;

    }

    public Response editSkill(int skillId) {

        String skillService = helpers.generateSkill();

        Response response = given()
                .queryParam("skill", skillService)
                .queryParam("skillId", skillId)
                .put(API + EDIT_SKILL);

        return response;

    }

    protected void assertEditPersonalProfile(Response response, PersonalProfileModel personalProfile) {

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.getBody().jsonPath().getString("firstName"), personalProfile.getFirstName(),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getLastName(), response.getBody().jsonPath().getString("lastName"),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getBirthYear(), response.getBody().jsonPath().getString("birthYear"),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getLocation().getCity().toString(), response.getBody().jsonPath().getString("location.city.city"),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getPersonalReview(), response.getBody().jsonPath().getString("personalReview"),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getPicture(), response.getBody().jsonPath().getString("picture"),
                "User personal profile was not updated.");
        assertEquals(personalProfile.getPicturePrivacy(), response.getBody().jsonPath().getString("picturePrivacy"),
                "User personal profile was not updated.");
    }

    public void assertPostCreation(PostModel post, boolean publicVisibility) {

        assertNotNull(post, "Post was not created.");
        assertNotNull(post.getPostId(), "Post was not created.");

    }

    public void assertEditedPost(int postId, String postToBeEditedContent) {

        PostModel[] foundPosts = findAllPosts();

        for (PostModel post : foundPosts) {
            if (post.getPostId() == postId) {
                assertNotEquals(post.getContent(), postToBeEditedContent,
                        "Post contents are equal. Post was not edited");
                break;
            }
        }
    }
}
