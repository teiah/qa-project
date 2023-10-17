package test.cases.wearerestassured.tests.tests;

import api.models.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {

    UserModel user;

    @BeforeClass
    public void setUpPostTest() {
        user = WEareApi.registerUser(ROLE_USER.toString());
    }

    @AfterClass
    public void tearDownPostTest() {
        WEareApi.disableUser(globalRESTAdminUser, user.getId());
    }

    @Test
    public void userCanRegisterWithValidCredentials() {

        UserModel userModel = WEareApi.registerUser(ROLE_USER.toString());

        assertEquals(userModel.getAuthorities().size(), 1, "User is not registered as \"USER\".");

    }

    @Test
    public void userCanRegisterAsAdminWithValidCredentials() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        assertEquals(adminUser.getUsername(), adminUser.getUsername(), "User was not registered");
        assertEquals(adminUser.getPassword(), adminUser.getPassword(), "User was not registered");
        assertEquals(adminUser.getAuthorities().size(), 2, "User was not registered as admin");

    }

    @Test
    public void userCanEditPersonalProfileWithValidData() {

        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(user);
        user.setPersonalProfile(personalProfile);

        assertEquals(user.getPersonalProfile().toString(), personalProfile.toString(), "User personal profile was not updated.");

    }

    @Test
    public void userCanEditExpertiseProfileWithValid_Data() {

        ExpertiseProfileModel expertiseProfile = WEareApi.editExpertiseProfile(user);
        user.setExpertiseProfile(expertiseProfile);

        assertEquals(user.getExpertiseProfile(), expertiseProfile,
                "User expertise profile was not updated.");

    }

    @Test
    public void userCanGetUsersBySearchParameters() {

        String firstname = user.getPersonalProfile().getFirstName();

        UserBySearchModel userAfterSearch = WEareApi.searchUser(user.getId(), firstname);

        assertEquals(userAfterSearch.getUsername(), user.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getUserId(), user.getId(), "User was not found");

    }

    @Test
    public void userCanSeeProfilePostsOfUser() {

        boolean publicVisibility;
        int postsCount = 3;
        for (int i = 0; i < postsCount; i++) {
            publicVisibility = i % 2 == 0;
            WEareApi.createPost(user, publicVisibility);
        }

        PostModel[] userPosts = WEareApi.showProfilePosts(user);

        assertEquals(userPosts.length, postsCount, "Wrong profile posts count");

        for (PostModel userPost : userPosts) {
            assertEquals(userPost.getClass(), PostModel.class, "Wrong type of post");
            assertNotNull(userPost, "Post is null");
            WEareApi.deletePost(user, userPost.getPostId());
        }

    }

    @Test
    public void userCanFindUserByID() {

        Response returnedUser = WEareApi.getUserById(user.getUsername(), user.getId());

        String userId = String.valueOf(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(Integer.parseInt(userId), user.getId(), "Ids do not match.");

    }

    @Test
    public void adminUserCanDisableAnotherUser() {

        UserModel userToBeDisabled = WEareApi.registerUser(ROLE_USER.toString());

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        WEareApi.disableUser(globalRESTAdminUser, userToBeDisabled.getId());

        UserBySearchModel returnedDisabledUser = WEareApi.searchUser(userToBeDisabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeDisabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User was not disabled");
    }

    @Test
    public void adminUserCanEnableAnotherUser() {

        UserModel userToBeEnabled = WEareApi.registerUser(ROLE_USER.toString());

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        WEareApi.disableUser(globalRESTAdminUser, userToBeEnabled.getId());

        UserBySearchModel returnedDisabledUser = WEareApi.searchUser(userToBeEnabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeEnabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User is not disabled");

        WEareApi.enableUser(globalRESTAdminUser, userToBeEnabled);

        UserBySearchModel returnedEnabledUser = WEareApi.searchUser(userToBeEnabled.getId(), firstname);
        assertEquals(returnedEnabledUser.getUserId(), userToBeEnabled.getId(), "User ids do not match.");

        assertTrue(returnedEnabledUser.isEnabled(), "User wss not enabled");

        WEareApi.disableUser(globalRESTAdminUser, userToBeEnabled.getId());
    }

    // Delete User is not implemented and cannot be tested

}
