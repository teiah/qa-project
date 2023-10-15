package test.cases;

import api.WEareApi;
import api.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;

public class BaseTestSetup {

    protected api.WEareApi WEareApi;
    protected UserModel globalAdminUser;
    protected Helpers helpers;
    protected UserActions actions;

    @BeforeClass
    public void setup() {
        WEareApi = new WEareApi();
        helpers = new Helpers();
        actions = new UserActions();
        globalAdminUser = WEareApi.registerUser(ROLE_ADMIN.toString());
    }

    @AfterClass
    public void tearDown() {
        WEareApi.disableUser(globalAdminUser, globalAdminUser.getId());
    }

}
