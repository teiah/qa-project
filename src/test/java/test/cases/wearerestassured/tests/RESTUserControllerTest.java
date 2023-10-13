package test.cases.wearerestassured.tests;

import com.telerikacademy.testframework.models.ExpertiseProfileModel;
import com.telerikacademy.testframework.models.PersonalProfileModel;
import com.telerikacademy.testframework.models.PostModel;
import com.telerikacademy.testframework.models.UserModel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.cases.BaseTestSetup;

import java.sql.SQLException;

import static com.telerikacademy.testframework.utils.UserRoles.*;
import static org.testng.Assert.*;

public class RESTUserControllerTest extends BaseTestSetup {

    @Test
    public void user_Can_Register_With_Valid_Credentials() throws SQLException {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());

        assertEquals(user.getUsername(), user.getUsername(), "User was not registered");
        assertEquals(user.getPassword(), user.getPassword(), "User was not registered");
        assertEquals(user.getAuthorities().size(), 1, "User is not registered as \"USER\".");

        weareApi.disableUser(globalAdminUser, user.getId());

    }

    @Test
    public void user_Can_Register_As_Admin_With_Valid_Credentials() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        assertEquals(adminUser.getUsername(), adminUser.getUsername(), "User was not registered");
        assertEquals(adminUser.getPassword(), adminUser.getPassword(), "User was not registered");
        assertEquals(adminUser.getAuthorities().size(), 2, "User was not registered as admin");

        weareApi.disableUser(globalAdminUser, adminUser.getId());

    }

    @Test
    public void user_Can_Edit_Personal_Profile_With_Valid_Data() {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = weareApi.editPersonalProfile(user);
        user.setPersonalProfile(personalProfile);

        assertEquals(user.getPersonalProfile().toString(), personalProfile.toString(), "User personal profile was not updated.");

        weareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void user_Can_Edit_Expertise_Profile_With_Valid_Data() {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        ExpertiseProfileModel expertiseProfile = weareApi.editExpertiseProfile(user);
        user.setExpertiseProfile(expertiseProfile);

        assertEquals(user.getExpertiseProfile().toString(), expertiseProfile.toString(),
                "User expertise profile was not updated.");

        weareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void user_Can_Get_Users_By_Search_Parameters() {
        UserModel userOne = weareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = weareApi.editPersonalProfile(userOne);
        userOne.setPersonalProfile(personalProfile);

        String firstname = userOne.getPersonalProfile().getFirstName();

        UserModel userAfterSearch = weareApi.searchUser(firstname);

        assertEquals(userAfterSearch.getUsername(), userOne.getUsername(), "User was not found");
        assertEquals(userAfterSearch.getId(), userOne.getId(), "User was not found");

        weareApi.disableUser(globalAdminUser, userOne.getId());
    }

    @Test
    public void user_Can_See_Profile_Posts_Of_User() {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());
        PersonalProfileModel personalProfile = weareApi.editPersonalProfile(user);
        user.setPersonalProfile(personalProfile);
        boolean publicVisibility;
        int postsCount = 3;
        for (int i = 0; i < postsCount; i++) {
            publicVisibility = i % 2 == 0;
            weareApi.createPost(user, publicVisibility);
        }

        PostModel[] userPosts = weareApi.showProfilePosts(user);

        assertEquals(userPosts.length, postsCount, "Wrong profile posts count");

        for (PostModel userPost : userPosts) {
            assertEquals(userPost.getClass(), PostModel.class, "Wrong type of post");
            assertNotNull(userPost, "Post is null");
            weareApi.deletePost(user, userPost.getPostId());
        }

        weareApi.disableUser(globalAdminUser, user.getId());

    }

    @Test
    public void user_Can_Find_User_By_ID() {

        UserModel user = weareApi.registerUser(ROLE_USER.toString());

        Response returnedUser = weareApi.getUserById(user.getUsername(), user.getId());

        String userId = String.valueOf(returnedUser.getBody().jsonPath().getString("id"));

        Assert.assertEquals(Integer.parseInt(userId), user.getId(), "Ids do not match.");

        weareApi.disableUser(globalAdminUser, user.getId());
    }

    @Test
    public void admin_User_Can_Disable_Another_User() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        UserModel userToBeDisabled = weareApi.registerUser(ROLE_USER.toString());

        PersonalProfileModel personalProfile = weareApi.editPersonalProfile(userToBeDisabled);
        userToBeDisabled.setPersonalProfile(personalProfile);

        String firstname = userToBeDisabled.getPersonalProfile().getFirstName();

        assertTrue(userToBeDisabled.isEnabled(), "User is not enabled");

        weareApi.disableUser(adminUser, userToBeDisabled.getId());

        userToBeDisabled = weareApi.searchUser(firstname);

        assertFalse(userToBeDisabled.isEnabled(), "User was not disabled");

        weareApi.disableUser(globalAdminUser, adminUser.getId());
    }

    @Test
    public void admin_User_Can_Enable_Another_User() {

        UserModel adminUser = weareApi.registerUser(ROLE_ADMIN.toString());
        UserModel userToBeEnabled = weareApi.registerUser(ROLE_USER.toString());

        PersonalProfileModel personalProfile = weareApi.editPersonalProfile(userToBeEnabled);
        userToBeEnabled.setPersonalProfile(personalProfile);

        String firstname = userToBeEnabled.getPersonalProfile().getFirstName();

        weareApi.disableUser(adminUser, userToBeEnabled.getId());

        userToBeEnabled = weareApi.searchUser(firstname);

        assertFalse(userToBeEnabled.isEnabled(), "User is not disabled");

        weareApi.enableUser(adminUser, userToBeEnabled);

        userToBeEnabled = weareApi.searchUser(firstname);

        assertTrue(userToBeEnabled.isEnabled(), "User wss not enabled");

        weareApi.disableUser(globalAdminUser, adminUser.getId());
        weareApi.disableUser(globalAdminUser, userToBeEnabled.getId());
    }

    // Delete User is not implemented and cannot be tested

}
