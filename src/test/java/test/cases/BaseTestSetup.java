package test.cases;

import api.WEareApi;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.annotations.BeforeClass;

import static com.telerikacademy.testframework.utils.Constants.BASE_URL;

public class BaseTestSetup {

    protected api.WEareApi WEareApi;
    protected Helpers helpers;

    @BeforeClass
    public void setup() {

        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        WEareApi = new WEareApi();
        helpers = new Helpers();

    }

}
