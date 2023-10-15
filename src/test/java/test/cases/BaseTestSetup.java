package test.cases;

import api.WEareApi;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.utils.Helpers;
import com.telerikacademy.testframework.models.*;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.log4testng.Logger;

import static com.telerikacademy.testframework.utils.Constants.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static io.restassured.RestAssured.given;

public class BaseTestSetup {

    protected WEareApi WEareApi;
    protected UserModel globalAdminUser;
    protected Helpers helpers;
    protected UserActions actions = new UserActions();

    public BaseTestSetup() {
    }
    @BeforeClass
    public void setup() {
        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        WEareApi = new WEareApi();
        globalAdminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

        Logger logger;

    }

    @AfterClass
    public void clear() {
        WEareApi.disableUser(globalAdminUser, globalAdminUser.getId());
    }

}
