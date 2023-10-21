package test.cases.wearerestassuredtests.tests;

import io.restassured.response.Response;
import models.models.PersonalProfileModel;
import models.models.PostModel;
import models.models.UserBySearchModel;
import models.models.UserModel;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassuredtests.base.BaseWeareRestAssuredTest;

import static models.basemodel.BaseModel.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void UserRegistered_When_ValidCredentialsProvided() {

        UserModel userModel = new UserModel();
        userModel.register(ROLE_USER.toString());

        assertEquals(userModel.getAuthorities().size(), 1, "User is not registered as \"USER\".");

    }

    @Test
    public void AdminUserRegistered_When_ValidCredentialsProvided() {

        UserModel adminUser = new UserModel();
        adminUser.register(ROLE_ADMIN.toString());

        assertEquals(adminUser.getUsername(), adminUser.getUsername(), "User was not registered");
        assertEquals(adminUser.getPassword(), adminUser.getPassword(), "User was not registered");
        assertEquals(adminUser.getAuthorities().size(), 2, "User was not registered as admin");

    }

    @Test
    public void UserPersonalProfileEdited_When_ValidDataProvided() {

        PersonalProfileModel personalProfileBeforeUpdate = globalRestApiUser.getPersonalProfile();

        globalRestApiUser.editPersonalProfile(globalRestApiUser.getId(), globalRestApiUser.getUsername(), globalRestApiUser.getPassword());

        globalRestApiUser.getPersonalProfile().assertUpdatePersonalProfile(personalProfileBeforeUpdate);
    }

    @Test
    public void UserExpertiseProfileEdited_When_ValidDataProvided() {

        String expertiseProfile = globalRestApiUser.getExpertiseProfile().toString();

        globalRestApiUser.editExpertiseProfile();

        assertNotEquals(globalRestApiUser.getExpertiseProfile().toString(), expertiseProfile,
                "User expertise profile was not updated.");
    }

    @Test
    public void UserFound_When_SearchParametersProvided() {

        String firstname = globalRestApiUser.getPersonalProfile().getFirstName();

        UserBySearchModel userAfterSearch = searchUser(globalRestApiUser.getId(), firstname);

        assert userAfterSearch != null;
        assertEquals(userAfterSearch.getUsername(), globalRestApiUser.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getUserId(), globalRestApiUser.getId(), "User was not found");

    }

    @Test
    public void UserPostsListed_When_Requested() {

        int postsCount = 3;
        for (int i = 0; i < postsCount; i++) {
            boolean publicVisibility = true;
            PostModel publicPost = globalRestApiUser.createPost(publicVisibility);
            assertTrue(publicPostExists(publicPost.getPostId()), "Post not created.");
            publicVisibility = false;
            PostModel privatePost = globalRestApiUser.createPost(publicVisibility);
            assertTrue(privatePostExists(globalRestApiUser, privatePost.getPostId()), "Post not created.");
        }

        PostModel[] userPosts = globalRestApiUser.showProfilePosts();

        assertEquals(userPosts.length, 2 * postsCount, "Wrong profile posts count");

        for (PostModel userPost : userPosts) {
            globalRestApiUser.deletePost(userPost.getPostId());
            if (userPost.isPublic()) {
                assertFalse(publicPostExists(userPost.getPostId()), "Post not deleted.");
            } else {
                assertFalse(privatePostExists(globalRestApiUser, userPost.getPostId()), "Post not deleted.");
            }
        }

    }

    @Test
    public void UserFoundById_When_Requested_By_AdminUser() {

        Response returnedUser = getUserById(globalRestApiAdminUser.getUsername(), globalRestApiUser.getId());

        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(userId, globalRestApiUser.getId(), "Ids do not match.");

    }

    @Test
    public void UserFoundById_When_Requested_By_AnotherUser() {

        UserModel userToFind = new UserModel();
        userToFind.register(ROLE_USER.toString());

        Response returnedUser = getUserById(globalRestApiUser.getUsername(), userToFind.getId());

        int userId = Integer.parseInt(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(userId, userToFind.getId(), "Ids do not match.");

        globalRestApiAdminUser.disableUser(userToFind.getId());

    }

    @Test
    public void UserDisabled_By_AdminUser() {

        UserModel userToBeDisabled = new UserModel();
        userToBeDisabled.register(ROLE_USER.toString());

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        globalRestApiAdminUser.disableUser(userToBeDisabled.getId());

        UserBySearchModel returnedDisabledUser = searchUser(userToBeDisabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeDisabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User was not disabled");
    }

    @Test
    public void UserEnabled_By_AdminUser() {

        UserModel userToBeEnabled = new UserModel();
        userToBeEnabled.register(ROLE_USER.toString());

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        globalRestApiAdminUser.disableUser(userToBeEnabled.getId());

        UserBySearchModel returnedDisabledUser = searchUser(userToBeEnabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeEnabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User is not disabled");

        globalRestApiAdminUser.enableUser(userToBeEnabled);

        UserBySearchModel returnedEnabledUser = searchUser(userToBeEnabled.getId(), firstname);
        assert returnedEnabledUser != null;
        assertEquals(returnedEnabledUser.getUserId(), userToBeEnabled.getId(), "User ids do not match.");

        assertTrue(returnedEnabledUser.isEnabled(), "User wss not enabled");

        globalRestApiAdminUser.disableUser(userToBeEnabled.getId());
    }

    // Delete User is not implemented and cannot be tested

}
