package test.cases.wearerestassuredtests.base;

import restassuredapi.models.models.UserModel;
import org.testng.annotations.*;
import restassuredapi.UserApi;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.*;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRestApiAdminUser = new UserModel();
    protected UserModel globalRestApiUser = new UserModel();

    @BeforeClass
    public void setUpRestAssured() {
        UserApi.register(globalRestApiAdminUser, ROLE_ADMIN.toString());
        UserApi.register(globalRestApiUser, ROLE_USER.toString());
    }

    @AfterClass
    public void disableGlobalUser() {
        UserApi.disableUser(globalRestApiAdminUser, globalRestApiUser);
    }

}
