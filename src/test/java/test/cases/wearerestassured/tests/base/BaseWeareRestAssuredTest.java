package test.cases.wearerestassured.tests.base;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.annotations.BeforeClass;
import test.cases.BaseTestSetup;

import static com.telerikacademy.testframework.utils.Constants.BASE_URL;


public class BaseWeareRestAssuredTest extends BaseTestSetup {

    @BeforeClass
    public void setUpRestAssured() {
        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

    }

}
