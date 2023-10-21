package test.cases.wearerestassuredtests.base;

import models.models.UserModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRestApiAdminUser = new UserModel();
    protected UserModel globalRestApiUser = new UserModel();

    @BeforeClass
    public void setUpRestAssured() {
        globalRestApiAdminUser.register(ROLE_ADMIN.toString());
        globalRestApiUser.register(ROLE_ADMIN.toString());
    }

}
