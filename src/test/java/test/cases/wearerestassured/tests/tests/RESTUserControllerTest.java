package test.cases.wearerestassured.tests.tests;

import api.models.ExpertiseProfileModel;
import api.models.PersonalProfileModel;
import api.models.PostModel;
import api.models.UserModel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.wearerestassured.tests.base.BaseWeareRestAssuredTest;

import java.sql.SQLException;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseWeareRestAssuredTest {

    @Test
    public void user_Can_Register_With_Valid_Credentials() throws SQLException {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());

        assertEquals(user.getUsername(), user.getUsername(), "User was not registered");
        assertEquals(user.getPassword(), user.getPassword(), "User was not registered");
        assertEquals(user.getAuthorities().size(), 1, "User is not registered as \"USER\".");

        WEareApi.disableUser(globalAdminUser, user.getId());

    }

    @Test
    public void user_Can_Register_As_Admin_With_Valid_Credentials() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        assertEquals(adminUser.getUsername(), adminUser.getUsername(), "User was not registered");
        assertEquals(adminUser.getPassword(), adminUser.getPassword(), "User was not registered");
        assertEquals(adminUser.getAuthorities().size(), 2, "User was not registered as admin");

        WEareApi.disableUser(globalAdminUser, adminUser.getId());

    }

    @Test
    public void user_Can_Edit_Personal_Profile_With_Valid_Data() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(user);
        user.setPersonalProfile(personalProfile);

        assertEquals(user.getPersonalProfile().toString(), personalProfile.toString(), "User personal profile was not updated.");

        WEareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void user_Can_Edit_Expertise_Profile_With_Valid_Data() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());
        ExpertiseProfileModel expertiseProfile = WEareApi.editExpertiseProfile(user);
        user.setExpertiseProfile(expertiseProfile);

        assertEquals(user.getExpertiseProfile().toString(), expertiseProfile.toString(),
                "User expertise profile was not updated.");

        WEareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void user_Can_Get_Users_By_Search_Parameters() {
        UserModel userOne = WEareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(userOne);
        userOne.setPersonalProfile(personalProfile);

        String firstname = userOne.getPersonalProfile().getFirstName();

        UserModel userAfterSearch = WEareApi.searchUser(firstname);

        assertEquals(userAfterSearch.getUsername(), userOne.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getId(), userOne.getId(), "User was not found");

        WEareApi.disableUser(globalAdminUser, userOne.getId());
    }

    @Test
    public void user_Can_See_Profile_Posts_Of_User() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(user);
        user.setPersonalProfile(personalProfile);
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

        WEareApi.disableUser(globalAdminUser, user.getId());

    }

    @Test
    public void user_Can_Find_User_By_ID() {

        UserModel user = WEareApi.registerUser(ROLE_USER.toString());

        Response returnedUser = WEareApi.getUserById(user.getUsername(), user.getId());

        String userId = String.valueOf(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(Integer.parseInt(userId), user.getId(), "Ids do not match.");

        WEareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void admin_User_Can_Disable_Another_User() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
        UserModel userToBeDisabled = WEareApi.registerUser(ROLE_USER.toString());

        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(userToBeDisabled);
        userToBeDisabled.setPersonalProfile(personalProfile);

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        WEareApi.disableUser(adminUser, userToBeDisabled.getId());

        userToBeDisabled = WEareApi.searchUser(firstname);

        assertFalse(userToBeDisabled.isEnabled(), "User was not disabled");

        WEareApi.disableUser(globalAdminUser, adminUser.getId());
    }

    @Test
    public void admin_User_Can_Enable_Another_User() {

        UserModel adminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
        UserModel userToBeEnabled = WEareApi.registerUser(ROLE_USER.toString());

        PersonalProfileModel personalProfile = WEareApi.editPersonalProfile(userToBeEnabled);
        userToBeEnabled.setPersonalProfile(personalProfile);

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        WEareApi.disableUser(adminUser, userToBeEnabled.getId());

        userToBeEnabled = WEareApi.searchUser(firstname);

        assertFalse(userToBeEnabled.isEnabled(), "User is not disabled");

        WEareApi.enableUser(adminUser, userToBeEnabled);

        userToBeEnabled = WEareApi.searchUser(firstname);

        assertTrue(userToBeEnabled.isEnabled(), "User wss not enabled");

        WEareApi.disableUser(globalAdminUser, adminUser.getId());
        WEareApi.disableUser(globalAdminUser, userToBeEnabled.getId());
    }

    // Delete User is not implemented and cannot be tested

}
