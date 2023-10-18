package test.cases.wearerestassuredtests.base;

import api.models.UserModel;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

    protected UserModel globalRESTAdminUser;

    @BeforeClass
    public void setUpSelenium() {
        globalRESTAdminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
    }

}
