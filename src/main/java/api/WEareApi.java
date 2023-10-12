package api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.telerikacademy.testframework.Constants.API;
import static com.telerikacademy.testframework.Constants.BASE_URL;
import static com.telerikacademy.testframework.Endpoints.USER_BY_ID;
import static io.restassured.RestAssured.given;

public class WEareApi {

    private static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        return sdf.format(date);
    }

    private static String randomAlphanumericString(int length) {
        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

//    protected RequestSpecification getRestAssured() {
//        Gson deserializer = new Gson();
//        String baseUri = PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
//                .getProperty("jira.api.baseUrl") + PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
//                .getProperty("jira.api.version");
//        String username = Utils.getConfigPropertyByKey("weare.username");
//        String token = Utils.getConfigPropertyByKey("jira.apiToken");
//        return RestAssured
//                .given()
//                .header("Content-Type", "application/json")
//                .baseUri(baseUri)
//                .auth()
//                .preemptive()
//                .basic(username, token);
//    }

    public UserModel getUserById(String userId, String principal) {
        EncoderConfig encoderConfig = RestAssured.config().getEncoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false);

        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig);

        RestAssured.baseURI = BASE_URL;

        Response response = given()
                .contentType("application/json")
                .queryParam("principal", principal)
                .get(String.format(API + USER_BY_ID, userId));

        UserModel userModel = new Gson().fromJson(response.getBody().asString(), UserModel.class);
        return userModel;
    }

}