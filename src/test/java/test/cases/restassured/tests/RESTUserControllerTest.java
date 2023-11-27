package test.cases.restassured.tests;

import api.models.*;
import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.utils.Authority;
import factories.ExpertiseFactory;
import factories.ProfileFactory;
import factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;
import api.controllers.UserController;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {
    private String cookieValue;
    private User user;

    @Test
    public void shouldCreateUser() {
        user = UserFactory.createUser();
        UserResponse registeredUser = UserController.registerUser(
                new UserRequest(Authority.ROLE_USER.toString(), user));

        UserResponse userFromGetRequest = UserController.getUserById(registeredUser.getUsername(), registeredUser.getId());

        assertEquals(userFromGetRequest, registeredUser);
//        assertEquals(userFromGetRequest.getAuthorities().get(0), Authority.ROLE_USER);
    }

    @Test
    public void shouldCreateAdmin() {
        user = UserFactory.createAdmin();
        UserResponse registeredUser = UserController.registerUser(
                new UserRequest(Authority.ROLE_ADMIN.toString(), user));
        UserResponse userFromGetRequest = UserController.getUserById(registeredUser.getUsername(), registeredUser.getId());

        assertEquals(userFromGetRequest, registeredUser);
//      TODO: Check if user has admin role
    }

    @Test
    public void shouldEditPersonalProfile() {
        user = UserFactory.createUser();
        UserResponse registeredUser = UserController.registerUser(new UserRequest(Authority.ROLE_USER.toString(), user));
        cookieValue = getCookieValue(user);
        user.setProfile(ProfileFactory.createProfile());
        PersonalProfileRequest update = new PersonalProfileRequest(user);

        PersonalProfileRequest editedProfile = UserController.updatePersonalProfile(registeredUser.getId(), update, cookieValue);

        assertEquals(editedProfile.getFirstName(), update.getFirstName(), "First names do not match.");
        assertEquals(editedProfile.getLastName(), update.getLastName(), "Last names do not match.");
        assertEquals(editedProfile.getBirthYear(), update.getBirthYear(), "Birth dates do not match.");
        assertEquals(editedProfile.getLocation().getCity().getCity(), update.getLocation().getCity().getCity(),
                "Cities do not match.");
        assertEquals(editedProfile.getPersonalReview(), update.getPersonalReview(),
                "Personal reviews do not match.");
        assertEquals(editedProfile.getPicturePrivacy(), update.getPicturePrivacy(),
                "Pictures' privacy do not match.");
    }


    @Test
    public void shouldEditExpertiseProfile() {
        user = UserFactory.createUser();
        UserResponse registeredUser = UserController.registerUser(new UserRequest(Authority.ROLE_USER.toString(), user));
        cookieValue = getCookieValue(user);
        user.getProfile().setSkills(ExpertiseFactory.createSkills());
        ExpertiseProfileRequest update = new ExpertiseProfileRequest(user);
        ExpertiseProfileResponse editedProfile = UserController.editExpertiseProfile(
                registeredUser.getId(), update, cookieValue);
        UserResponse userFromGetRequest = UserController.getUserById(registeredUser.getUsername(), registeredUser.getId());


        assertEquals(editedProfile.getAvailability(), update.getAvailability(), "Availability does not match.");

        assertEquals(editedProfile.getCategory().getName(), update.getCategory().getName(), "Category does not match.");
        assertEquals(userFromGetRequest, registeredUser);

    }

    @Test
    public void shouldRetrieveUsersByExpertiseAndName() {
        user = UserFactory.createUser();
        UserResponse registeredUser = UserController.registerUser(new UserRequest(Authority.ROLE_USER.toString(), user));
        user.setProfile(ProfileFactory.createProfile());
        PersonalProfileRequest update = new PersonalProfileRequest(user);
        cookieValue = getCookieValue(user);
        PersonalProfileRequest editedProfile = UserController.updatePersonalProfile(registeredUser.getId(), update, cookieValue);

        AllUsersRequest allUsersRequest = new AllUsersRequest(0, true, "", editedProfile.getFirstName(), 10);
        AllUsersResponse[] users = UserController.getUsers(allUsersRequest);

        for (AllUsersResponse responseUser : users) {
            UserResponse returnedUser = UserController.getUserById("admin", responseUser.getUserId());

            Assertions.assertEquals(editedProfile.getFirstName(), returnedUser.getFirstName(),
                    "The username didn't match.");
            Assertions.assertEquals(responseUser.getUsername(), returnedUser.getUsername(),
                    "The username didn't match.");
            Assertions.assertEquals(responseUser.getUserId(), returnedUser.getId(),
                    "The id didn't match.");
        }
    }

    @Test
    public void shouldRetrieveUserPosts() {
//
//        int postsCount = 3;
//        for (int i = 0; i < postsCount; i++) {
//            boolean publicVisibility = true;
//            Post publicPost = PostController.createPost(globalRestApiUser, publicVisibility);
//            assertTrue(PostController.publicPostExists(publicPost.getPostId()), "Post not created.");
//            publicVisibility = false;
//            Post privatePost = PostController.createPost(globalRestApiUser, publicVisibility);
//            assertTrue(PostController.privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
//        }
//
//        Post[] userPosts = PostController.showProfilePosts(globalRestApiUser);
//
//        assertEquals(userPosts.length, 2 * postsCount, "Wrong profile posts count");
//
//        for (Post userPost : userPosts) {
//            PostController.deletePost(globalRestApiUser, userPost.getPostId());
//            if (userPost.isPublic()) {
//                assertFalse(PostController.publicPostExists(userPost.getPostId()), "Post not deleted.");
//            } else {
//                assertFalse(PostController.privatePostExists(globalRestApiUser, userPost.getPostId()), "Post not deleted.");
//            }
//        }

    }

    @Test
    public void shouldRetrieveUserByIdAsAdmin() {
//
//        Response returnedUser = getUserById(globalRestApiAdminUser.getUsername(), globalRestApiUser.getId());
//
//        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));
//
//        Assert.assertEquals(userId, globalRestApiUser.getId(), "Ids do not match.");

    }

    @Test
    public void shouldRetrieveUserByIdAsUser() {
//
//        User userToFind = new User();
//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_USER.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        UserController.register();
//
//        Response returnedUser = getUserById(userToFind.getUsername(), userToFind.getId());
//
//        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));
//
//        Assert.assertEquals(userId, userToFind.getId(), "Ids do not match.");

//        UserController.disableUser(globalRestApiAdminUser, userToFind);

    }

}
