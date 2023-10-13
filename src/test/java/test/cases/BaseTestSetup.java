package test.cases;

import api.WEareApi;
import com.telerikacademy.testframework.utils.Helpers;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.telerikacademy.testframework.models.*;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.log4testng.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;

import static com.telerikacademy.testframework.utils.Constants.*;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static com.telerikacademy.testframework.utils.JSONRequests.*;
import static com.telerikacademy.testframework.utils.UserRoles.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class BaseTestSetup {

    protected WEareApi weareApi;
    protected UserModel globalAdminUser;

    public BaseTestSetup() {
    }
    @BeforeClass
    public void setup() {
        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        weareApi = new WEareApi();
        globalAdminUser = weareApi.registerUser(ROLE_ADMIN.toString());

        Logger logger;

    }

    @AfterClass
    public void clear() {
        weareApi.disableUser(globalAdminUser, globalAdminUser.getId());
    }

}
