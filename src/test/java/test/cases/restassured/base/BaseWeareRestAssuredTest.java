package test.cases.restassured.base;

import api.models.models.UserModel;
import org.testng.annotations.*;
import api.controllers.UserController;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRestApiAdminUser = new UserModel();
    protected UserModel globalRestApiUser = new UserModel();

    @BeforeClass
    public void setUpRestAssured() {
        UserController.register(globalRestApiAdminUser, ROLE_ADMIN.toString());
        UserController.register(globalRestApiUser, ROLE_USER.toString());
    }

    @AfterClass
    public void disableGlobalUser() {
        UserController.disableUser(globalRestApiAdminUser, globalRestApiUser);
    }

}
