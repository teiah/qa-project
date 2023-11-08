package test.cases.restassured.base;

import api.controllers.RegistrationController;
import api.models.models.UserModel;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.*;
import api.controllers.UserController;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRestApiAdminUser = new UserModel();
    protected UserModel globalRestApiUser = new UserModel();

    @BeforeClass
    public void setUpRestAssured() {

        String password = Helpers.generatePassword();
        String email = Helpers.generateEmail();
        String authority = ROLE_ADMIN.toString();
        String username = Helpers.generateUsernameAsImplemented(authority);

        UserModel user = new UserModel();
        UserController.register(user, username, password, email, authority);

        UserController.register(globalRestApiAdminUser, username, password, email, authority);
        UserController.register(globalRestApiUser, username, password, email, ROLE_ADMIN.toString());
    }

    @AfterClass
    public void disableGlobalUser() {
        UserController.disableUser(globalRestApiAdminUser, globalRestApiUser);
    }

}
