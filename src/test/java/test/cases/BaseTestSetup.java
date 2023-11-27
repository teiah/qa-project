package test.cases;

import com.telerikacademy.testframework.models.User;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.*;
import org.testng.annotations.BeforeClass;
import com.telerikacademy.testframework.utils.*;
import api.controllers.UserController;

import static com.telerikacademy.testframework.utils.Constants.BASE_URL;

public class BaseTestSetup {

    @BeforeClass
    public void setup(ITestContext ctx) {

        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        TestRunner runner = (TestRunner) ctx;
        runner.setOutputDirectory("/target");

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }



    public static String getCookieValue(User user) {
        String name = Utils.getConfigPropertyByKey("auth.cookieName");
        Response auth = UserController.authUser(user.getUsername(), user.getPassword());
        return auth.getDetailedCookie(name).getValue();
    }

}
