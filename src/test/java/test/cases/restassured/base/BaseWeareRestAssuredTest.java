package test.cases.restassured.base;

import api.models.models.UserModel;
import org.testng.annotations.*;
import api.controllers.User;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRestApiAdminUser = new UserModel();
    protected UserModel globalRestApiUser = new UserModel();

    @BeforeClass
    public void setUpRestAssured() {
        User.register(globalRestApiAdminUser, ROLE_ADMIN.toString());
        User.register(globalRestApiUser, ROLE_USER.toString());
    }

    @AfterClass
    public void disableGlobalUser() {
        User.disableUser(globalRestApiAdminUser, globalRestApiUser);
    }

}
