package test.cases.restassured.base;

import api.models.models.User;
import com.telerikacademy.testframework.utils.Helpers;
import org.testng.annotations.*;
import api.controllers.UserController;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.Authority.*;

public class BaseWeareRestAssuredTest extends BaseTestSetup {

//    protected User globalRestApiAdminUser = new User();
//    protected User globalRestApiUser = new User();

//    @BeforeClass
//    public void setUpRestAssured() {

//        String password = Helpers.generatePassword();
//        String email = Helpers.generateEmail();
//        String authority = ROLE_ADMIN.toString();
//        String username = Helpers.generateUsernameAsImplemented(authority);
//
//        User user = new User();
//        UserController.register(user, username, password, email, authority);
//
//        UserController.register(globalRestApiAdminUser, username, password, email, authority);
//        UserController.register(globalRestApiUser, username, password, email, ROLE_ADMIN.toString());
//    }

//    @AfterClass
//    public void disableGlobalUser() {
//        UserController.disableUser(globalRestApiAdminUser, globalRestApiUser);
//    }

}
