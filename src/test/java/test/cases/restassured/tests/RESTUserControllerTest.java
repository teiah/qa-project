package test.cases.restassured.tests;

import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.utils.Authority;
import factories.ExpertiseFactory;
import factories.ProfileFactory;
import factories.UserFactory;
import org.testng.annotations.Test;
import api.controllers.UserController;
import api.models.models.*;
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

    @Ignore
    @Test
    public void shouldEditExpertiseProfile() {

        ExpertiseProfile expertiseProfile = new ExpertiseProfile();
        double availability = 8;
        int categoryId = 100;
        String categoryName = "All";
        String skill1 = Helpers.generateSkill();
        String skill2 = Helpers.generateSkill();
        String skill3 = Helpers.generateSkill();
        String skill4 = Helpers.generateSkill();
        String skill5 = Helpers.generateSkill();

        expertiseProfile.setAvailability(availability);
        Category category = new Category();
        expertiseProfile.setCategory(category);
        expertiseProfile.getCategory().setId(categoryId);
        expertiseProfile.getCategory().setName(categoryName);

        for (int i = 0; i < 5; i++) {
            expertiseProfile.getSkills().add(new Skill());
        }
        expertiseProfile.getSkills().get(0).setSkill(skill1);
        expertiseProfile.getSkills().get(1).setSkill(skill2);
        expertiseProfile.getSkills().get(2).setSkill(skill3);
        expertiseProfile.getSkills().get(3).setSkill(skill4);
        expertiseProfile.getSkills().get(4).setSkill(skill5);

//        assertNotEquals(globalRestApiUser.getExpertiseProfile().toString(), expertiseProfile.toString(),
//                "Expertise profiles match.");
//
//        UserController.editExpertiseProfile(globalRestApiUser, expertiseProfile);
//
//        assertEquals(globalRestApiUser.getExpertiseProfile().toString(), expertiseProfile.toString(),
//                "User expertise profile was not updated.");
    }

    @Test
    public void shouldRetrieveUsersByExpertiseAndName() {

//        String firstname = globalRestApiUser.getPersonalProfile().getFirstName();
//
//        UserBySearch userAfterSearch = searchUser(globalRestApiUser.getId(), firstname);
//
//        assert userAfterSearch != null;
//        assertEquals(userAfterSearch.getUsername(), globalRestApiUser.getUsername(), "User was not found");
//        assertEquals(userAfterSearch.getUserId(), globalRestApiUser.getId(), "User was not found");

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
