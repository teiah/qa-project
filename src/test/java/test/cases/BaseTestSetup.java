package test.cases;

import api.WEareApi;
import api.models.UserModel;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.annotations.*;

import static com.telerikacademy.testframework.utils.Constants.BASE_URL;
import static com.telerikacademy.testframework.utils.UserRoles.ROLE_ADMIN;

public class BaseTestSetup {

    protected api.WEareApi WEareApi;
    protected UserModel globalAdminUser;
    protected Helpers helpers;

    @BeforeClass
    public void setup() {

        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        WEareApi = new WEareApi();
        helpers = new Helpers();

        globalAdminUser = WEareApi.registerUser(ROLE_ADMIN.toString());

    }

}
