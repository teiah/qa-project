package models.models;

import models.basemodel.BaseModel;
import com.google.gson.Gson;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static models.models.UserBySearchModel.searchUsersBody;
import static org.testng.Assert.*;
import static org.testng.Assert.assertNotEquals;
import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class UserModel extends BaseModel {

    public static final String skillBody = "{\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"skill\": \"%s\",\n" +
            "  \"skillId\": 0\n" +
            "}";
    public static final String commentBody = "{\n" +
            "  \"commentId\": 0,\n" +
            "  \"content\": \"%s\",\n" +
            "  \"deletedConfirmed\": %s,\n" +
            "  \"postId\": %s,\n" +
            "  \"userId\": %s\n" +
            "}";
    public static final String sendRequestBody = "{\n" +
            "  \"id\": %s,\n" +
            "  \"username\": \"%s\"\n" +
            "}";
    public static final String expertiseProfileBody = "{\n" +
            "  \"availability\": %d,\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"id\": 0,\n" +
            "  \"skill1\": \"%s\",\n" +
            "  \"skill2\": \"%s\",\n" +
            "  \"skill3\": \"%s\",\n" +
            "  \"skill4\": \"%s\",\n" +
            "  \"skill5\": \"%s\"\n" +
            "}";
    private List<GrantedAuthorityModel> authorities = new ArrayList<>();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private String email;
    private boolean enabled = true;
    private ExpertiseProfileModel expertiseProfile;
    private String password;
    private PersonalProfileModel personalProfile;
    private int userId;
    private String username;
    private static final String userBody = "{\n" +
            "  \"authorities\": [\n" +
            "    %s\n" +
            "  ],\n" +
            "  \"category\": {\n" +
            "    \"id\": %s,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"confirmPassword\": \"" + "%s" + "\",\n" +
            "  \"email\": \"" + "%s" + "\",\n" +
            "  \"password\": \"" + "%s" + "\",\n" +
            "  \"username\": \"" + "%s" + "\"\n" +
            "}";

    public UserModel() {
    }

    public List<GrantedAuthorityModel> getAuthorities() {
        return this.authorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ExpertiseProfileModel getExpertiseProfile() {
        return expertiseProfile;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public PersonalProfileModel getPersonalProfile() {
        return personalProfile;
    }

    public int getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthorities(List<GrantedAuthorityModel> authorities) {
        this.authorities = authorities;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setExpertiseProfile(ExpertiseProfileModel expertiseProfile) {
        this.expertiseProfile = expertiseProfile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addAuthority(RoleModel authority) {
        this.authorities.add(authority);
    }

    public void register(String authority) {

        String email = generateEmail();
        String generatedPassword = generatePassword();
        String generatedUsername = generateUsernameAsImplemented(authority);
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
                .body(String.format(userBody, authority, category.getId(), category.getName(), generatedPassword, email, generatedPassword, generatedUsername))
                .post(API + REGISTER_USER)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(response.getBody().asPrettyString());

        int userId = Integer.parseInt(getUserId(response));

        assertEquals(response.body().asString(), String.format("User with name %s and id %s was created",
                generatedUsername, userId), "User was not registered");

        extractUser(userId, generatedUsername, generatedPassword);
    }

    private void extractUser(int userId, String username, String password) {

        UserByIdModel userByIdModel = getUserById(username, userId).as(UserByIdModel.class);
        this.setUserId(userByIdModel.getId());
        this.setUsername(userByIdModel.getUsername());
        this.setPassword(password);
        this.setEmail(userByIdModel.getEmail());
        String firstName = generateFirstName();
        this.personalProfile = new PersonalProfileModel();
        this.personalProfile.setPersonalProfileFirstName(this.userId, this.username, this.password, firstName);
        assertEquals(this.getPersonalProfile().getFirstName(), firstName,
                "User personal profile was not updated.");
        UserBySearchModel userBySearchModel = searchUser(this.getId(), this.getPersonalProfile().getFirstName());
        assert userBySearchModel != null;
        this.setExpertiseProfile(userBySearchModel.getExpertiseProfile());
        this.setAccountNonExpired(userBySearchModel.isAccountNonExpired());
        this.setAccountNonLocked(userBySearchModel.isAccountNonLocked());
        this.setCredentialsNonExpired(userBySearchModel.isCredentialsNonExpired());
        this.setEnabled(userBySearchModel.isEnabled());
        List<GrantedAuthorityModel> authorities = new ArrayList<>();
        RoleModel roleModel = new RoleModel();
        roleModel.setAuthority(ROLE_USER.toString());
        authorities.add(roleModel);
        if (userByIdModel.getAuthorities().length == 2) {
            roleModel.setAuthority(ROLE_ADMIN.toString());
            authorities.add(roleModel);
        }
        this.setAuthorities(authorities);
        assertNotNull(this.getExpertiseProfile().getCategory(), "User has no professional category");

    }

    void setPersonalProfile(PersonalProfileModel personalProfileModel) {
        this.personalProfile = personalProfileModel;
    }

    public void editExpertiseProfile() {

        int availability = 8;
        int categoryId = 100;
        String categoryName = "All";
        String skill1 = generateSkill();
        String skill2 = generateSkill();
        String skill3 = generateSkill();
        String skill4 = generateSkill();
        String skill5 = generateSkill();

        this.getExpertiseProfile().setAvailability(availability);
        this.getExpertiseProfile().getCategory().setId(categoryId);
        this.getExpertiseProfile().getCategory().setName(categoryName);
        for (int i = 0; i < 5; i++) {
            this.getExpertiseProfile().getSkills().add(new SkillModel());
        }
        this.getExpertiseProfile().getSkills().get(0).setSkill(skill1);
        this.getExpertiseProfile().getSkills().get(1).setSkill(skill2);
        this.getExpertiseProfile().getSkills().get(2).setSkill(skill3);
        this.getExpertiseProfile().getSkills().get(3).setSkill(skill4);
        this.getExpertiseProfile().getSkills().get(4).setSkill(skill5);

        String body = String.format(expertiseProfileBody, availability, categoryId, categoryName, skill1, skill2, skill3,
                skill4, skill5);

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(String.format(API + UPGRADE_USER_EXPERTISE_WITH_ID, this.getId()))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        UserBySearchModel userBySearchModel = searchUser(this.getId(), this.getPersonalProfile().getFirstName());

        assert userBySearchModel != null;
        assertEditExpertiseProfile(userBySearchModel, this.expertiseProfile);

        LOGGER.info(String.format("Expertise profile of user %s with id %d was updated", this.getUsername(), this.getId()));

    }

    public void disableUser(int userId) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", false)
                .queryParam("userId", userId)
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("User with id %d disabled.", userId));
    }

    public void enableUser(UserModel userToBeEnabled) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .formParam("enable", true)
                .queryParam("userId", userToBeEnabled.getId())
                .post(ADMIN_STATUS);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_MOVED_TEMPORARILY, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("User %s with id %d enabled.", userToBeEnabled.getUsername(), userToBeEnabled.getId()));

    }

    private String getUserId(Response response) {
        return response.body().asString().replaceAll("\\D", "");
    }

    private void assertEditExpertiseProfile(UserBySearchModel userBySearchModel, ExpertiseProfileModel expertiseProfileModel) {

        assertEquals(userBySearchModel.getExpertiseProfile().getCategory().getId(), expertiseProfileModel.getCategory().getId(),
                "Category ids do not match.");
        assertEquals(userBySearchModel.getExpertiseProfile().getCategory().getName(), expertiseProfileModel.getCategory().getName(),
                "Category names do not match.");
        assertEquals(userBySearchModel.getExpertiseProfile().getAvailability(), expertiseProfileModel.getAvailability(),
                "Availabilities do not match.");
        for (int i = 0; i < 5; i++) {
            assertEquals(userBySearchModel.getExpertiseProfile().getSkills().get(i).getSkill(),
                    expertiseProfileModel.getSkills().get(i).getSkill(), String.format("Skill%d do not match.", i));
        }

    }

    public void editPersonalProfile(int userId, String username, String password) {
        this.personalProfile.updatePersonalProfile(userId, username, password);
    }

    public PostModel createPost(boolean publicVisibility) {

        PostModel post = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(PostModel.generatePostBody(publicVisibility))
                .when()
                .post(API + CREATE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .response()
                .as(PostModel.class);

        if (publicVisibility) {
            LOGGER.info(String.format("Public post with id %d created by user %s.", post.getPostId(), this.getUsername()));
        } else {
            LOGGER.info(String.format("Private post with id %d created by user %s.", post.getPostId(), this.getUsername()));
        }

        return post;
    }

    public void editPost(PostModel post) {

        boolean visibility = post.isPublic();

        Response editedPostResponse = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .queryParam("postId", post.getPostId())
                .body(String.format(PostModel.generatePostBody(visibility)))
                .put(API + EDIT_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        if (post.isPublic()) {
            LOGGER.info(String.format("Public post with id %d edited.", post.getPostId()));
        } else {
            LOGGER.info(String.format("Private post with id %d edited.", post.getPostId()));
        }

    }

    public void likePost(PostModel postToBeLiked) {

        int likesBefore = postToBeLiked.getLikes().size();

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", postToBeLiked.getPostId())
                .post(API + LIKE_POST);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        int likesAfter = response.as(PostModel.class).getLikes().size();

        assertEquals(likesAfter, likesBefore + 1,"Post was not liked.");

        postToBeLiked.getLikes().add(this);

    }

    public PostModel[] showProfilePosts() {

        int index = 0;
        boolean next = true;
        String searchParam1 = "";
        String searchParam2 = this.getPersonalProfile().getFirstName();
        int size = 1000000;

        String body = String.format(searchUsersBody, index, next, searchParam1, searchParam2, size);

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .get(String.format(API + USER_POSTS_WITH_ID, this.getId()))
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        PostModel[] userPosts = new Gson().fromJson(response.getBody().asString(), PostModel[].class);

        return userPosts;

    }

    public void deletePost(int postId) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("postId", postId)
                .delete(API + DELETE_POST)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();

        LOGGER.info(String.format("Post with id %d deleted.", postId));

    }

    public String generateCommentContent() {
        return faker.lorem().sentence(5);
    }

    public CommentModel createComment(PostModel post) {

        String commentContent = generateCommentContent();
        boolean deletedConfirmed = true;
        int postId = post.getPostId();
        int userId = this.getId();

        String body = String.format(commentBody, commentContent, deletedConfirmed, postId, userId);

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
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

        comment.setUser(this);
        comment.setPost(post);

        LOGGER.info(String.format("Comment with id %d created.", comment.getCommentId()));

        return comment;

    }

    public void editComment(CommentModel commentToBeEdited) {

        String commentContent = generateCommentContent();

        Response editedCommentResponse = given()
                .auth()
                .form(this.username, this.password,
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

    public CommentModel likeComment(int commentId) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .post(API + LIKE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(CommentModel.class);

    }

    public CommentModel getCommentById(int commentId) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .get(API + COMMENT_SINGLE);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        CommentModel comment = response.as(CommentModel.class);

        return comment;
    }

    public void deleteComment(int commentId) {
        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("commentId", commentId)
                .delete(API + DELETE_COMMENT);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        LOGGER.info(String.format("Comment with id %d deleted.", commentId));

    }

    public String generateSkill() {
        return faker.lorem().characters(5, 10);
    }

    public SkillModel createSkill() {

        int categoryId = 100;
        String categoryName = "All";
        String skillService = generateSkill();

        String body = String.format(skillBody, categoryId, categoryName, skillService);

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(API + CREATE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        SkillModel skill = response.as(SkillModel.class);

        LOGGER.info(String.format("Skill %s created in category %s.", skillService, categoryName));

        return skill;
    }

    public void deleteSkill(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .put(API + DELETE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        LOGGER.info("Skill deleted.");
    }

    public SkillModel getSkillById(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .get(API + GET_ONE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(SkillModel.class);

    }

    public Response editSkill(int skillId) {

        String skillService = generateSkill();

        Response response = given()
                .queryParam("skill", skillService)
                .queryParam("skillId", skillId)
                .put(API + EDIT_SKILL);

        return response;

    }

    public RequestModel sendRequest(UserModel receiver) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, receiver.getId(), receiver.getUsername()))
                .post(API + REQUEST);

        LOGGER.info(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");
        assertEquals(response.body().asString(), String.format("%s send friend request to %s",
                this.getUsername(), receiver.getUsername()), "Connection request was not send");

        RequestModel request = new RequestModel();
        request.setSender(this);
        request.setReceiver(receiver);
        String[] fields = getRequestBySenderAndReceiver(this, receiver);
        request.setId(Integer.parseInt(fields[0]));
        request.setTimeStamp(fields[1]);

        return request;

    }

    public RequestModel[] getUserRequests() {
        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .get(String.format(API + USER_REQUEST_WITH_ID, this.getId()));

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

    public Response approveRequest(RequestModel request) {

        Response response = given()
                .auth()
                .form(this.getUsername(), this.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .queryParam("requestId", request.getId())
                .post(String.format(API + APPROVE_REQUEST_WITH_ID, this.getId()));

        LOGGER.info(response.getBody().asPrettyString());

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response;
    }

    public Response disconnectFromUser(UserModel sender) {

        Response response = given()
                .auth()
                .form(sender.getUsername(), sender.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(String.format(sendRequestBody, this.getId(), this.getUsername()))
                .post(API + REQUEST);

        LOGGER.info(response.getBody().asPrettyString());

        return response;

    }

    public void connectTo(UserModel receiver) {

        RequestModel requestModel = this.sendRequest(receiver);
        receiver.approveRequest(requestModel);

    }

    public void assertEditedPublicPost(int postId, String postToBeEditedContent) {

        PostModel[] foundPosts = findAllPosts();

        for (PostModel post : foundPosts) {
            if (post.getPostId() == postId) {
                assertNotEquals(post.getContent(), postToBeEditedContent,
                        "Post contents are equal. Post was not edited");
                break;
            }
        }
    }

    public void assertEditedPrivatePost(UserModel user, int postId, String postToBeEditedContent) {

        PostModel[] foundPosts = user.showProfilePosts();

        for (PostModel post : foundPosts) {
            if (post.getPostId() == postId) {
                assertNotEquals(post.getContent(), postToBeEditedContent,
                        "Post contents are equal. Post was not edited");
                break;
            }
        }
    }

    public void assertEditedComment(PostModel post, int commentId, String contentToBeEdited) {

        CommentModel[] postComments = post.findAllCommentsOfAPost(this);

        for (CommentModel postComment : postComments) {
            if (postComment.getCommentId() == commentId) {
                assertNotEquals(postComment.getContent(), contentToBeEdited, "Contents are the same.");
                break;
            }
        }
    }
}
