package api.controllers;

import com.google.gson.Gson;
import com.telerikacademy.testframework.utils.Helpers;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.Response;
import api.models.models.Skill;
import api.models.models.User;

import static com.telerikacademy.testframework.utils.Constants.API;
import static com.telerikacademy.testframework.utils.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class SkillController extends BaseWeAreApi{

    public static final String skillBody = "{\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"skill\": \"%s\",\n" +
            "  \"skillId\": 0\n" +
            "}";

    public static Skill[] getAllSkills() {

        Response response = given()
                .get(API + FIND_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        Skill[] skills = new Gson().fromJson(response.getBody().asString(), Skill[].class);

        return skills;

    }

    public static Skill createSkill(User user) {

        int categoryId = 100;
        String categoryName = "All";
        String skillService = Helpers.generateSkill();

        String body = String.format(skillBody, categoryId, categoryName, skillService);

        Response response = given()
                .auth()
                .form(user.getUsername(), user.getPassword(),
                        new FormAuthConfig(AUTHENTICATE, "username", "password"))
                .contentType("application/json")
                .body(body)
                .post(API + CREATE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        Skill skill = response.as(Skill.class);

        LOGGER.info(String.format("Skill %s created in category %s.", skillService, categoryName));

        return skill;
    }

    public static boolean skillExists(int skillId) {

        Skill[] skills = getAllSkills();

        for (Skill skill : skills) {
            if (skill.getSkillId() == skillId) {
                return true;
            }
        }

        return false;

    }

    public static void deleteSkill(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .put(API + DELETE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        LOGGER.info("Skill deleted.");
    }

    public static Skill getSkillById(int skillId) {

        Response response = given()
                .queryParam("skillId", skillId)
                .get(API + GET_ONE_SKILL);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200.");

        return response.as(Skill.class);

    }

    public static Response editSkill(int skillId) {

        String skillService = Helpers.generateSkill();

        Response response = given()
                .queryParam("skill", skillService)
                .queryParam("skillId", skillId)
                .put(API + EDIT_SKILL);

        return response;

    }
}
