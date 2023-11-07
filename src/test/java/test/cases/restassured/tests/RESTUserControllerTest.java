package test.cases.restassured.tests;

import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import api.Post;
import api.User;
import api.models.models.*;
import test.cases.restassured.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;
import static api.User.getUserById;
import static api.User.searchUser;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void shouldCreateUser() {

        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_USER.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserModel user = new UserModel();
        User.register(user, username, password, email, authority);

        assertEquals(user.getAuthorities().size(), 1, "User is not registered as \"USER\".");

    }

    @Test
    public void shouldCreateAdmin() {

        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_ADMIN.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserModel adminUser = new UserModel();
        User.register(adminUser, username, password, email, authority);

        assertEquals(adminUser.getUsername(), adminUser.getUsername(), "User was not registered");
        assertEquals(adminUser.getPassword(), adminUser.getPassword(), "User was not registered");
        assertEquals(adminUser.getAuthorities().size(), 2, "User was not registered as admin");

    }

    @Test
    public void shouldEditPersonalProfile() {

        PersonalProfileModel personalProfile = new PersonalProfileModel();
        personalProfile.setBirthYear(Helpers.generateBirthdayDate());
        personalProfile.setFirstName(globalRestApiUser.getPersonalProfile().getFirstName());
        personalProfile.setId(globalRestApiUser.getPersonalProfile().getId());
        personalProfile.setLastName(Helpers.generateLastName());
        personalProfile.getLocation().getCity().setCity(Helpers.generateCity());
        personalProfile.setPersonalReview(Helpers.generatePersonalReview());
        personalProfile.setPicture(Helpers.generatePicture());
        personalProfile.setPicturePrivacy(true);
        personalProfile.setSex("MALE");

        assertNotEquals(globalRestApiUser.getPersonalProfile().toString(), personalProfile.toString(),
                "Personal profiles match.");

        User.updatePersonalProfile(globalRestApiUser, personalProfile);

        assertEquals(globalRestApiUser.getPersonalProfile().toString(), personalProfile.toString(),
                "Personal profile was not updated.");

    }

    @Test
    public void shouldEditExpertiseProfile() {

        ExpertiseProfileModel expertiseProfile = new ExpertiseProfileModel();
        double availability = 8;
        int categoryId = 100;
        String categoryName = "All";
        String skill1 = Helpers.generateSkill();
        String skill2 = Helpers.generateSkill();
        String skill3 = Helpers.generateSkill();
        String skill4 = Helpers.generateSkill();
        String skill5 = Helpers.generateSkill();

       expertiseProfile.setAvailability(availability);
       CategoryModel category = new CategoryModel();
       expertiseProfile.setCategory(category);
       expertiseProfile.getCategory().setId(categoryId);
       expertiseProfile.getCategory().setName(categoryName);

        for (int i = 0; i < 5; i++) {
            expertiseProfile.getSkills().add(new SkillModel());
        }
        expertiseProfile.getSkills().get(0).setSkill(skill1);
        expertiseProfile.getSkills().get(1).setSkill(skill2);
        expertiseProfile.getSkills().get(2).setSkill(skill3);
        expertiseProfile.getSkills().get(3).setSkill(skill4);
        expertiseProfile.getSkills().get(4).setSkill(skill5);

        assertNotEquals(globalRestApiUser.getExpertiseProfile().toString(), expertiseProfile.toString(),
                "Expertise profiles match.");

        User.editExpertiseProfile(globalRestApiUser, expertiseProfile);

        assertEquals(globalRestApiUser.getExpertiseProfile().toString(), expertiseProfile.toString(),
                "User expertise profile was not updated.");
    }

    @Test
    public void shouldRetrieveUsersByExpertiseAndName() {

        String firstname = globalRestApiUser.getPersonalProfile().getFirstName();

        UserBySearchModel userAfterSearch = searchUser(globalRestApiUser.getId(), firstname);

        assert userAfterSearch != null;
        assertEquals(userAfterSearch.getUsername(), globalRestApiUser.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getUserId(), globalRestApiUser.getId(), "User was not found");

    }

    @Test
    public void shouldRetrieveUserPosts() {

        int postsCount = 3;
        for (int i = 0; i < postsCount; i++) {
            boolean publicVisibility = true;
            PostModel publicPost = Post.createPost(globalRestApiUser, publicVisibility);
            assertTrue(Post.publicPostExists(publicPost.getPostId()), "Post not created.");
            publicVisibility = false;
            PostModel privatePost = Post.createPost(globalRestApiUser, publicVisibility);
            assertTrue(Post.privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
        }

        PostModel[] userPosts = Post.showProfilePosts(globalRestApiUser);

        assertEquals(userPosts.length, 2 * postsCount, "Wrong profile posts count");

        for (PostModel userPost : userPosts) {
            Post.deletePost(globalRestApiUser, userPost.getPostId());
            if (userPost.isPublic()) {
                assertFalse(Post.publicPostExists(userPost.getPostId()), "Post not deleted.");
            } else {
                assertFalse(Post.privatePostExists(globalRestApiUser, userPost.getPostId()), "Post not deleted.");
            }
        }

    }

    @Test
    public void shouldRetrieveUserByIdAsAdmin() {

        Response returnedUser = getUserById(globalRestApiAdminUser.getUsername(), globalRestApiUser.getId());

        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(userId, globalRestApiUser.getId(), "Ids do not match.");

    }

    @Test
    public void shouldRetrieveUserByIdAsUser() {

        UserModel userToFind = new UserModel();
        User.register(userToFind, ROLE_USER.toString());

        Response returnedUser = getUserById(userToFind.getUsername(), userToFind.getId());

        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(userId, userToFind.getId(), "Ids do not match.");

        User.disableUser(globalRestApiAdminUser, userToFind);

    }

    @Test
    public void userDisabled_By_AdminUser() {

        UserModel userToBeDisabled = new UserModel();
        User.register(userToBeDisabled, ROLE_USER.toString());

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        User.disableUser(globalRestApiAdminUser, userToBeDisabled);

        UserBySearchModel returnedDisabledUser = searchUser(userToBeDisabled.getId(), firstname);

        assert returnedDisabledUser != null;
        assertEquals(returnedDisabledUser.getUserId(), userToBeDisabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User was not disabled");
    }

    @Test
    public void userEnabled_By_AdminUser() {

        UserModel userToBeEnabled = new UserModel();
        User.register(userToBeEnabled, ROLE_USER.toString());

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        User.disableUser(globalRestApiAdminUser, userToBeEnabled);

        UserBySearchModel returnedDisabledUser = searchUser(userToBeEnabled.getId(), firstname);

        assert returnedDisabledUser != null;
        assertEquals(returnedDisabledUser.getUserId(), userToBeEnabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User is not disabled");

        User.enableUser(globalRestApiAdminUser, userToBeEnabled);

        UserBySearchModel returnedEnabledUser = searchUser(userToBeEnabled.getId(), firstname);
        assert returnedEnabledUser != null;
        assertEquals(returnedEnabledUser.getUserId(), userToBeEnabled.getId(), "User ids do not match.");

        assertTrue(returnedEnabledUser.isEnabled(), "User wss not enabled");

        User.disableUser(globalRestApiAdminUser, userToBeEnabled);
    }

    // Delete User is not implemented and cannot be tested

}
