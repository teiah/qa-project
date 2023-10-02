package api;

import com.google.gson.Gson;
import com.telerikacademy.testframework.PropertiesManager;
import com.telerikacademy.testframework.Utils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    protected RequestSpecification getRestAssured() {
        Gson deserializer = new Gson();
        String baseUri = PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
                .getProperty("jira.api.baseUrl") + PropertiesManager.PropertiesManagerEnum.INSTANCE.getConfigProperties()
                .getProperty("jira.api.version");
        String username = Utils.getConfigPropertyByKey("weare.username");
        String token = Utils.getConfigPropertyByKey("jira.apiToken");
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .baseUri(baseUri)
                .auth()
                .preemptive()
                .basic(username, token);
    }

}