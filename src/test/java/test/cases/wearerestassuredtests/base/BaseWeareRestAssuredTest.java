package test.cases.wearerestassuredtests.base;

import models.wearemodels.UserModel;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRESTAdminUser;

    @BeforeClass
    public void setUpRestAssured() {
        globalRESTAdminUser = new UserModel();
        globalRESTAdminUser.register(ROLE_ADMIN.toString());
    }

}
