package test.cases;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

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

    }

}
