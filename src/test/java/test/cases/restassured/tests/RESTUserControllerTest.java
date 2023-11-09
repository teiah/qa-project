package test.cases.restassured.tests;

import com.telerikacademy.testframework.utils.Helpers;
import factories.UserFactory;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import api.controllers.UserController;
import api.models.models.*;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.Authority.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RESTUserControllerTest
        extends BaseWeareRestAssuredTest {

    @Test
    public void shouldCreateUser() {
        User registeredUser = UserController.registerUser(UserFactory.createUser());
        User userFromGetRequest = UserController.getUserById(registeredUser.getUsername(), registeredUser.getId());

        assertEquals(userFromGetRequest, registeredUser);
//        TODO: Why are authorities null?
//        assertEquals(userFromGetRequest.getAuthorities().get(0), ROLE_USER);
    }

    @Test
    public void shouldCreateAdmin() {
        User registeredUser = UserController.registerUser(UserFactory.createAdmin());
        User userFromGetRequest = UserController.getUserById(registeredUser.getUsername(), registeredUser.getId());

        assertEquals(userFromGetRequest, registeredUser);
//      TODO: Check if user has admin role
    }

    @Ignore
    @Test
    public void shouldEditPersonalProfile() {

        PersonalProfile personalProfile = new PersonalProfile();
        personalProfile.setBirthYear(Helpers.generateBirthdayDate());
//        personalProfile.setFirstName(globalRestApiUser.getPersonalProfile().getFirstName());
//        personalProfile.setId(globalRestApiUser.getPersonalProfile().getId());
//        personalProfile.setLastName(Helpers.generateLastName());
//        personalProfile.getLocation().getCity().setCity(Helpers.generateCity());
//        personalProfile.setPersonalReview(Helpers.generatePersonalReview());
//        personalProfile.setPicture(Helpers.generatePicture());
//        personalProfile.setPicturePrivacy(true);
//        personalProfile.setSex("MALE");
//
//        assertNotEquals(globalRestApiUser.getPersonalProfile().toString(), personalProfile.toString(),
//                "Personal profiles match.");
//
//        UserController.updatePersonalProfile(globalRestApiUser, personalProfile);
//
//        assertEquals(globalRestApiUser.getPersonalProfile().toString(), personalProfile.toString(),
//                "Personal profile was not updated.");

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

    @Test
    public void userDisabled_By_AdminUser() {

//        User userToBeDisabled = new User();
//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_USER.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        UserController.register();
//
//        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();
//
//        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");
//
////        UserController.disableUser(globalRestApiAdminUser, userToBeDisabled);
//
//        UserBySearch returnedDisabledUser = searchUser(userToBeDisabled.getId(), firstname);
//
//        assert returnedDisabledUser != null;
//        assertEquals(returnedDisabledUser.getUserId(), userToBeDisabled.getId(), "Users do not match.");
//
//        assertFalse(returnedDisabledUser.isEnabled(), "User was not disabled");
//    }
//
//    @Test
//    public void userEnabled_By_AdminUser() {
//
//        User userToBeEnabled = new User();
//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_USER.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        UserController.register();
//
//        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();
//
////        UserController.disableUser(globalRestApiAdminUser, userToBeEnabled);
//
//        UserBySearch returnedDisabledUser = searchUser(userToBeEnabled.getId(), firstname);
//
//        assert returnedDisabledUser != null;
//        assertEquals(returnedDisabledUser.getUserId(), userToBeEnabled.getId(), "Users do not match.");
//
//        assertFalse(returnedDisabledUser.isEnabled(), "User is not disabled");
//
////        UserController.enableUser(globalRestApiAdminUser, userToBeEnabled);
//
//        UserBySearch returnedEnabledUser = searchUser(userToBeEnabled.getId(), firstname);
//        assert returnedEnabledUser != null;
//        assertEquals(returnedEnabledUser.getUserId(), userToBeEnabled.getId(), "User ids do not match.");
//
//        assertTrue(returnedEnabledUser.isEnabled(), "User wss not enabled");

//        UserController.disableUser(globalRestApiAdminUser, userToBeEnabled);
    }

    // Delete User is not implemented and cannot be tested

}
