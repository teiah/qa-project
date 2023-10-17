package test.cases.wearerestassured.tests.tests;

import api.models.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void userCanRegisterWithValidCredentials() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());

        assertEquals(user.getAuthorities().size(), 1, "User is not registered as \"USER\".");

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

        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(globalUser);
        globalUser.setPersonalProfile(personalProfile);

        assertEquals(globalUser.getPersonalProfile().toString(), personalProfile.toString(), "User personal profile was not updated.");

    }

    @Test
    public void userCanEditExpertiseProfileWithValid_Data() {

        ExpertiseProfileModel expertiseProfile = WEareApi.editExpertiseProfile(globalUser);
        globalUser.setExpertiseProfile(expertiseProfile);

        assertEquals(globalUser.getExpertiseProfile(), expertiseProfile,
                "User expertise profile was not updated.");

    }

    @Test
    public void userCanGetUsersBySearchParameters() {

        String firstname = globalUser.getPersonalProfile().getFirstName();

        UserBySearchModel userAfterSearch = WEareApi.searchUser(globalUser.getId(), firstname);

        assertEquals(userAfterSearch.getUsername(), globalUser.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getUserId(), globalUser.getId(), "User was not found");

    }

    @Test
    public void userCanSeeProfilePostsOfUser() {

        boolean publicVisibility;
        int postsCount = 3;
        for (int i = 0; i < postsCount; i++) {
            publicVisibility = i % 2 == 0;
            WEareApi.createPost(globalUser, publicVisibility);
        }

        PostModel[] userPosts = WEareApi.showProfilePosts(globalUser);

        assertEquals(userPosts.length, postsCount, "Wrong profile posts count");

        for (PostModel userPost : userPosts) {
            assertEquals(userPost.getClass(), PostModel.class, "Wrong type of post");
            assertNotNull(userPost, "Post is null");
            WEareApi.deletePost(globalUser, userPost.getPostId());
        }

    }

    @Test
    public void userCanFindUserByID() {


        Response returnedUser = WEareApi.getUserById(globalUser.getUsername(), globalUser.getId());

        String userId = String.valueOf(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(Integer.parseInt(userId), globalUser.getId(), "Ids do not match.");

    }

    @Test
    public void adminUserCanDisableAnotherUser() {

        UserModel userToBeDisabled = WEareApi.registerUser(ROLE_USER.toString());

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        WEareApi.disableUser(globalAdminUser, userToBeDisabled.getId());

        UserBySearchModel returnedDisabledUser = WEareApi.searchUser(userToBeDisabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeDisabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User was not disabled");
    }

    @Test
    public void adminUserCanEnableAnotherUser() {

        UserModel userToBeEnabled = WEareApi.registerUser(ROLE_USER.toString());

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        WEareApi.disableUser(globalAdminUser, userToBeEnabled.getId());

        UserBySearchModel returnedDisabledUser = WEareApi.searchUser(userToBeEnabled.getId(), firstname);

        assertEquals(returnedDisabledUser.getUserId(), userToBeEnabled.getId(), "Users do not match.");

        assertFalse(returnedDisabledUser.isEnabled(), "User is not disabled");

        WEareApi.enableUser(globalAdminUser, userToBeEnabled);

        UserBySearchModel returnedEnabledUser = WEareApi.searchUser(userToBeEnabled.getId(), firstname);
        assertEquals(returnedEnabledUser.getUserId(), userToBeEnabled.getId(), "User ids do not match.");

        assertTrue(returnedEnabledUser.isEnabled(), "User wss not enabled");

        WEareApi.disableUser(globalAdminUser, userToBeEnabled.getId());
    }

    // Delete User is not implemented and cannot be tested

}
