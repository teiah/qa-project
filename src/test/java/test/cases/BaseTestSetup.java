package test.cases;

import api.WEareApi;
import com.telerikacademy.testframework.UserActions;
import com.telerikacademy.testframework.models.UserModel;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.log4testng.Logger;

import static com.telerikacademy.testframework.utils.Constants.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;

public class BaseTestSetup {



    @BeforeClass
    public void setup() {


    }

    @AfterClass
    public void tearDown() {

    }

}
